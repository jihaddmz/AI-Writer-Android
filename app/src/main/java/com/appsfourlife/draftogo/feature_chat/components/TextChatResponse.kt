package com.appsfourlife.draftogo.feature_chat.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.MyImage
import com.appsfourlife.draftogo.components.MySpacer
import com.appsfourlife.draftogo.components.MyText
import com.appsfourlife.draftogo.components.MyUrlImage
import com.appsfourlife.draftogo.data.model.ModelChatResponse
import com.appsfourlife.draftogo.helpers.HelperAuth
import com.appsfourlife.draftogo.helpers.Helpers
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TextChatResponse(
    modelChatResponse: ModelChatResponse,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacersSize.small),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MyText(
                    text = modelChatResponse.text,
                    color = textColor,
                    modifier = modifier
                        .background(color = bgC, shape = Shapes.medium)
                        .combinedClickable(enabled = true, onLongClick = {
                            Helpers.copyToClipBoard(
                                modelChatResponse.text,
                                msgID = R.string.text_copied
                            )
                        }, onClick = {
                            onClick()
                        })
                        .padding(10.dp)
                        .fillMaxWidth(0.8f)
                )
            }

            MySpacer(type = "medium", widthOrHeight = "width")

            MyUrlImage(
                modifier = Modifier
                    .padding(top = SpacersSize.small)
                    .clip(CircleShape),
                imageUrl = if (HelperAuth.auth.currentUser?.photoUrl == null) "https://firebasestorage.googleapis.com/v0/b/ai-writer-9832b.appspot.com/o/profile.png?alt=media&token=ca0b14b3-01bf-44b5-97c4-2c69a09cad31" else HelperAuth.auth.currentUser!!.photoUrl.toString(),
                contentDesc = "",
                baseSize = 32.dp,
                contentScale = ContentScale.Fit
            )
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = SpacersSize.small),
        ) {

            MyImage(
                modifier = Modifier
                    .padding(top = SpacersSize.small)
                    .clip(CircleShape),
                imageID = R.drawable.logo,
                contentDesc = "",
                contentScale = ContentScale.Fit,
                baseSize = 32.dp
            )

            MySpacer(type = "medium", widthOrHeight = "width")

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {

                MyText(
                    text = modelChatResponse.text,
                    color = textColor,
                    modifier = Modifier
                        .background(color = bgC, shape = Shapes.medium)
                        .combinedClickable(enabled = true, onLongClick = {
                            Helpers.copyToClipBoard(
                                modelChatResponse.text,
                                msgID = R.string.text_copied
                            )
                        }, onClick = {
                            onClick()
                        })
                        .padding(10.dp)
                        .animateContentSize()
                        .fillMaxWidth()
                )
            }
        }
    }
}