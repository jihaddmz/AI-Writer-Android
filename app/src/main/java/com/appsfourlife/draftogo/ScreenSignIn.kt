package com.appsfourlife.draftogo

import android.app.Activity
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.helpers.HelperAuth
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
            title = stringResource(id = R.string.sign_in),
            endY = 800f
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
                        text = "${stringResource(id = R.string.ai_writing_tool)}:",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                    )
                    MySpacer(type = "small")
                    val listOfWritingFeatures = listOf(
                        stringResource(id = R.string.access_to_all_templates),
                        stringResource(id = R.string.generate_content_in_different_languages),
                        stringResource(id = R.string.create_custom_templates),
                        stringResource(id = R.string.read_output_outload),
                        stringResource(id = R.string.share_content_directly),
                        stringResource(id = R.string.save_outputs_for_comparison),
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
                        stringResource(id = R.string.arts_per_credits),
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