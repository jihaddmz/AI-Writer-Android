package com.appsfourlife.draftogo

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.helpers.HelperSharedPreference

object SettingsNotifier {

    val showDialogNbOfGenerationsLeftExceeded : MutableState<Boolean> = mutableStateOf(false)
    val isConnected : MutableState<Boolean> = mutableStateOf(true)
    val nbOfGenerationsLeft = mutableStateOf(HelperSharedPreference.getInt(HelperSharedPreference.SP_SETTINGS, HelperSharedPreference.SP_SETTINGS_NB_OF_GENERATIONS_LEFT, Constants.MAX_NB_OF_TRIES_ALLOWED))
    val isSubscribed = mutableStateOf(false)
    val isRenewable = mutableStateOf(false)
    val output = mutableStateOf("")
    val outputList = mutableStateListOf<String>()
    val input = mutableStateOf(TextFieldValue(text = ""))
    val name = mutableStateOf("")
    val jobTitle = mutableStateOf("")
    val stopTyping = mutableStateOf(false)

    fun resetValues(){
        stopTyping.value = true
        input.value = TextFieldValue(text = "")
        name.value = ""
        jobTitle.value = ""
        output.value = ""
        outputList.clear()
    }
}