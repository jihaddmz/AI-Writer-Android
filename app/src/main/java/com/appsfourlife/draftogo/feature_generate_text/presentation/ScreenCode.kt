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
import com.appsfourlife.draftogo.components.Output
import com.appsfourlife.draftogo.components.TopBar
import com.appsfourlife.draftogo.components.input
import com.appsfourlife.draftogo.components.myDropDown
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun ScreenCode(
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
        text = stringResource(id = R.string.write_a_code),
        navController = navController
    ) {

        if (showDialog.value) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpacersSize.medium)
                .verticalScroll(verticalScroll)
        ) {

            val programmingLanguage =
                myDropDown(label = stringResource(id = R.string.programming_language))

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            val output = input(
                label = stringResource(id = R.string.code_input_label),
                inputPrefix = "${stringResource(id = R.string.write_a_code)} in $programmingLanguage",
                showDialog = showDialog
            )

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            generatedText.value = output

            Output(outputText = generatedText)
        }
    }

}