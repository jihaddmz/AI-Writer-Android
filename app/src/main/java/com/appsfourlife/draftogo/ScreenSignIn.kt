package com.appsfourlife.draftogo

import android.app.Activity
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.helpers.HelperAuth
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.SettingsNotifier
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenSignIn(
) {
    val currentActivity = LocalContext.current as Activity
    val coroutineScope = rememberCoroutineScope()

    SettingsNotifier.disableDrawerContent.value = true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = SpacersSize.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        TopImageHeader(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f),
            sheetScaffoldState = null,
            showQuitBtn = false,
            drawableID = R.drawable.login,
            title = "",
        )

        val textDraftogo = remember {
            mutableStateOf("")
        }
        val finishedTextDraftogoAnimation = remember {
            mutableStateOf(false)
        }
        TypeWriterEffect(
            baseText = "${stringResource(id = R.string.app_name)}: ${stringResource(id = R.string.aichat_and_image)}",
            outputText = textDraftogo,
            finished = finishedTextDraftogoAnimation,
            delay = 1000
        )
        if (textDraftogo.value.isNotEmpty())
            Text(
                text = AnnotatedString(
                    textDraftogo.value[0].toString(),
                    spanStyle = SpanStyle(
                        color = Blue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp
                    )
                ).plus(
                    AnnotatedString(
                        text = textDraftogo.value.substring(1),
                        spanStyle = SpanStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Normal,
                            fontSize = 30.sp
                        )
                    )
                ), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
            )

        MySpacer(type = "medium")

        MyTipText(
            text = stringResource(id = R.string.welcome_text),
            modifier = Modifier.padding(horizontal = SpacersSize.medium)
        )

        MySpacer(type = "large")
        MySpacer(type = "large")

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f), contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = SpacersSize.medium),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Column {

                    MyTextTitle(
                        text = "${stringResource(id = R.string.ai_chat_tool)}:",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                    )
                    MySpacer(type = "small")
                    val listOfWritingFeatures = listOf(
                        stringResource(id = R.string.chat_about_anyhing),
                        stringResource(id = R.string.save_your_chat_session),
                        stringResource(id = R.string.generate_in_any_language_you_want),
                        stringResource(id = R.string.write_anywhere),
                    )
                    val state = rememberScrollState()
                    LaunchedEffect(key1 = Unit, block = {
                        while (true) {
                            if (state.value == state.maxValue)
                                state.animateScrollTo(0)
                            else
                                state.animateScrollTo(state.value + 50)
                        }
                    })
                    Row(modifier = Modifier.horizontalScroll(state)) {
                        listOfWritingFeatures.forEach {
                            Row {
                                MyIcon(iconID = R.drawable.icon_checkcircle, contentDesc = "")
                                MySpacer(type = "small", widthOrHeight = "width")
                                MyText(text = it)
                            }
                            MySpacer(type = "medium", widthOrHeight = "width")
                        }
                    }
                }

                Column {

                    val state = rememberScrollState()

                    MyTextTitle(
                        text = "${stringResource(id = R.string.ai_art_tool)}:",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                    )
                    MySpacer(type = "small")
                    val listOfArtFeatures = listOf(
                        stringResource(id = R.string.create_any_image_you_can_imagine),
                        stringResource(id = R.string.download_high_resolution_artwork),
                        stringResource(id = R.string.access_to_all_styles),
                        stringResource(id = R.string.use_some_inspiration),
                    )

                    LaunchedEffect(key1 = Unit, block = {
                        while (true) {
                            if (state.value == state.maxValue)
                                state.animateScrollTo(0)
                            else
                                state.animateScrollTo(state.value + 50)
                        }
                    })
                    Row(modifier = Modifier.horizontalScroll(state)) {
                        listOfArtFeatures.forEach {
                            Row {
                                MyIcon(iconID = R.drawable.icon_checkcircle, contentDesc = "")
                                MySpacer(type = "small", widthOrHeight = "width")
                                MyText(text = it)
                            }
                            MySpacer(type = "medium", widthOrHeight = "width")
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f), contentAlignment = Alignment.BottomCenter
        ) {
            MyIconTextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SpacersSize.large),
                text = stringResource(id = R.string.sign_in_with_google),
                iconID = R.drawable.icon_google
            ) {
                HelperAuth.signIn(currentActivity)
            }
        }
    }
}