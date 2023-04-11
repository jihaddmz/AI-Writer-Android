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
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.SettingsNotifier

@Composable
fun ScreenResume(
    navController: NavController
) {

    SettingsNotifier.templateType = "Resume"

    val verticalScroll = rememberScrollState()
    val showDialog = remember {
        mutableStateOf(false)
    }

    TopBar(
        text = stringResource(id = R.string.write_a_resume), navController = navController
    ) {
        if (showDialog.value) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

        BottomSheetWriting(navController = navController) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = SpacersSize.medium, end = SpacersSize.medium, bottom = 80.dp)
                    .verticalScroll(verticalScroll)
            ) {

                val resumeType =
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
                            id = R.string.write_a_resume_of_type,
                            resumeType
                        )
                    } for a ${SettingsNotifier.jobTitle.value}",
                    showDialog = showDialog,
                    verticalScrollState = verticalScroll
                )

                Spacer(modifier = Modifier.height(SpacersSize.medium))

                Output(outputText = SettingsNotifier.output)

            }
        }
    }
}