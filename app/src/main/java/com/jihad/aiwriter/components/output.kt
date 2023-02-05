package com.jihad.aiwriter.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jihad.aiwriter.App
import com.jihad.aiwriter.R
import com.jihad.aiwriter.helpers.*
import com.jihad.aiwriter.ui.theme.Blue
import com.jihad.aiwriter.ui.theme.Glass
import com.jihad.aiwriter.ui.theme.Shapes
import com.jihad.aiwriter.ui.theme.SpacersSize

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

            if (showDialog.value) Dialog(onDismissRequest = {
                showDialog.value = false
            }) {

                Column(
                    modifier = Modifier
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            layout(constraints.maxWidth, constraints.constrainHeight(constraints.maxHeight - 500), placementBlock = {
                                placeable.place(0, 0)
                            })
                        }
                        .fillMaxWidth()
                        .background(color = Glass, shape = Shapes.medium)
                        .verticalScroll(verticalScroll)
                        .padding(SpacersSize.medium),
                ) {

                    if (showLinearProgressIndicator.value) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(SpacersSize.small))
                    }

                    val language = myDropDown(modifier = Modifier.fillMaxWidth(),
                        label = stringResource(id = R.string.language),
                        list = App.listOfLanguages,
                        onItemSelect = {
                            showLinearProgressIndicator.value = true
                        })

                    Spacer(modifier = Modifier.height(SpacersSize.small))

                    AnimatedVisibility(visible = translatedText.value.isNotEmpty()) {
                        MyText(text = translatedText.value, modifier = Modifier.clickable {
                            HelperUI.showToast(
                                context, App.getTextFromString(R.string.text_copied)
                            )
                            Helpers.copyToClipBoard(translatedText.value, context)
                        })
                    }

                    HelperTranslate.identifyText(outputText.value) { initialLanguage ->
                        HelperTranslate.downloadLanguageModel(language.lowercase(),
                            onSuccessListener = {
                                HelperTranslate.translating(
                                    initialLanguage, outputText.value, language.lowercase()
                                ) { textTranslate ->
                                    showLinearProgressIndicator.value = false
                                    translatedText.value = textTranslate
                                }
                            })
                    }
                }
            }

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
                        iconID = R.drawable.icon_alternate_share, contentDesc = stringResource(
                            id = R.string.share
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

                IconButton(onClick = {
                    if (outputText.value.isNotEmpty())
                        showDialog.value = true
                    else
                        HelperUI.showToast(context, App.getTextFromString(R.string.no_text_to_translate))
                }) {
                    MyIcon(
                        iconID = R.drawable.icon_translate,
                        contentDesc = stringResource(id = R.string.translate_text)
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

                IconButton(onClick = {
                    Helpers.copyToClipBoard(context = context, text = outputText.value)
                    HelperUI.showToast(context, App.getTextFromString(R.string.text_copied))
                }) {
                    MyIcon(
                        iconID = R.drawable.copy, contentDesc = stringResource(
                            id = R.string.copy
                        ), tint = Blue
                    )
                }
            }
        }
    }
}