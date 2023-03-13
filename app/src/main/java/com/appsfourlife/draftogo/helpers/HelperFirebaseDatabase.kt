package com.appsfourlife.draftogo.helpers

import androidx.compose.runtime.MutableState
import com.appsfourlife.draftogo.feature_generate_text.models.ModelHistory
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object HelperFirebaseDatabase {
    private val firestore by lazy { Firebase.firestore }

    fun writeHistoryEntry(type: String, input: String, output: String) {
        val map = hashMapOf(
            "type" to type,
            "input" to input,
            "output" to output,
            "date" to HelperDate.getCurrentDate()
        )

        firestore.collection("users")
            .document(HelperAuth.auth.currentUser?.email!!)
            .set(
                hashMapOf(
                    "nbOfGenerationsConsumed" to HelperSharedPreference.getNbOfGenerationsConsumed(),
                    "nbOfWordsGenerated" to HelperSharedPreference.getNbOfWordsGenerated()
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
                } else {
                    HelperSharedPreference.setInt(
                        HelperSharedPreference.SP_SETTINGS,
                        HelperSharedPreference.SP_SETTINGS_NB_OF_GENERATIONS_CONSUMED,
                        nbOfGenerationsConsumed.toInt()
                    )
                }
                if (nbOfWordsGenerated == null) { // no field nbOfGenerationsConsumed yet
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
}