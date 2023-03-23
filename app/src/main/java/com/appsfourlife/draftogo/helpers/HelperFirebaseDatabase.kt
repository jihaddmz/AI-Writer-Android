package com.appsfourlife.draftogo.helpers

import androidx.compose.runtime.MutableState
import com.appsfourlife.draftogo.feature_generate_text.models.ModelHistory
import com.appsfourlife.draftogo.feature_generate_text.models.ModelTemplateIcon
import com.appsfourlife.draftogo.util.SettingsNotifier
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object HelperFirebaseDatabase {
    private val firestore by lazy { Firebase.firestore }

    fun writeHistoryEntry(type: String, input: String, output: String) {
        val map = hashMapOf(
            "type" to type,
            "input" to input,
            "output" to output,
            "date" to HelperDate.getCurrentDateWithSec()
        )

        firestore.collection("users")
            .document(HelperAuth.auth.currentUser?.email!!)
            .set(
                hashMapOf(
                    "nbOfGenerationsConsumed" to HelperSharedPreference.getNbOfGenerationsConsumed(),
                    "nbOfWordsGenerated" to HelperSharedPreference.getNbOfWordsGenerated(),
                    "renewalDate" to HelperAuth.getExpirationDate()
                )
            )

        firestore.collection("users")
            .document(HelperAuth.auth.currentUser?.email!!)
            .collection("history")
            .document()
            .set(map)
            .addOnCompleteListener {
            }
    }

    fun fetchNbOfGenerationsConsumed() {
        firestore.collection("users")
            .document(HelperAuth.auth.currentUser?.email!!)
            .get().addOnCompleteListener {
                val nbOfGenerationsConsumed = it.result.get("nbOfGenerationsConsumed") as Long?
                val nbOfWordsGenerated = it.result.get("nbOfWordsGenerated") as Long?
                if (nbOfGenerationsConsumed == null) { // no field nbOfGenerationsConsumed yet
                    HelperSharedPreference.setInt(
                        HelperSharedPreference.SP_SETTINGS,
                        HelperSharedPreference.SP_SETTINGS_NB_OF_GENERATIONS_CONSUMED,
                        0
                    )
                    SettingsNotifier.nbOfGenerationsConsumed.value = 0
                } else {
                    HelperSharedPreference.setInt(
                        HelperSharedPreference.SP_SETTINGS,
                        HelperSharedPreference.SP_SETTINGS_NB_OF_GENERATIONS_CONSUMED,
                        nbOfGenerationsConsumed.toInt()
                    )
                    SettingsNotifier.nbOfGenerationsConsumed.value = nbOfGenerationsConsumed.toInt()
                }
                if (nbOfWordsGenerated == null) { // no field nbOfWordsGenerated yet
                    HelperSharedPreference.setInt(
                        HelperSharedPreference.SP_SETTINGS,
                        HelperSharedPreference.SP_SETTINGS_NB_OF_WORDS_GENERATED,
                        0
                    )
                } else {
                    HelperSharedPreference.setInt(
                        HelperSharedPreference.SP_SETTINGS,
                        HelperSharedPreference.SP_SETTINGS_NB_OF_WORDS_GENERATED,
                        nbOfWordsGenerated.toInt()
                    )
                }
            }
    }

    fun fetchHistory(
        list: MutableState<MutableList<ModelHistory>>,
        noHistory: MutableState<Boolean>,
        showCircularIndicator: MutableState<Boolean>
    ) {
        val result: MutableList<ModelHistory> = mutableListOf()

        firestore.collection("users")
            .document(HelperAuth.auth.currentUser?.email!!)
            .collection("history")
            .get()
            .addOnCompleteListener {
                if (it.result.documents.isEmpty()) {
                    noHistory.value = true
                } else
                    it.result.documents.forEach { documentSnapshot ->
                        result.add(
                            ModelHistory(
                                type = documentSnapshot.get("type").toString().trim(),
                                input = documentSnapshot.get("input").toString().trim(),
                                output = documentSnapshot.get("output").toString().trim(),
                                date = documentSnapshot.get("date").toString().trim()
                            )
                        )
                    }
                list.value = result
                showCircularIndicator.value = false
            }
    }

    fun fetchAppVersion(onResultFetched: (String) -> Unit) {
        firestore.collection("app").document("settings").get().addOnCompleteListener {
            onResultFetched(it.result.get("appVersion").toString())
        }
    }

    fun deleteAllHistory() {
        firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!)
            .collection("history").get().addOnCompleteListener {
                it.result.documents.forEach { documentSnapshot ->
                    documentSnapshot.reference.delete()
                }
            }
    }

    fun resetNbOfGenerationsConsumedAndNbOfWordsGenerated() {
        firestore.collection("users")
            .document(HelperAuth.auth.currentUser?.email!!)
            .set(
                hashMapOf(
                    "nbOfGenerationsConsumed" to 0,
                    "nbOfWordsGenerated" to 0,
                    "renewalDate" to HelperAuth.getExpirationDate()
                )
            )
    }

    fun getRenewalDate(onQueryComplete: (String) -> Unit) {
        firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!)
            .get().addOnCompleteListener {
                onQueryComplete(it.result.get("renewalDate").toString())
            }
    }

    fun setRenewalDate() {
        firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!)
            .set(
                hashMapOf(
                    "renewalDate" to HelperAuth.getExpirationDate()
                )
            )
    }

    fun decrementNbOfConsumed() {
        val nbOfConsumed = HelperSharedPreference.getNbOfGenerationsConsumed()

        firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!)
            .update("nbOfGenerationsConsumed", nbOfConsumed)
    }

    fun updateNbOfWords() {
        firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!)
            .update("nbOfWordsGenerated", Constants.BASE_PLAN_MAX_NB_OF_WORDS - 500)
    }

    fun getAllTemplateIcons(list: MutableState<List<ModelTemplateIcon>>) {
        val result = mutableListOf<ModelTemplateIcon>()
        firestore.collection("app").document("templates").collection("icons")
            .get().addOnCompleteListener { task ->
                task.result.documents.forEach {
                    val name = it.getString("name")
                    val url = it.getString("url")

                    name?.let {
                        result.add(ModelTemplateIcon(name = name, url = url!!))
                    }
                }
                list.value = result
            }
    }
}