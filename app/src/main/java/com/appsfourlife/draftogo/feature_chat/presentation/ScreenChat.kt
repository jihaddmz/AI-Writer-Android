package com.appsfourlife.draftogo.feature_chat.presentation

import android.view.WindowManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.BuildConfig
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.data.model.ModelChatResponse
import com.appsfourlife.draftogo.data.model.ModelNewChat
import com.appsfourlife.draftogo.feature_chat.components.SubmitChatQuery
import com.appsfourlife.draftogo.feature_chat.components.TextChatResponse
import com.appsfourlife.draftogo.feature_chat.components.listOfNewChats
import com.appsfourlife.draftogo.helpers.HelperFirebaseDatabase
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.helpers.HelperUI
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.BottomNavScreens
import com.appsfourlife.draftogo.util.SettingsNotifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.timerTask

val queryChat =
    mutableStateOf("")

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScreenChat(
    navHostController: NavHostController,
    scaffoldState: ScaffoldState,
    title: String? = null,
    newChatID: Int = 0
) {

    LaunchedEffect(key1 = true, block = {
        queryChat.value = ""
    })

    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val state = rememberLazyListState()
    val listOfChats = remember {
        mutableStateOf(mutableListOf<ModelChatResponse>())
    }
    val isAppOutDated = remember {
        mutableStateOf(false)
    }

    val timer = remember {
        mutableStateOf(0)
    }
    val showDialogIntroducingChanges = remember {
        mutableStateOf(false)
    }
    val showDialogAccessibilityPermission = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true, block = {
        coroutineScope.launch(Dispatchers.IO) {
            if (SettingsNotifier.isConnected.value)
                HelperFirebaseDatabase.fetchAppVersion {
                    isAppOutDated.value = it != BuildConfig.VERSION_NAME
                }
        }
        Timer().scheduleAtFixedRate(timerTask {
            if (timer.value == 2) {
                showDialogIntroducingChanges.value = HelperSharedPreference.getBool(
                    HelperSharedPreference.SP_SETTINGS,
                    HelperSharedPreference.SP_SETTINGS_IS_FIRST_TIME_V30_LAUNCHED,
                    true
                ) && !HelperSharedPreference.getBool(
                    HelperSharedPreference.SP_SETTINGS,
                    HelperSharedPreference.SP_SETTINGS_IS_FIRST_TIME_V230_LAUNCHED,
                    true
                )
                showDialogAccessibilityPermission.value = !showDialogIntroducingChanges.value && !HelperSharedPreference.getDontShowAnyWhereWritingPermission()
                cancel()
            }
            timer.value += 1
        }, 1000, 1000)
    })

    val showAccessibilityOnClick = remember {
        mutableStateOf(false)
    }

    if (showDialogIntroducingChanges.value) {

        MyDialog(
            showDialog = showDialogIntroducingChanges,
            text = stringResource(id = R.string.v30_updates_explanation),
            showOkBtn = true,
            title = stringResource(id = R.string.v30_updates),
            onOkBtnClick = {
                showAccessibilityOnClick.value = true
            })

        HelperSharedPreference.setBool(
            HelperSharedPreference.SP_SETTINGS,
            HelperSharedPreference.SP_SETTINGS_IS_FIRST_TIME_V30_LAUNCHED,
            false
        )
    }

    if (showDialogAccessibilityPermission.value || showAccessibilityOnClick.value)
        HelperUI.ShowAccessibilityPermissionRequester(true)


    App.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    LaunchedEffect(key1 = true, block = {
        listOfChats.value =
            App.databaseApp.daoApp.getAllChatsNyNewChatID(newChatID = newChatID) as MutableList<ModelChatResponse>
    })

    BottomSheetWriting(
        modifier = Modifier
            .fillMaxSize(),
        navController = navHostController
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            if (isAppOutDated.value) // if the app is outdated show the alert dialog to update
                MyDialog(
                    modifier = Modifier,
                    showDialog = isAppOutDated,
                    text = stringResource(id = R.string.app_is_outdated),
                    title = stringResource(id = R.string.attention),
                    properties = DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true,
                    )
                )

            val title1 = remember {
                if (title == "Unnamed") {
                    mutableStateOf(App.getTextFromString(R.string.new_chat))
                } else if (newChatID == 0) {
                    mutableStateOf(App.getTextFromString(R.string.chat))
                } else
                    mutableStateOf(title!!)
//                } else
//                    mutableStateOf(App.getTextFromString(R.string.chat))

            }

            AppBarTransparent(
                title = title1.value,
                showSidebar = true,
                showHelpIcon = true,
                scaffoldState = scaffoldState,
            ) {
                App.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                SettingsNotifier.navHostController?.navigate(BottomNavScreens.Dashboard.route)
            }

            if (listOfChats.value.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f), contentAlignment = Alignment.Center
                ) {
                    MyText(
                        textAlign = TextAlign.Center,
                        text = stringResource(id = R.string.no_chats_found),
                        color = Color.LightGray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f),
                    state = state
                ) {


                    items(listOfChats.value.size, key = { listOfChats.value[it].id }) { index ->
                        val modelChatResponse = listOfChats.value[index]
                        TextChatResponse(modelChatResponse = modelChatResponse, onClick = {
                            if (modelChatResponse.role == "user") {
                                queryChat.value = modelChatResponse.text
                            }
                        })
                        MySpacer(type = "medium")
                    }
                }
            }

            SubmitChatQuery(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = SpacersSize.small),
                newChatID = newChatID,
                onResponseDone = {
                    coroutineScope.launch(Dispatchers.IO) {
                        App.databaseApp.daoApp.insertChat(
                            ModelChatResponse(
                                App.databaseApp.daoApp.getChatMaxID() + 1,
                                role = "system",
                                text = it,
                                color = 0,
                                newChatID = newChatID
                            )
                        )
                        listOfChats.value =
                            App.databaseApp.daoApp.getAllChatsNyNewChatID(newChatID) as MutableList<ModelChatResponse>
                        state.animateScrollToItem(100)
                    }
                },
                onSubmitClick = { boolean, string ->
                    coroutineScope.launch(Dispatchers.IO) {

                        if (boolean) {
                            if (newChatID != 0) { // if the user is coming from added new chat
                                val newChat = App.databaseApp.daoApp.getNewChatByID(newChatID)
                                if (newChat == null) {
                                    title1.value = string
                                    App.databaseApp.daoApp.insertNewChat(
                                        ModelNewChat(
                                            newChatID,
                                            text = string
                                        )
                                    )
                                    listOfNewChats.value =
                                        App.databaseApp.daoApp.getAllNewChats() as MutableList<ModelNewChat>
                                }
                            }
                        }

                        listOfChats.value =
                            App.databaseApp.daoApp.getAllChatsNyNewChatID(newChatID) as MutableList<ModelChatResponse>
                        keyboardController?.hide()
                        state.animateScrollToItem(100)

                    }
                },
                onResponseError = {
                    coroutineScope.launch(Dispatchers.IO) {
                        App.databaseApp.daoApp.deleteChatByText("...")

                        listOfChats.value =
                            App.databaseApp.daoApp.getAllChatsNyNewChatID(newChatID) as MutableList<ModelChatResponse>
                    }
                },
                onClearClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        listOfChats.value =
                            App.databaseApp.daoApp.getAllChatsNyNewChatID(newChatID) as MutableList<ModelChatResponse>
                    }
                })
        }
    }
}