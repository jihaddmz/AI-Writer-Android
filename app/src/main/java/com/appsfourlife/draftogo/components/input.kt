package com.appsfourlife.draftogo.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.extensions.animateOffsetX
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.SettingsNotifier
import kotlinx.coroutines.*
import java.util.*
import kotlin.concurrent.timerTask

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun input(
    modifier: Modifier = Modifier,
    label: String,
    inputPrefix: String = "",
    nbOfGenerations: Int = 1,
    verticalScrollState: ScrollState = rememberScrollState(),
    length: Int = Constants.MAX_GENERATION_LENGTH.toInt(),
    showDialog: MutableState<Boolean> = mutableStateOf(false),
    checkIfInputIsEmpty: Boolean = false
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val isGenerateBtnEnabled = remember {
        mutableStateOf(false)
    }

    val connectionError = stringResource(id = R.string.no_connection)

    Column(modifier = modifier) {

        Card(shape = Shapes.medium, border = BorderStroke(3.dp, color = Blue)) {

            Column {

                MyTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 100.dp),
                    onValueChanged = {
                        SettingsNotifier.input.value = TextFieldValue(text = it)
                        isGenerateBtnEnabled.value = it.isNotEmpty()
                    },
                    placeholder = label,
                    value = SettingsNotifier.input.value.text
                )

                /**
                 * input actions
                 **/
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = SpacersSize.small, bottom = SpacersSize.small),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        MySpacer(type = "small", widthOrHeight = "width")
                        SettingsNotifier.outputLanguage.value = myDropDown(
                            list = Constants.OUTPUT_LANGUAGES,
                        )
                    }

                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = {
                            SettingsNotifier.input.value = TextFieldValue(text = "")
                            isGenerateBtnEnabled.value = false
                        }, modifier = Modifier.animateOffsetX(initialOffsetX = 100.dp)) {
                            MyIcon(
                                iconID = R.drawable.clear, tint = Blue, contentDesc = stringResource(
                                    id = R.string.clear
                                )
                            )
                        }

                        MySpacer(type = "small", widthOrHeight = "width")

                        IconButton(onClick = {
                            SettingsNotifier.input.value = Helpers.pasteFromClipBoard(
                                mutableStateOf(SettingsNotifier.input.value), context
                            )
                            if (SettingsNotifier.input.value.text.isNotEmpty())
                                isGenerateBtnEnabled.value = true
                        }, modifier = Modifier.animateOffsetX(initialOffsetX = 100.dp)) {
                            MyIcon(
                                iconID = R.drawable.icon_paste,
                                tint = Blue,
                                contentDesc = stringResource(
                                    id = R.string.paste
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(SpacersSize.small))

        /**
         * generate button
         **/
        val generateText = remember {
            mutableStateOf(App.getTextFromString(R.string.generate))
        }
        MyButton(modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
            text = generateText.value,
            isEnabled = isGenerateBtnEnabled.value,
            content = {
                MyAnimatedVisibility(visible = generateText.value == App.getTextFromString(R.string.generated)) {
                    Row {
                        MySpacer(type = "small", widthOrHeight = "width")
                        MyIcon(
                            iconID = R.drawable.icon_checkcircle,
                            tint = Color.White,
                            contentDesc = stringResource(id = R.string.done)
                        )
                    }
                }
            }) {

            if (!SettingsNotifier.isConnected.value) {
                HelperUI.showToast(
                    context,
                    connectionError,
                )
                return@MyButton
            }

            if (checkIfInputIsEmpty) // so if the user coming from custom screen, so there is no input prefix
            // then the model will generate gibberish content
                if (SettingsNotifier.input.value.text.trim().isEmpty()) {
                    HelperUI.showToast(msg = App.getTextFromString(R.string.no_input_entered))
                    return@MyButton
                }

            /**
             * if user is on base plan subscription and nb of words generated is at the max, prevent him from
             * generating extra prompts
             **/
            if (HelperAuth.isSubscribed())
                if (HelperSharedPreference.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_BASE && HelperSharedPreference.getNbOfWordsGenerated() >= Constants.BASE_PLAN_MAX_NB_OF_WORDS) {
                    SettingsNotifier.basePlanMaxNbOfWordsExceeded.value = true
                    return@MyButton
                }

            if (HelperSharedPreference.getNbOfGenerationsConsumed() >= 2 && !HelperAuth.isSubscribed()) { // if nbOfGenerationsConsumed is >= 2
                // and the user is not subscribed, force the user to subscribe
                SettingsNotifier.showDialogNbOfGenerationsLeftExceeded.value = true
            } else {
                keyboardController?.hide()
                isGenerateBtnEnabled.value = false
                showDialog.value = true
                generateText.value = App.getTextFromString(R.string.generating)

                HelperChatGPT.getResponse(
                    inputPrefix + " " + SettingsNotifier.input.value.text.trim(),
                    context,
                    length,
                    nbOfGenerations,
                    isGenerateBtnEnabled,
                    coroutineScope = coroutineScope,
                    onErrorAction = {
                        showDialog.value = false
                        isGenerateBtnEnabled.value = true
                        generateText.value = App.getTextFromString(R.string.generate)

                    }, verticalScrollState = verticalScrollState
                ) { // on fetching response action done
                    showDialog.value = false
                    generateText.value = App.getTextFromString(R.string.generated)

                    if (!HelperAuth.isSubscribed()) { // if the user is not yet subscribed, decrement the nb of generations left
                        SettingsNotifier.nbOfGenerationsConsumed.value += 1
                    }

                    Timer().schedule(timerTask {
                        generateText.value = App.getTextFromString(R.string.generate)
                    }, 1500)
                }
            }
        }
    }
}