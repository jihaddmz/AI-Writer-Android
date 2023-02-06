package com.jihad.aiwriter

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.jihad.aiwriter.helpers.HelperSharedPreference

object SettingsNotifier {

    val showDialogNbOfGenerationsLeftExceeded : MutableState<Boolean> = mutableStateOf(false)
    val nbOfGenerationsLeft = mutableStateOf(HelperSharedPreference.getInt(HelperSharedPreference.SP_SETTINGS, HelperSharedPreference.SP_SETTINGS_NB_OF_GENERATIONS_LEFT, 3))
    val isSubscribed = mutableStateOf(false)
}