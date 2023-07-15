package com.appsfourlife.draftogo.feature_generate_video.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object NotifiersVideo {

    var videoGeneratedIUri: String? = null
    var stopExoPlayers: MutableState<Boolean> = mutableStateOf(false)

    fun clearInstances() {
        videoGeneratedIUri = null
        stopExoPlayers.value = false
    }
}