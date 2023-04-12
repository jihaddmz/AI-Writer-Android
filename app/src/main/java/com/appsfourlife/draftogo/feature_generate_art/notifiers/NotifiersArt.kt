package com.appsfourlife.draftogo.feature_generate_art.notifiers

import androidx.compose.runtime.mutableStateOf
import com.appsfourlife.draftogo.feature_generate_art.data.model.ModelArtHistory
import com.appsfourlife.draftogo.helpers.HelperSharedPreference

object NotifiersArt {

    val credits by lazy { mutableStateOf(HelperSharedPreference.getNbOfArtsCredits()) }
    val listOfPromptHistory = mutableStateOf(listOf<ModelArtHistory>())
}