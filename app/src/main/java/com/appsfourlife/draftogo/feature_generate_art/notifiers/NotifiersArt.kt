package com.appsfourlife.draftogo.feature_generate_art.notifiers

import androidx.compose.runtime.mutableStateOf
import com.appsfourlife.draftogo.feature_generate_art.data.model.ModelArtHistory

object NotifiersArt {

    val credits = mutableStateOf(0)
    val listOfPromptHistory = mutableStateOf(listOf<ModelArtHistory>())
}