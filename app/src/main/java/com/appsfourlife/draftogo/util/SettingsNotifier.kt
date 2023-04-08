package com.appsfourlife.draftogo.util

import android.speech.tts.TextToSpeech
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelTemplate
import com.appsfourlife.draftogo.feature_generate_text.models.ModelComparedGenerationItem
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.google.android.gms.ads.rewarded.RewardedAd

object SettingsNotifier {

    val showDialogNbOfGenerationsLeftExceeded: MutableState<Boolean> = mutableStateOf(false)
    val showLoadingDialog = mutableStateOf(false)
    val basePlanMaxNbOfWordsExceeded = mutableStateOf(false)
    val showAddTemplateDialog = mutableStateOf(false)
    val showDeleteTemplateDialog = mutableStateOf(false)

    val isConnected: MutableState<Boolean> = mutableStateOf(true)
    val nbOfGenerationsConsumed =
        mutableStateOf(HelperSharedPreference.getNbOfGenerationsConsumed())
    val isSubscribed = mutableStateOf(false)
    val isRenewable = mutableStateOf(false)
    val output = mutableStateOf("")
    val outputList = mutableStateListOf<String>()
    val input = mutableStateOf(TextFieldValue(text = ""))
    val name = mutableStateOf("")
    val jobTitle = mutableStateOf("")
    val stopTyping = mutableStateOf(false)
    var mRewardedAds: RewardedAd? = null
    var disableDrawerContent = mutableStateOf(true)
    var enableSheetContent = mutableStateOf(false)
    var templateType = ""
    var predefinedTemplates = mutableStateOf(listOf<ModelTemplate>())
    var comparisonGenerationEntries = mutableStateOf(listOf<ModelComparedGenerationItem>())
    var templateToDelete: ModelTemplate? = null
    var currentQuerySection: String? = null
    var tts: TextToSpeech? = null
    val outputLanguage = mutableStateOf(HelperSharedPreference.getOutputLanguage())

    fun addComparisonGenerationEntry(modelComparedGenerationItem: ModelComparedGenerationItem) {
        val previous = comparisonGenerationEntries.value.toMutableList()
        previous.add(modelComparedGenerationItem)
        comparisonGenerationEntries.value = previous
    }
    fun deleteComparisonGenerationEntry(index: Int) {
        val previous = comparisonGenerationEntries.value.toMutableList()
        previous.removeAt(index)
        comparisonGenerationEntries.value = previous
    }

    fun resetValues() {
        stopTTS()
        tts = null
        stopTyping.value = true
        input.value = TextFieldValue(text = "")
        name.value = ""
        jobTitle.value = ""
        output.value = ""
        outputList.clear()
    }

    fun stopTTS() {
        tts?.let {
            if (it.isSpeaking)
                it.stop()
        }
    }
}