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
import androidx.navigation.NavController
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.util.SettingsNotifier
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun ScreenTiktok(
    navController: NavController
) {

    SettingsNotifier.templateType = "Tiktok"

    val verticalScroll = rememberScrollState()
    val showDialog = remember {
        mutableStateOf(false)
    }

    TopBar(
        text = stringResource(id = R.string.write_a_viral_tiktok_captions_top_bar),
        navController = navController
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpacersSize.medium)
                .verticalScroll(verticalScroll)
        ) {

            if (showDialog.value) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

            val nbOfGenerations = sliderNbOfGenerations()

            MySpacer(type = "small")

            input(
                label = stringResource(id = R.string.tiktok_input_label),
                inputPrefix = stringResource(
                    id = R.string.write_a_viral_tiktok_captions,
                    HelperSharedPreference.getOutputLanguage()
                ),
                showDialog = showDialog,
                length = Constants.DEFAULT_POSTING_GENERATION_LENGTH.toInt(),
                nbOfGenerations = nbOfGenerations,
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