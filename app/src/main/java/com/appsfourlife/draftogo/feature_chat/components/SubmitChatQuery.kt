package com.appsfourlife.draftogo.feature_chat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.android.volley.VolleyError
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.data.model.ModelChatResponse
import com.appsfourlife.draftogo.feature_chat.presentation.queryChat
import com.appsfourlife.draftogo.feature_generate_text.components.DialogPricingType
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.util.SettingsNotifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubmitChatQuery(
    modifier: Modifier = Modifier,
    newChatID: Int = 0,
    onSubmitClick: (Boolean, String) -> Unit,
    onClearClick: () -> Unit,
    onResponseDone: (String) -> Unit,
    onResponseError: (VolleyError) -> Unit
) {

    val content = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val showDialogClearConfirmation = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current


    val connectionError = stringResource(id = R.string.no_connection)
    val showPricingDialogTypes = remember {
        mutableStateOf(false)
    }
    val showPricingDialogTypesTitle = remember {
        mutableStateOf("")
    }

    var input = remember {
        ""
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (showDialogClearConfirmation.value)
            MyCustomConfirmationDialog(
                showDialog = showDialogClearConfirmation,
                negativeBtnText = stringResource(id = R.string.no),
                positiveBtnText = stringResource(id = R.string.yes),
                onPositiveBtnClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        App.databaseApp.daoApp.deleteAllChatsByNewChatID(newChatID)
                        onClearClick()
                    }
                }) {
                MyText(text = stringResource(id = R.string.text_delete_all_chats_confirmation))
            }

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

        IconButton(onClick = {
            coroutineScope.launch(Dispatchers.IO) {
                if (App.databaseApp.daoApp.getAllChats().isNotEmpty())
                    showDialogClearConfirmation.value = true
            }
        }) {
            MyIcon(iconID = R.drawable.clear, contentDesc = "clear", tint = Color.Red)
        }

        MyCardView(modifier = Modifier.fillMaxWidth(0.8f)) {

            MyTextField(value = queryChat.value, onValueChanged = {
                queryChat.value = it
            }, placeholder = stringResource(id = R.string.type_your_message_here))

        }

        IconButton(modifier = Modifier, onClick = {

            if (queryChat.value.isEmpty()) {
                HelperUI.showToast(msg = App.getTextFromString(R.string.no_input_entered))
                return@IconButton
            }

            if (!SettingsNotifier.isConnected.value) {
                HelperUI.showToast(
                    context,
                    connectionError,
                )
                return@IconButton
            }

            /**
             * if user is on base plan subscription and nb of words generated is at the max, prevent him from
             * generating extra prompts
             **/
            // todo uncomment this when we want to re-enable the paid plans
//            if (HelperAuth.isSubscribed())
//                if (HelperSharedPreference.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_BASE && HelperSharedPreference.getNbOfWordsGenerated() >= Constants.BASE_PLAN_MAX_NB_OF_WORDS) {
//                    showPricingDialogTypes.value = true
//                    showPricingDialogTypesTitle.value =
//                        App.getTextFromString(textID = R.string.you_have_reached_max_nb_of_words_generated)
//                    return@IconButton
//                }
//
//            if (HelperSharedPreference.getNbOfGenerationsConsumed() >= 2 && !HelperAuth.isSubscribed()) { // if nbOfGenerationsConsumed is >= 2
//                // and the user is not subscribed, force the user to subscribe
//                showPricingDialogTypes.value = true
//                showPricingDialogTypesTitle.value = ""
//                return@IconButton
//            }

            HelperChatGPT.getChatResponseForChat(
                query = queryChat.value,
                content,
                coroutineScope = coroutineScope,
                onErrorAction = {
                    onResponseError(it)
                },
                onDoneAction = {
                    coroutineScope.launch(Dispatchers.IO) {
                        App.databaseApp.daoApp.deleteChatByText("...")
                        onResponseDone(it)
                    }
                }
            )
            coroutineScope.launch(Dispatchers.IO) {
                App.databaseApp.daoApp.insertChat(
                    ModelChatResponse(
                        App.databaseApp.daoApp.getChatMaxID() + 1,
                        role = "user",
                        text = queryChat.value,
                        color = 1,
                        newChatID = newChatID
                    )
                )
                input = queryChat.value
                onSubmitClick(true, input)
                queryChat.value = ""

                delay(1000)

                App.databaseApp.daoApp.insertChat(
                    ModelChatResponse(
                        App.databaseApp.daoApp.getChatMaxID() + 1,
                        role = "system",
                        text = "...",
                        color = 0,
                        newChatID = newChatID
                    )
                )
                onSubmitClick(false, input)

            }
        }) {
            MyIcon(iconID = R.drawable.icon_send, contentDesc = "submit", tint = Color.Green)
        }
    }
}