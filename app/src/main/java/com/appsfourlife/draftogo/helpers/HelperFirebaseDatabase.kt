package com.appsfourlife.draftogo.helpers

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object HelperFirebaseDatabase {
    private val firestore by lazy { Firebase.firestore }

    fun writeHistoryEntry(type: String, input: String, output: String) {
        val map = hashMapOf(
            "type" to type,
            "input" to input,
            "output" to output
        )

        firestore.collection("users")
            .document(HelperAuth.auth.currentUser?.email!!)
            .set(hashMapOf("nbOfGenerationsLeft" to HelperSharedPreference.getNbOfGenerationsLeft()))

        firestore.collection("users")
            .document(HelperAuth.auth.currentUser?.email!!)
            .collection("history")
            .document()
            .set(map)
            .addOnCompleteListener {
            }
    }

    fun fetchNbOfGenerationsLeft() {
        firestore.collection("users")
            .document(HelperAuth.auth.currentUser?.email!!)
            .get().addOnCompleteListener {
                Helpers.logD("${it.result.get("nbOfGenerationsLeft")}")
            }
    }

    fun fetchHistory() {
        firestore.collection("users")
            .document(HelperAuth.auth.currentUser?.email!!)
            .collection("history")
            .get()
            .addOnCompleteListener {
                it.result.documents.forEach { documentSnapshot ->
                    Helpers.logD("type ${documentSnapshot.get("type")} input ${documentSnapshot.get("input")} output ${documentSnapshot.get("output")}")
                    Helpers.logD("------------------------------------------------------------")
                }
            }
    }

}