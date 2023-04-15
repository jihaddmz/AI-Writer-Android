package com.appsfourlife.draftogo.home.util

import androidx.compose.runtime.mutableStateOf
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelFavoriteTemplate

object NotifiersHome {

    val listOfFavoriteTemplates = mutableStateOf(listOf<ModelFavoriteTemplate>())
}