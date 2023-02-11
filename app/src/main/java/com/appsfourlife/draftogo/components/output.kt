package com.appsfourlife.draftogo.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun Output(
    modifier: Modifier = Modifier,
    emailName: String = "",
    outputText: MutableState<String>,
) {

    val context = LocalContext.current
    val showDialog = remember {
        mutableStateOf(false)
    }
    val verticalScroll = rememberScrollState()
    val translatedText = remember {
        mutableStateOf("")
    }
    val showLinearProgressIndicator = remember {
        mutableStateOf(true)
    }

    Card(
        modifier = modifier.fillMaxSize(), shape = Shapes.medium, backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize(animationSpec = tween(durationMillis = 1000))
        ) {

            MyTextField(
                modifier = Modifier.defaultMinSize(minHeight = 250.dp),
                value = outputText.value,
                onValueChanged = {
                    outputText.value = it
                },
                fontWeight = FontWeight.Bold,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpacersSize.small),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {

                IconButton(modifier = Modifier, onClick = {
                    if (emailName.isNotEmpty()) {
                        Helpers.shareEmailOutput(outputText.value, emailName, context)
                    } else Helpers.shareOutput(text = outputText.value, context)
                }) {
                    MyIcon(
                        iconID = com.appsfourlife.draftogo.R.drawable.icon_alternate_share, contentDesc = stringResource(
                            id = com.appsfourlife.draftogo.R.string.share
                        ), tint = Blue
                    )
                }

                when (rememberWindowInfo().screenWidthInfo) {
                    is WindowInfo.WindowType.Compact -> Spacer(modifier = Modifier.width(0.dp))
                    is WindowInfo.WindowType.Medium -> Spacer(
                        modifier = Modifier.width(
                            SpacersSize.small
                        )
                    )
                    else -> Spacer(modifier = Modifier.width(SpacersSize.medium))
                }

                when (rememberWindowInfo().screenWidthInfo) {
                    is WindowInfo.WindowType.Compact -> Spacer(modifier = Modifier.width(0.dp))
                    is WindowInfo.WindowType.Medium -> Spacer(
                        modifier = Modifier.width(
                            SpacersSize.small
                        )
                    )
                    else -> Spacer(modifier = Modifier.width(SpacersSize.medium))
                }

                IconButton(onClick = {
                    Helpers.copyToClipBoard(context = context, text = outputText.value)
                    HelperUI.showToast(context, App.getTextFromString(com.appsfourlife.draftogo.R.string.text_copied))
                }) {
                    MyIcon(
                        iconID = com.appsfourlife.draftogo.R.drawable.copy, contentDesc = stringResource(
                            id = com.appsfourlife.draftogo.R.string.copy
                        ), tint = Blue
                    )
                }
            }
        }
    }
}