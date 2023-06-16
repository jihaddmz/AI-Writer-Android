package com.appsfourlife.draftogo.feature_chat.presentation

import android.view.WindowManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.AppBarTransparent
import com.appsfourlife.draftogo.components.BottomSheetWriting
import com.appsfourlife.draftogo.components.MySpacer
import com.appsfourlife.draftogo.components.MyText
import com.appsfourlife.draftogo.data.model.ModelChatResponse
import com.appsfourlife.draftogo.feature_chat.components.SubmitChatQuery
import com.appsfourlife.draftogo.feature_chat.components.TextChatResponse
import com.appsfourlife.draftogo.helpers.HelperAnalytics
import com.appsfourlife.draftogo.util.BottomNavScreens
import com.appsfourlife.draftogo.util.SettingsNotifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScreenChat(navHostController: NavHostController) {
    HelperAnalytics.sendEvent("chat")

    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val state = rememberLazyListState()
    val listOfChats = remember {
        mutableStateOf(mutableListOf<ModelChatResponse>())
    }

    App.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    LaunchedEffect(key1 = true, block = {
        listOfChats.value = App.databaseApp.daoApp.getAllChats() as MutableList<ModelChatResponse>
    })

    BottomSheetWriting(
        modifier = Modifier
            .fillMaxSize(),
        navController = navHostController
    ) {

        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {

            AppBarTransparent(title = stringResource(id = R.string.chat)) {
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
                        TextChatResponse(modelChatResponse = modelChatResponse)
                        MySpacer(type = "medium")
                    }
                }
            }

            SubmitChatQuery(modifier = Modifier.fillMaxWidth(), onResponseDone = {
                coroutineScope.launch(Dispatchers.IO) {
                    App.databaseApp.daoApp.insertChat(
                        ModelChatResponse(
                            role = "system",
                            text = it,
                            color = 0
                        )
                    )
                    listOfChats.value =
                        App.databaseApp.daoApp.getAllChats() as MutableList<ModelChatResponse>
                    state.animateScrollToItem(100)
                }
            }, onSubmitClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    listOfChats.value =
                        App.databaseApp.daoApp.getAllChats() as MutableList<ModelChatResponse>
                    keyboardController?.hide()
                    state.animateScrollToItem(100)
                }
            }, onResponseError = {
            }, onClearClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    listOfChats.value =
                        App.databaseApp.daoApp.getAllChats() as MutableList<ModelChatResponse>
                }
            })
        }
    }
}