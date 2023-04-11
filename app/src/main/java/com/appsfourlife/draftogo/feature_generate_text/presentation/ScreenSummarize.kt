package com.appsfourlife.draftogo.feature_generate_text.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.SettingsNotifier

@Composable
fun ScreenSummarize(
    navController: NavController
) {

    SettingsNotifier.templateType = "Summarize"

    val verticalScroll = rememberScrollState()
    val showDialog = remember {
        mutableStateOf(false)
    }

    TopBar(
        text = stringResource(id = R.string.summarize_the_following_text), navController = navController
    ) {

        BottomSheet(navController = navController) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = SpacersSize.medium, end = SpacersSize.medium, bottom = 80.dp)
                    .verticalScroll(verticalScroll)
            ) {

                if (showDialog.value) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

                MySpacer(type = "small")

                input(
                    label = stringResource(id = R.string.text_to_summarize),
                    inputPrefix = stringResource(id = R.string.summarize_the_following_text),
                    showDialog = showDialog,
                    verticalScrollState = verticalScroll
                )

                Spacer(modifier = Modifier.height(SpacersSize.medium))

                if (SettingsNotifier.outputList.isEmpty()) {
                    Output(outputText = SettingsNotifier.output)
                } else if (SettingsNotifier.outputList.isNotEmpty()) {
                    SettingsNotifier.outputList.forEach {
                        Output(outputText = mutableStateOf(it))
                        MySpacer(type = "small")
                    }
                }
            }
        }
    }
}