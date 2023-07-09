package com.appsfourlife.draftogo.feature_chat.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.MyIcon
import com.appsfourlife.draftogo.components.MyIconTextButton
import com.appsfourlife.draftogo.components.MySpacer
import com.appsfourlife.draftogo.components.MyText
import com.appsfourlife.draftogo.components.MyTextTitle
import com.appsfourlife.draftogo.data.model.ModelNewChat
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.BottomNavScreens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

var listOfNewChats = mutableStateOf(mutableListOf<ModelNewChat>())

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun SidebarNewChat(scaffoldState: ScaffoldState, navController: NavController, onDeleteBtnClick: (ModelNewChat) -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = SpacersSize.small),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyTextTitle(text = stringResource(id = R.string.new_chat), fontWeight = FontWeight.Bold)
            IconButton(onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    val maxNewChatID = App.databaseApp.daoApp.getMaxNewChatID()
                    scaffoldState.drawerState.animateTo(
                        DrawerValue.Closed,
                        tween(durationMillis = 1000)
                    )
                    coroutineScope.launch(Dispatchers.Main) {
                        if (maxNewChatID == null)
                            navController.navigate(BottomNavScreens.Chat.route + "?title=Unnamed,newChatID=${1}")
                        else {
                            navController.navigate(BottomNavScreens.Chat.route + "?title=Unnamed,newChatID=${maxNewChatID + 1}")
                        }
                    }
                }
            }) {
                MyIcon(
                    iconID = R.drawable.icon_add_new,
                    contentDesc = "add new chat",
                    tint = Color.Black
                )
            }
        }

        MySpacer(type = "large")

        /**
         * chat new sessions content
         **/

        LaunchedEffect(key1 = true, block = {
            coroutineScope.launch(Dispatchers.IO) {
                listOfNewChats.value =
                    App.databaseApp.daoApp.getAllNewChats() as MutableList<ModelNewChat>

                if (listOfNewChats.value.size == 0) {
                    App.databaseApp.daoApp.insertNewChat(
                        ModelNewChat(
                            0,
                            App.getTextFromString(R.string.chat)
                        )
                    )
                    listOfNewChats.value =
                        App.databaseApp.daoApp.getAllNewChats() as MutableList<ModelNewChat>

                }
            }
        })

        if (listOfNewChats.value.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f), contentAlignment = Alignment.Center
            ) {
                MyText(
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.add_new_chat_tip),
                    color = Color.LightGray
                )
            }
        } else
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            ) {

                items(listOfNewChats.value.size, key = { listOfNewChats.value[it].id }) { index ->
                    val current = listOfNewChats.value[index]

                    ItemDrawerChatSession(
                        modifier = Modifier.animateItemPlacement(),
                        modelNewChat = current,
                        listOfNewChats = listOfNewChats,
                        onClick = {
                            coroutineScope.launch {
                                scaffoldState.drawerState.animateTo(
                                    DrawerValue.Closed,
                                    tween(durationMillis = 1000)
                                )
                                if (current.text == App.getTextFromString(R.string.chat)) {
                                    navController.navigate(BottomNavScreens.Chat.route + "?title=${current.text},newChatID=${0}")
                                } else
                                    navController.navigate(BottomNavScreens.Chat.route + "?title=${current.text},newChatID=${current.id}")
                            }
                        },
                    onDeleteBtnClick = {
                        onDeleteBtnClick(it)
                    })

                    MySpacer(type = "medium")
                }
            }

        /**
         * collapse btn
         **/
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = SpacersSize.small, end = SpacersSize.small),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            MyIconTextButton(
                modifier = Modifier.fillMaxWidth(0.5f),
                iconID = R.drawable.icon_arrow_back,
                text = stringResource(id = R.string.collapse)
            ) {
                coroutineScope.launch {
                    scaffoldState.drawerState.animateTo(
                        DrawerValue.Closed,
                        tween(durationMillis = 1000)
                    )
                }
            }
        }
    }
}