package com.jihad.aiwriter.feature_generate_text.presentation

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
import com.jihad.aiwriter.R
import com.jihad.aiwriter.components.Output
import com.jihad.aiwriter.components.TopBar
import com.jihad.aiwriter.components.input
import com.jihad.aiwriter.components.myDropDown
import com.jihad.aiwriter.ui.theme.SpacersSize

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