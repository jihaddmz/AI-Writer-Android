package com.appsfourlife.draftogo.feature_chat.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.MyText
import com.appsfourlife.draftogo.data.model.ModelChatResponse
import com.appsfourlife.draftogo.helpers.Helpers
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TextChatResponse(modelChatResponse: ModelChatResponse) {

    val textColor = if (modelChatResponse.color == 1) {
        Color.White
    } else {
        Color.Black
    }

    val bgC = if (modelChatResponse.color == 1) {
        Blue
    } else {
        Color.LightGray
    }

    if (modelChatResponse.role == "user") {
        MyText(
            text = modelChatResponse.text,
            color = textColor,
            modifier = Modifier
                .padding(horizontal = 50.dp)
                .background(color = bgC, shape = Shapes.medium)
                .combinedClickable(enabled = true, onLongClick = {
                    Helpers.copyToClipBoard(modelChatResponse.text, msgID = R.string.text_copied)
                }, onClick = {

                })
                .padding(10.dp)
                .fillMaxWidth()
        )
    } else {
        MyText(
            text = modelChatResponse.text,
            color = textColor,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .background(color = bgC, shape = Shapes.medium)
                .combinedClickable(enabled = true, onLongClick = {
                    Helpers.copyToClipBoard(modelChatResponse.text, msgID = R.string.text_copied)
                }, onClick = {

                })
                .padding(10.dp)
                .fillMaxWidth()
        )
    }
}