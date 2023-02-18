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
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.SettingsNotifier
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun ScreenCV(
    navController: NavController
) {

    val verticalScroll = rememberScrollState()
    val showDialog = remember {
        mutableStateOf(false)
    }

    TopBar(
        text = stringResource(id = R.string.write_a_cv), navController = navController
    ) {
        if (showDialog.value) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpacersSize.medium)
                .verticalScroll(verticalScroll)
        ) {


            val cvType =
                myDropDown(label = stringResource(id = R.string.type), list = App.listOfCVTypes)

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            myEditTextLabel(
                label = stringResource(id = R.string.job_title), placeHolder = stringResource(
                    id = R.string.web_developer
                )
            )

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            input(
                label = stringResource(id = R.string.cv_input_label),
                inputPrefix = "${
                    stringResource(
                        id = R.string.write_a_cv_of_type,
                        HelperSharedPreference.getOutputLanguage(),
                        cvType
                    )
                } for a ${SettingsNotifier.jobTitle.value} ",
                showDialog = showDialog,
                verticalScrollState = verticalScroll
            )

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            Output(outputText = SettingsNotifier.output)

        }
    }
}