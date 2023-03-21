package com.appsfourlife.draftogo.components

import android.speech.tts.TextToSpeech
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.helpers.HelperUI
import com.appsfourlife.draftogo.helpers.Helpers
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import java.util.*

@Composable
fun Output(
    modifier: Modifier = Modifier,
    emailName: String = "",
    outputText: MutableState<String>,
    fromScreen: String = ""
) {

    val context = LocalContext.current

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

            if (fromScreen != "instagram" && fromScreen != "youtube")
                MyAnimatedVisibility(visible = outputText.value.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(SpacersSize.small),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        IconButton(modifier = Modifier, onClick = {
                            if (fromScreen.lowercase() == "email") {
                                Helpers.shareEmailOutput(outputText.value, emailName, context)
                            } else if (fromScreen.lowercase() == "facebook") {
                                Helpers.copyToClipBoard(
                                    text = outputText.value,
                                    msgID = com.appsfourlife.draftogo.R.string.text_copied_facebook
                                )
                                Helpers.shareOutputToFacebook(context, "Hello there")
                            } else if (fromScreen.lowercase() == "twitter") {
                                Helpers.shareOutputToTwitter(context, outputText.value)
                            } else if (fromScreen.lowercase() == "linkedin") {
                                Helpers.shareOutputToLinkedIn(context, outputText.value)
                            } else
                                Helpers.shareOutput(text = outputText.value, context)
                        }) {
                            MyIcon(
                                iconID = R.drawable.icon_alternate_share,
                                contentDesc = stringResource(
                                    id = com.appsfourlife.draftogo.R.string.share
                                ),
                                tint = Blue
                            )
                        }

                        MySpacer(type = "small", widthOrHeight = "width")

                        IconButton(onClick = {
                            if (HelperSharedPreference.getSubscriptionType() != Constants.SUBSCRIPTION_TYPE_PLUS) {
                                HelperUI.showToast(msg = App.getTextFromString(R.string.plus_feature))
                                return@IconButton
                            }

                            var tts: TextToSpeech? = null
                            tts = TextToSpeech(
                                context
                            ) { status ->
                                if (status == TextToSpeech.SUCCESS) {
                                    val languageCode =
                                        when (HelperSharedPreference.getOutputLanguage()) {
                                            App.getTextFromString(R.string.english) -> "en"
                                            App.getTextFromString(R.string.french) -> "fr"
                                            App.getTextFromString(R.string.arabic) -> "ar"
                                            App.getTextFromString(R.string.german) -> "de"
                                            App.getTextFromString(R.string.hindi) -> "hi"
                                            App.getTextFromString(R.string.italian) -> "it"
                                            App.getTextFromString(R.string.purtaguese) -> "pt"
                                            App.getTextFromString(R.string.russian) -> "ru"
                                            App.getTextFromString(R.string.turkish) -> "tr"
                                            App.getTextFromString(R.string.swedish) -> "se"
                                            else -> "nl"
                                        }
                                    val result =
                                        tts?.setLanguage(Locale.forLanguageTag(languageCode))

                                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

                                    } else {
                                        tts!!.speak(
                                            outputText.value,
                                            TextToSpeech.QUEUE_FLUSH,
                                            null,
                                            ""
                                        )
                                    }
                                }
                            }
                        }) {
                            MyIcon(
                                iconID = com.appsfourlife.draftogo.R.drawable.icon_speaker,
                                contentDesc = stringResource(
                                    id = com.appsfourlife.draftogo.R.string.speaker
                                ),
                                tint = Blue
                            )
                        }

                        IconButton(onClick = {
                            Helpers.copyToClipBoard(
                                context = context,
                                text = outputText.value,
                                msgID = com.appsfourlife.draftogo.R.string.text_copied
                            )
                        }) {
                            MyIcon(
                                iconID = com.appsfourlife.draftogo.R.drawable.copy,
                                contentDesc = stringResource(
                                    id = com.appsfourlife.draftogo.R.string.copy
                                ),
                                tint = Blue
                            )
                        }
                    }
                }
        }
    }
}