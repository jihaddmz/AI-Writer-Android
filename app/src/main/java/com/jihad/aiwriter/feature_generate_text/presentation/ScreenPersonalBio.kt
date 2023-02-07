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
import com.jihad.aiwriter.helpers.HelperSharedPreference
import com.jihad.aiwriter.helpers.Helpers
import com.jihad.aiwriter.ui.theme.SpacersSize

@Composable
fun ScreenPersonalBio(
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

            val output = input(
                label = stringResource(id = R.string.that_captures_attention),
                inputPrefix = inputPrefix,
                showDialog = showDialog
            )

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            generatedText.value = output

            Output(outputText = generatedText)

        }
    }
}