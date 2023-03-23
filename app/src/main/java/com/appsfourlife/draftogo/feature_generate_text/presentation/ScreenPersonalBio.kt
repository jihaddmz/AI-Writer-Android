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
import com.appsfourlife.draftogo.components.Output
import com.appsfourlife.draftogo.components.TopBar
import com.appsfourlife.draftogo.components.input
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun ScreenPersonalBio(
    navController: NavController
) {

    SettingsNotifier.templateType = "PersonalBio"

    val verticalScroll = rememberScrollState()
    val showDialog = remember {
        mutableStateOf(false)
    }

    TopBar(
        text = stringResource(id = R.string.write_a_personal_bio_top_bar), navController = navController
    ) {

        if (showDialog.value) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpacersSize.medium)
                .verticalScroll(verticalScroll)
        ) {

            val inputPrefix = stringResource(id = R.string.write_a_personal_bio, HelperSharedPreference.getOutputLanguage())

            input(
                label = stringResource(id = R.string.that_captures_attention),
                inputPrefix = inputPrefix,
                showDialog = showDialog,
                length = Constants.DEFAULT_POSTING_GENERATION_LENGTH.toInt(),
                verticalScrollState = verticalScroll
            )

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            Output(outputText = SettingsNotifier.output)

        }
    }
}