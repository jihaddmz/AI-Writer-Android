package com.appsfourlife.draftogo.helpers

import androidx.compose.runtime.MutableState
import com.appsfourlife.draftogo.feature_generate_art.notifiers.NotifiersArt
import com.appsfourlife.draftogo.feature_generate_text.models.ModelHistory
import com.appsfourlife.draftogo.feature_generate_text.models.ModelTemplateIcon
import com.appsfourlife.draftogo.util.SettingsNotifier
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
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
                ), SetOptions.merge()
            )

        firestore.collection("users")
            .document(HelperAuth.auth.currentUser?.email!!)
            .collection("history")
            .document()
            .set(map)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }

    fun setNbOfGenerationsConsumedAndNbOfWordsGenerated() {
        firestore.collection("users")
            .document(HelperAuth.auth.currentUser?.email!!)
            .set(
                hashMapOf(
                    "nbOfGenerationsConsumed" to HelperSharedPreference.getNbOfGenerationsConsumed(),
                    "nbOfWordsGenerated" to HelperSharedPreference.getNbOfWordsGenerated(),
                ), SetOptions.merge()
            )
    }

    fun fetchNbOfGenerationsConsumedAndNbOfWordsGenerated(onComplete: () -> Unit) {
        firestore.collection("users")
            .document(HelperAuth.auth.currentUser?.email!!)
            .get()
            .addOnSuccessListener {
                val nbOfGenerationsConsumed = it.get("nbOfGenerationsConsumed") as Long?
                val nbOfWordsGenerated = it.get("nbOfWordsGenerated") as Long?
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

                onComplete()
            }.addOnFailureListener {

            }
    }

    fun fetchNbOfArtsAndVideosAndChatAndCompletionWordsGenerated() {
        firestore.collection("users")
            .document(HelperAuth.auth.currentUser?.email!!)
            .get()
            .addOnSuccessListener {
                val nbOfArtsGenerated = it.get("nbOfArtsGenerated") as Long?
                if (nbOfArtsGenerated == null) {
                    HelperSharedPreference.setNbOfArtsGenerated(0)
                } else {
                    HelperSharedPreference.setNbOfArtsGenerated(nbOfArtsGenerated.toInt())
                }

                val nbOfVideosGenerated = it.get("nbOfVideosGenerated") as Long?
                if (nbOfVideosGenerated == null) {
                    HelperSharedPreference.setNbOfVideosGenerated(0)
                } else {
                    HelperSharedPreference.setNbOfVideosGenerated(nbOfVideosGenerated.toInt())
                }

                val nbOfChatWordsGenerated = it.get("nbOfChatWordsGenerated") as Long?
                if (nbOfChatWordsGenerated == null) {
                    HelperSharedPreference.setNbOfChatWordsGenerated(0)
                } else {
                    HelperSharedPreference.setNbOfChatWordsGenerated(nbOfChatWordsGenerated.toInt())
                }

                val nbOfCompletionWordsGenerated = it.get("nbOfCompletionWordsGenerated") as Long?
                if (nbOfCompletionWordsGenerated == null) {
                    HelperSharedPreference.setNbOfCompletionWordsGenerated(0)
                } else {
                    HelperSharedPreference.setNbOfCompletionWordsGenerated(nbOfCompletionWordsGenerated.toInt())
                }
            }
            .addOnFailureListener {

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
            .addOnSuccessListener { task ->
                if (task.documents.isEmpty()) {
                    noHistory.value = true
                } else {
                    task.documents.forEach { documentSnapshot ->
                        result.add(
                            ModelHistory(
                                type = documentSnapshot.get("type").toString().trim(),
                                input = documentSnapshot.get("input").toString().trim(),
                                output = documentSnapshot.get("output").toString().trim(),
                                date = documentSnapshot.get("date").toString().trim()
                            )
                        )
                    }
                    list.value = result.sortedBy {
                        HelperDate.parseStringToDate(
                            it.date,
                            "yyyy-MM-dd HH:mm:ss"
                        )?.time
                    } as MutableList<ModelHistory>
                    list.value = list.value.reversed() as MutableList<ModelHistory>
                }
                showCircularIndicator.value = false
            }.addOnFailureListener {

            }
    }

    fun fetchAppVersion(onResultFetched: (String) -> Unit) {
        firestore.collection("app").document("settings").get().addOnSuccessListener {
            onResultFetched(it.get("appVersion").toString())
        }.addOnFailureListener {

        }
    }

    fun deleteAllHistory() {
        firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!)
            .collection("history")
            .get()
            .addOnSuccessListener {
                it.documents.forEach { documentSnapshot ->
                    documentSnapshot.reference.delete()
                }
            }
            .addOnFailureListener {

            }
    }

    fun resetNbOfGenerationsConsumedAndNbOfWordsGeneratedAndNbOfArtsGenerated() {
        firestore.collection("users")
            .document(HelperAuth.auth.currentUser?.email!!)
            .set(
                hashMapOf(
                    "nbOfWordsGenerated" to 0,
                    "nbOfArtsGenerated" to 0,
                    "nbOfVideosGenerated" to 0,
                    "nbOfChatWordsGenerated" to 0,
                    "nbOfCompletionWordsGenerated" to 0,
                    "renewalDate" to HelperAuth.getExpirationDate()
                ), SetOptions.merge()
            )

        HelperSharedPreference.setNbOfArtsGenerated(0)
        HelperSharedPreference.setNbOfWordsGenerated(0)
        HelperSharedPreference.setNbOfVideosGenerated(0)
        HelperSharedPreference.setNbOfChatWordsGenerated(0)
        HelperSharedPreference.setNbOfCompletionWordsGenerated(0)
    }

    fun getRenewalDate(onQueryComplete: (String) -> Unit) {
        firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!)
            .get()
            .addOnSuccessListener {
                onQueryComplete(it.get("renewalDate").toString())
            }
            .addOnFailureListener {

            }
    }

    fun setRenewalDate() {
        firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!)
            .set(
                hashMapOf(
                    "renewalDate" to HelperAuth.getExpirationDate()
                ), SetOptions.merge()
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
            .get()
            .addOnSuccessListener { task ->
                task.documents.forEach {
                    val name = it.getString("name")
                    val url = it.getString("url")

                    name?.let {
                        result.add(ModelTemplateIcon(name = name, url = url!!))
                    }
                }
                list.value = result
            }
            .addOnFailureListener {

            }
    }

    fun setNbOfArtCredits() {
        val hashmap = hashMapOf(
            "nbOfArtCredits" to NotifiersArt.credits.value
        )
        HelperAuth.auth.currentUser?.email?.let {
            firestore.collection("users").document(it).set(hashmap, SetOptions.merge())
                .addOnSuccessListener {

                }
                .addOnFailureListener {

                }
        }
    }

    fun getNbOfArtCredits(onComplete: () -> Unit = {}) {
        firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!).get()
            .addOnSuccessListener { document ->
                val result = document.get("nbOfArtCredits").toString()
                if (result != "null") {
                    NotifiersArt.credits.value = result.toInt()
                    HelperSharedPreference.setNbOfArtsCredits()
                }

                onComplete()
            }
            .addOnFailureListener {

            }
    }

    fun getRewardText(onSuccess: (String?) -> Unit) {
        if (SettingsNotifier.isConnected.value) {
            firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!).get()
                .addOnSuccessListener { document ->
                    onSuccess(document.get("textReward").toString())
                    resetRewardText()
                }
                .addOnFailureListener {

                }
        }
    }

    fun resetRewardText() {
        if (SettingsNotifier.isConnected.value) {
            val hashmap = hashMapOf(
                "textReward" to ""
            )

            firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!)
                .set(hashmap, SetOptions.merge())
                .addOnSuccessListener {

                }
                .addOnFailureListener {

                }
        }
    }

    fun fetchVideoTemplates(onSuccess: (DocumentSnapshot) -> Unit) {
        firestore.collection("video_templates").get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.documents.forEach { document ->
                    onSuccess(document)
                }
            }.addOnFailureListener {

            }
    }

    fun incrementNbOfArtsGenerated() {
        firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!).get()
            .addOnSuccessListener { documentSnapshot ->
                val nbOfArtsGenerated = documentSnapshot.get("nbOfArtsGenerated")
                if (nbOfArtsGenerated == null){ // this field is not yet set in firebase
                    val hashmap = hashMapOf(
                        "nbOfArtsGenerated" to HelperSharedPreference.getNbOfArtsGenerated() + 1
                    )
                    firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!)
                        .set(hashmap, SetOptions.merge())
                } else {
                    val hashmap = hashMapOf(
                        "nbOfArtsGenerated" to (nbOfArtsGenerated as Long).toInt() + 1
                    )
                    HelperSharedPreference.setNbOfArtsGenerated((nbOfArtsGenerated as Long + 1).toInt())
                    firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!)
                        .set(hashmap, SetOptions.merge())
                }
            }
            .addOnFailureListener {

            }
    }

    fun incrementNbOfVideosGenerated() {
        firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!).get()
            .addOnSuccessListener { documentSnapshot ->
                val nbOfVideosGenerated = documentSnapshot.get("nbOfVideosGenerated")
                if (nbOfVideosGenerated == null){ // this field is not yet set in firebase
                    val hashmap = hashMapOf(
                        "nbOfVideosGenerated" to HelperSharedPreference.getNbOfVideosGenerated() + 1
                    )
                    firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!)
                        .set(hashmap, SetOptions.merge())
                } else {
                    val hashmap = hashMapOf(
                        "nbOfVideosGenerated" to (nbOfVideosGenerated as Long).toInt() + 1
                    )
                    HelperSharedPreference.setNbOfVideosGenerated((nbOfVideosGenerated as Long + 1).toInt())
                    firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!)
                        .set(hashmap, SetOptions.merge())
                }
            }
            .addOnFailureListener {

            }
    }

    fun setNbOfChatWordsGenerated(value: Int) {
        firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!).get()
            .addOnSuccessListener { documentSnapshot ->
                val nbOfChatWordsGenerated = documentSnapshot.get("nbOfChatWordsGenerated") as Long?
                if (nbOfChatWordsGenerated == null) {
                    val hashmap = hashMapOf(
                        "nbOfChatWordsGenerated" to value
                    )
                    firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!)
                        .set(hashmap, SetOptions.merge())
                    HelperSharedPreference.setNbOfChatWordsGenerated(value)
                } else {
                    val hashmap = hashMapOf(
                        "nbOfChatWordsGenerated" to nbOfChatWordsGenerated.toInt() + value
                    )
                    firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!)
                        .set(hashmap, SetOptions.merge())
                    HelperSharedPreference.setNbOfChatWordsGenerated(nbOfChatWordsGenerated.toInt() + value)

                }
            }
            .addOnFailureListener {

            }
    }

    fun setNbOfCompletionWordsGenerated(value: Int) {
        firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!).get()
            .addOnSuccessListener { documentSnapshot ->
                val nbOfCompletionWordsGenerated = documentSnapshot.get("nbOfCompletionWordsGenerated") as Long?
                if (nbOfCompletionWordsGenerated == null) {
                    val hashmap = hashMapOf(
                        "nbOfCompletionWordsGenerated" to value
                    )
                    firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!)
                        .set(hashmap, SetOptions.merge())
                    HelperSharedPreference.setNbOfCompletionWordsGenerated(value)
                } else {
                    val hashmap = hashMapOf(
                        "nbOfCompletionWordsGenerated" to nbOfCompletionWordsGenerated.toInt() + value
                    )
                    firestore.collection("users").document(HelperAuth.auth.currentUser?.email!!)
                        .set(hashmap, SetOptions.merge())
                    HelperSharedPreference.setNbOfCompletionWordsGenerated(nbOfCompletionWordsGenerated.toInt() + value)

                }
            }
            .addOnFailureListener {

            }
    }
}