package com.appsfourlife.draftogo.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.extensions.animateOffsetX
import com.appsfourlife.draftogo.feature_generate_text.components.DialogPricingType
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.ui.theme.*
import com.appsfourlife.draftogo.util.SettingsNotifier
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timerTask

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun input(
    modifier: Modifier = Modifier,
    label: String,
    inputPrefix: String = "",
    nbOfGenerations: Int = 1,
    verticalScrollState: ScrollState = rememberScrollState(),
    length: Int = Constants.MAX_GENERATION_LENGTH.toInt(),
    showDialog: MutableState<Boolean> = mutableStateOf(false),
    checkIfInputIsEmpty: Boolean = true
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val showPricingDialogTypes = remember {
        mutableStateOf(false)
    }
    val showPricingDialogTypesTitle = remember {
        mutableStateOf("")
    }

    val connectionError = stringResource(id = R.string.no_connection)

    Column(modifier = modifier) {

        if (showPricingDialogTypes.value && showPricingDialogTypesTitle.value == App.getTextFromString(
                textID = R.string.you_have_reached_max_nb_of_words_generated
            )
        ) {
            DialogPricingType(
                title = showPricingDialogTypesTitle.value,
                showDialog = showPricingDialogTypes,
                positiveBtnText = stringResource(id = R.string.upgrade)
            ) {
                SettingsNotifier.isBasePlanNbOfWordsExceeded.value = true
                coroutineScope.launch {
                    SettingsNotifier.isPricingBottomSheets.value = true
                    SettingsNotifier.sheetScaffoldState!!.bottomSheetState.animateTo(
                        BottomSheetValue.Expanded
                    )
                }
            }
        }

        if (showPricingDialogTypes.value && showPricingDialogTypesTitle.value != App.getTextFromString(
                textID = R.string.you_have_reached_max_nb_of_words_generated
            )
        ) {
            DialogPricingType(
                title = stringResource(id = R.string.no_generations_left),
                showDialog = showPricingDialogTypes
            ) {
                SettingsNotifier.isBasePlanNbOfWordsExceeded.value = false
                coroutineScope.launch {
                    SettingsNotifier.isPricingBottomSheets.value = true
                    SettingsNotifier.sheetScaffoldState!!.bottomSheetState.animateTo(
                        BottomSheetValue.Expanded
                    )
                }
            }
        }

        MyCardView {

            Column {

                MyTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 100.dp),
                    onValueChanged = {
                        SettingsNotifier.input.value = TextFieldValue(text = it)
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
                        SettingsNotifier.outputLanguage.value = inputDropDown(
                            list = Constants.OUTPUT_LANGUAGES,
                        )
                    }

                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = {
                            SettingsNotifier.input.value = TextFieldValue(text = "")
                        }, modifier = Modifier.animateOffsetX(initialOffsetX = 100.dp)) {
                            MyIcon(
                                iconID = R.drawable.clear,
                                tint = Rose,
                                contentDesc = stringResource(
                                    id = R.string.clear
                                )
                            )
                        }

                        MySpacer(type = "small", widthOrHeight = "width")

                        IconButton(onClick = {
                            SettingsNotifier.input.value = Helpers.pasteFromClipBoard(
                                mutableStateOf(SettingsNotifier.input.value), context
                            )
                        }, modifier = Modifier.animateOffsetX(initialOffsetX = 100.dp)) {
                            MyIcon(
                                iconID = R.drawable.icon_paste,
                                tint = Amber,
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
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Row(
                modifier = Modifier
                    .background(color = Blue, shape = Shapes.medium)
                    .padding(SpacersSize.small)
            ) {

                MyText(
                    modifier = Modifier
                        .padding(horizontal = SpacersSize.small)
                        .clickable {

                            if (generateText.value == App.getTextFromString(textID = R.string.generating))
                                return@clickable

                            if (!SettingsNotifier.isConnected.value) {
                                HelperUI.showToast(
                                    context,
                                    connectionError,
                                )
                                return@clickable
                            }

                            if (checkIfInputIsEmpty) // so if the user coming from custom screen, so there is no input prefix
                            // then the model will generate gibberish content
                                if (SettingsNotifier.input.value.text
                                        .trim()
                                        .isEmpty()
                                ) {
                                    HelperUI.showToast(msg = App.getTextFromString(R.string.no_input_entered))
                                    return@clickable
                                }

                            /**
                             * if user is on base plan subscription and nb of words generated is at the max, prevent him from
                             * generating extra prompts
                             **/
                            if (HelperAuth.isSubscribed())
                                if (HelperSharedPreference.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_BASE && HelperSharedPreference.getNbOfWordsGenerated() >= Constants.BASE_PLAN_MAX_NB_OF_WORDS) {
                                    showPricingDialogTypes.value = true
                                    showPricingDialogTypesTitle.value =
                                        App.getTextFromString(textID = R.string.you_have_reached_max_nb_of_words_generated)
                                    return@clickable
                                }

                            if (HelperSharedPreference.getNbOfGenerationsConsumed() >= 2 && !HelperAuth.isSubscribed()) { // if nbOfGenerationsConsumed is >= 2
                                // and the user is not subscribed, force the user to subscribe
                                showPricingDialogTypes.value = true
                                showPricingDialogTypesTitle.value = ""
                            } else {
                                keyboardController?.hide()
                                showDialog.value = true
                                generateText.value = App.getTextFromString(R.string.generating)

                                HelperChatGPT.getChatResponse(
                                    inputPrefix + " " + SettingsNotifier.input.value.text.trim() + ". Generate the response using the " + SettingsNotifier.outputLanguage.value + " language",
                                    context,
                                    length,
                                    nbOfGenerations,
                                    coroutineScope = coroutineScope,
                                    onErrorAction = {
                                        showDialog.value = false
                                        generateText.value =
                                            App.getTextFromString(R.string.generate)

                                    }, verticalScrollState = verticalScrollState
                                ) { // on fetching response action done
                                    showDialog.value = false

                                    if (!HelperAuth.isSubscribed()) { // if the user is not yet subscribed, decrement the nb of generations left
                                        SettingsNotifier.nbOfGenerationsConsumed.value += 1
                                    }

                                    Timer().schedule(timerTask {
                                        generateText.value =
                                            App.getTextFromString(R.string.generate)
                                    }, 1500)
                                }
                            }
                        },
                    textAlign = TextAlign.Center,
                    text = generateText.value,
                    color = Color.White
                )
                if (HelperSharedPreference.getIsSavedOutputsEnabled()) {
                    MyText(text = "|", color = Color.White)
                    MyText(
                        modifier = Modifier
                            .padding(horizontal = SpacersSize.small)
                            .clickable {
                                coroutineScope.launch {
                                    SettingsNotifier.isPricingBottomSheets.value = false
                                    SettingsNotifier.sheetScaffoldState?.bottomSheetState?.expand()
                                }
                            },
                        textAlign = TextAlign.Center,
                        text = stringResource(id = R.string.save_outputs),
                        color = Color.White
                    )
                }
            }
        }
    }
}