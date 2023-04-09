package com.appsfourlife.draftogo.feature_generate_text.presentation

import androidx.compose.animation.AnimatedVisibility
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
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.SettingsNotifier

@Composable
fun ScreenLetter(
    navController: NavController
) {

    SettingsNotifier.templateType = "Letter"

    val verticalScroll = rememberScrollState()
    val showDialog = remember {
        mutableStateOf(false)
    }

    TopBar(
        text = stringResource(id = R.string.write_a_letter), navController = navController
    ) {

        if (showDialog.value) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

        BottomSheetSaveOutputs(navController = navController) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = SpacersSize.medium, end = SpacersSize.medium, bottom = 80.dp)
                    .verticalScroll(verticalScroll)
            ) {

                val type =
                    myDropDown(
                        label = stringResource(id = R.string.type),
                        list = App.listOfLetterTypes
                    )

                Spacer(modifier = Modifier.height(SpacersSize.medium))

                val makeJobTitleVisible =
                    type == stringResource(id = R.string.cover_letter) || type == stringResource(id = R.string.resignation_letter) || type == stringResource(
                        id = R.string.reference_letter
                    )
                AnimatedVisibility(visible = makeJobTitleVisible) {
                    myEditTextLabel(
                        label = stringResource(id = R.string.job_title),
                        placeHolder = stringResource(
                            id = R.string.web_developer
                        )
                    )
                }

                Spacer(modifier = Modifier.height(SpacersSize.medium))

                myEditTextLabel()

                Spacer(modifier = Modifier.height(SpacersSize.medium))

                val length = length()

                Spacer(modifier = Modifier.height(SpacersSize.medium))

                val inputPrefix = if (SettingsNotifier.jobTitle.value.isNotEmpty()) {
                    "${
                        stringResource(
                            id = R.string.write_a_letter_of_type,
                            type
                        )
                    } for a job position of $SettingsNotifier.jobTitle.value to ${SettingsNotifier.name.value} "
                } else {
                    "${
                        stringResource(
                            id = R.string.write_a_letter_of_type,
                            HelperSharedPreference.getOutputLanguage(),
                            type
                        )
                    } to ${SettingsNotifier.name.value} "
                }

                input(
                    label = stringResource(id = R.string.letter_input_label),
                    inputPrefix = inputPrefix,
                    length = length,
                    showDialog = showDialog,
                    verticalScrollState = verticalScroll
                )

                Spacer(modifier = Modifier.height(SpacersSize.medium))

                Output(outputText = SettingsNotifier.output)

            }
        }
    }
}