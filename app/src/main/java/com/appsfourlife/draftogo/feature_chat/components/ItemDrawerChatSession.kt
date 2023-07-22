package com.appsfourlife.draftogo.feature_chat.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.MyCardView
import com.appsfourlife.draftogo.components.MyIcon
import com.appsfourlife.draftogo.components.MyText
import com.appsfourlife.draftogo.data.model.ModelNewChat
import com.appsfourlife.draftogo.helpers.HelperUI
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ItemDrawerChatSession(
    modifier: Modifier = Modifier,
    modelNewChat: ModelNewChat,
    listOfNewChats: MutableState<MutableList<ModelNewChat>>,
    onDeleteBtnClick: (ModelNewChat) -> Unit,
    onClick: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    MyCardView(modifier = modifier
        .fillMaxWidth(0.9f)
        .clickable {
            onClick(modelNewChat.text)
        }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyText(
                text = modelNewChat.text,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(SpacersSize.medium),
                enableSingleLine = true
            )
            if (modelNewChat.text != App.getTextFromString(textID = R.string.chat))
                IconButton(onClick = {
                    if (modelNewChat.text == App.getTextFromString(R.string.chat)) {
                        HelperUI.showToast(msg = App.context.getString(R.string.this_item_cant_be_deleted))
                    } else
                        coroutineScope.launch(Dispatchers.IO) {
                            App.databaseApp.daoApp.deleteAllChatsByNewChatID(modelNewChat.id)
                            App.databaseApp.daoApp.deleteNewChat(modelNewChat)
                            listOfNewChats.value =
                                App.databaseApp.daoApp.getAllNewChats() as MutableList<ModelNewChat>
                        }

                    onDeleteBtnClick(modelNewChat)
                }) {
                    MyIcon(iconID = R.drawable.icon_delete, contentDesc = "delete item")
                }
        }
    }
}