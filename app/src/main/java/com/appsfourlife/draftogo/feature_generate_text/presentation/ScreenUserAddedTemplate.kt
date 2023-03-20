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
import com.appsfourlife.draftogo.components.MySpacer
import com.appsfourlife.draftogo.components.Output
import com.appsfourlife.draftogo.components.TopBar
import com.appsfourlife.draftogo.components.input
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.SettingsNotifier

@Composable
fun ScreenUserAddedTemplate(
    navController: NavController
) {

    SettingsNotifier.templateType = SettingsNotifier.currentQuerySection!!

    val verticalScroll = rememberScrollState()
    val showDialog = remember {
        mutableStateOf(false)
    }

    TopBar(
        text = SettingsNotifier.currentQuerySection!!, navController = navController
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpacersSize.medium)
                .verticalScroll(verticalScroll)
        ) {

            if (showDialog.value) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

            MySpacer(type = "small")

            input(
                label = stringResource(id = R.string.about),
                inputPrefix = "${SettingsNotifier.currentQuerySection!!} in ${HelperSharedPreference.getOutputLanguage()} language",
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