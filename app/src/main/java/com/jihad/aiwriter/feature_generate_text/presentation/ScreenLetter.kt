package com.jihad.aiwriter.feature_generate_text.presentation

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
import androidx.navigation.NavController
import com.jihad.aiwriter.App
import com.jihad.aiwriter.R
import com.jihad.aiwriter.components.*
import com.jihad.aiwriter.ui.theme.SpacersSize

@Composable
fun ScreenLetter(
    navController: NavController
) {

    val generatedText = remember {
        mutableStateOf("")
    }
    val verticalScroll = rememberScrollState()
    val showDialog = remember {
        mutableStateOf(false)
    }

    TopBar(
        text = stringResource(id = R.string.write_a_letter), navController = navController
    ) {

        if (showDialog.value) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpacersSize.medium)
                .verticalScroll(verticalScroll)
        ) {

            val type =
                myDropDown(label = stringResource(id = R.string.type), list = App.listOfLetterTypes)

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            var jobTitle = ""
            val makeJobTitleVisible =
                type == stringResource(id = R.string.cover_letter) || type == stringResource(id = R.string.resignation_letter) || type == stringResource(
                    id = R.string.reference_letter
                )
            AnimatedVisibility(visible = makeJobTitleVisible) {
                jobTitle = myEditTextLabel(
                    label = stringResource(id = R.string.job_title), placeHolder = stringResource(
                        id = R.string.web_developer
                    )
                )
            }

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            val name = myEditTextLabel()

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            val length = length()

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            val inputPrefix = if (jobTitle.isNotEmpty()) {
                "${
                    stringResource(
                        id = R.string.write_a_letter_of_type,
                        type
                    )
                } for a job position of $jobTitle to $name "
            } else {
                "${
                    stringResource(
                        id = R.string.write_a_letter_of_type,
                        type
                    )
                } to $name "
            }

            val output = input(
                label = stringResource(id = R.string.letter_input_label),
                inputPrefix = inputPrefix,
                length = length,
                showDialog = showDialog
            )

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            generatedText.value = output

            Output(outputText = generatedText)

        }
    }
}