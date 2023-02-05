package com.jihad.aiwriter.helpers

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.jihad.aiwriter.App
import com.jihad.aiwriter.App.Companion.languageIdentifier

object HelperTranslate {

    fun downloadLanguageModel(languageCode: String, onSuccessListener: () -> Unit) {
        // Download english model.
        val englishModel = TranslateRemoteModel.Builder(languageCode).build()
        App.modelManager.isModelDownloaded(englishModel).addOnSuccessListener(
            OnSuccessListener<Boolean?> { aBoolean ->
                if (!aBoolean!!) { // model is not downloaded
                    val conditions = DownloadConditions.Builder()
                        .requireWifi()
                        .build()
                    App.modelManager.download(englishModel, conditions)
                        .addOnSuccessListener(OnSuccessListener<Void?> {
                            onSuccessListener()
                        })
                        .addOnFailureListener(OnFailureListener {
                            Helpers.logD("error")
                            // Error.
                        })
                }else
                    onSuccessListener()
            })
    }

    fun identifyText(text: String, onSuccessListener: (String) -> Unit) {
        languageIdentifier.identifyLanguage(text.trim())
            .addOnSuccessListener(
                OnSuccessListener<String?> { languageCode ->
                    if (languageCode != "und") {
                        onSuccessListener(languageCode)
                    }
                })
            .addOnFailureListener(
                OnFailureListener {
                    // Model couldn’t be loaded or other internal error.
                    // ...
                })
    }

    fun translating(
        languageCode: String,
        text: String,
        chosenLanguage: String,
        onSuccessListener: (String) -> Unit
    ) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(languageCode)
            .setTargetLanguage(
                chosenLanguage
            )
            .build()
        val translator = Translation.getClient(options)
        translator.translate(text)
            .addOnSuccessListener { translatedText -> // Translation successful.
                onSuccessListener(translatedText)
            }
            .addOnFailureListener { }
    }
}