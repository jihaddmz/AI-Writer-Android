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
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.SettingsNotifier

@Composable
fun ScreenCode(
    navController: NavController
) {

    SettingsNotifier.templateType = "Code"

    val verticalScroll = rememberScrollState()
    val showDialog = remember {
        mutableStateOf(false)
    }

    TopBar(
        text = stringResource(id = R.string.write_a_code),
        navController = navController
    ) {

        if (showDialog.value) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

        BottomSheetWriting(navController = navController) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = SpacersSize.medium, end = SpacersSize.medium)
                    .verticalScroll(verticalScroll)
            ) {

                val programmingLanguage =
                    myDropDown(label = stringResource(id = R.string.programming_language))

                Spacer(modifier = Modifier.height(SpacersSize.medium))

                input(
                    label = stringResource(id = R.string.code_input_label),
                    inputPrefix = "${stringResource(id = R.string.write_a_code)} in $programmingLanguage",
                    showDialog = showDialog,
                    verticalScrollState = verticalScroll
                )

                Spacer(modifier = Modifier.height(SpacersSize.medium))

                Output(outputText = SettingsNotifier.output)
            }
        }
    }

}