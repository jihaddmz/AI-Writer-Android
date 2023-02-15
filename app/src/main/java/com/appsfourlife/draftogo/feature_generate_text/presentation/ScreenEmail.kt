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
import com.appsfourlife.draftogo.SettingsNotifier
import com.appsfourlife.draftogo.components.Output
import com.appsfourlife.draftogo.components.TopBar
import com.appsfourlife.draftogo.components.input
import com.appsfourlife.draftogo.components.myEditTextLabel
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun ScreenEmail(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val verticalScroll = rememberScrollState()
    val showDialog = remember {
        mutableStateOf(false)
    }

    TopBar(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(verticalScroll),
        text = stringResource(id = R.string.write_an_email),
        navController = navController
    ) {

        if (showDialog.value) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpacersSize.medium)
        ) {

            myEditTextLabel(placeHolder = stringResource(id = R.string.placeholder_email_name))

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            input(
                label = stringResource(id = R.string.email_input_label),
                inputPrefix = stringResource(
                    id = R.string.write_an_email_to, HelperSharedPreference.getOutputLanguage(), SettingsNotifier.name.value
                ),
                showDialog = showDialog
            )

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            Output(
                outputText = SettingsNotifier.output,
                emailName = SettingsNotifier.name.value,
                fromScreen = "email"
            )
        }
    }
}