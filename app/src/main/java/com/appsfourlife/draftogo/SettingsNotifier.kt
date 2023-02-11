package com.appsfourlife.draftogo

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.helpers.HelperSharedPreference

object SettingsNotifier {

    val showDialogNbOfGenerationsLeftExceeded : MutableState<Boolean> = mutableStateOf(false)
    val nbOfGenerationsLeft = mutableStateOf(HelperSharedPreference.getInt(HelperSharedPreference.SP_SETTINGS, HelperSharedPreference.SP_SETTINGS_NB_OF_GENERATIONS_LEFT, Constants.MAX_NB_OF_TRIES_ALLOWED))
    val isSubscribed = mutableStateOf(false)
}