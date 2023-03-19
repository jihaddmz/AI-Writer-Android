package com.appsfourlife.draftogo.util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.google.android.gms.ads.rewarded.RewardedAd

object SettingsNotifier {

    val showDialogNbOfGenerationsLeftExceeded : MutableState<Boolean> = mutableStateOf(false)
    val isConnected : MutableState<Boolean> = mutableStateOf(true)
    val nbOfGenerationsConsumed = mutableStateOf(HelperSharedPreference.getNbOfGenerationsConsumed())
    val isSubscribed = mutableStateOf(false)
    val isRenewable = mutableStateOf(false)
    val output = mutableStateOf("")
    val outputList = mutableStateListOf<String>()
    val input = mutableStateOf(TextFieldValue(text = ""))
    val name = mutableStateOf("")
    val jobTitle = mutableStateOf("")
    val stopTyping = mutableStateOf(false)
    val basePlanMaxNbOfWordsExceeded = mutableStateOf(false)
    val showLoadingDialog = mutableStateOf(false)
    var mRewardedAds: RewardedAd? = null
    var disableDrawerContent = mutableStateOf(false)
    var templateType = ""

    fun resetValues(){
        stopTyping.value = true
        input.value = TextFieldValue(text = "")
        name.value = ""
        jobTitle.value = ""
        output.value = ""
        outputList.clear()
    }
}