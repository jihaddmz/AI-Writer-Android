package com.appsfourlife.draftogo.feature_generate_text.components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.MyCustomConfirmationDialog
import com.appsfourlife.draftogo.components.MyText
import com.appsfourlife.draftogo.helpers.HelperAds
import com.appsfourlife.draftogo.helpers.HelperFirebaseDatabase
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.helpers.HelperUI
import com.appsfourlife.draftogo.util.SettingsNotifier

@Composable
fun DialogPricingType(
    title: String,
    modifier: Modifier = Modifier,
    showDialog: MutableState<Boolean>,
    positiveBtnText: String = stringResource(id = R.string.subscribe),
    onPositiveBtnClick: () -> Unit
) {
    val currentActivity = LocalContext.current as Activity
    val coroutineScope = rememberCoroutineScope()

    MyCustomConfirmationDialog(
        modifier = modifier,
        showDialog = showDialog,
        negativeBtnText = stringResource(id = R.string.watch_an_ad),
        positiveBtnText = positiveBtnText,
        onPositiveBtnClick = {
            onPositiveBtnClick()
        }, onNegativeBtnClick = {
            if (SettingsNotifier.isConnected.value) {
                SettingsNotifier.showLoadingDialog.value = true
                HelperAds.loadAds {
                    HelperAds.showAds(currentActivity) { amount ->
                        SettingsNotifier.nbOfGenerationsConsumed.value -= 1
                        HelperSharedPreference.setInt(
                            HelperSharedPreference.SP_SETTINGS,
                            HelperSharedPreference.SP_SETTINGS_NB_OF_GENERATIONS_CONSUMED,
                            SettingsNotifier.nbOfGenerationsConsumed.value
                        )
                        HelperFirebaseDatabase.decrementNbOfConsumed()
                        showDialog.value = false
                    }
                }
            } else {
                HelperUI.showToast(msg = App.getTextFromString(textID = R.string.no_connection))
            }
        }) {
        MyText(text = title)
    }

}