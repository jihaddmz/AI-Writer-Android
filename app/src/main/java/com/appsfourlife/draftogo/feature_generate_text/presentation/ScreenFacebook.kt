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
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun ScreenFacebook(
    navController: NavController
) {

    val generatedText = remember {
        mutableStateOf("")
    }
    val verticalScroll = rememberScrollState()
    val showDialog = remember {
        mutableStateOf(false)
    }
    val listOfGenerations = remember {
        mutableListOf<String>()
    }

    TopBar(
        text = stringResource(id = R.string.write_a_facebook_post_top_bar), navController = navController
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpacersSize.medium)
                .verticalScroll(verticalScroll)
        ) {

            if (showDialog.value) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

            val nbOfGenerations = sliderNbOfGenerations()

            MySpacer(type = "small")

            val output = input(
                label = stringResource(id = R.string.facebook_input_label),
                inputPrefix = stringResource(id = R.string.write_a_facebook_post, HelperSharedPreference.getOutputLanguage()),
                showDialog = showDialog,
                nbOfGenerations = nbOfGenerations,
                listOfGeneratedTexts = listOfGenerations
            )

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            if (nbOfGenerations == 1 || listOfGenerations.isEmpty()) {
                generatedText.value = output
                Output(outputText = generatedText)
            } else if (nbOfGenerations > 0) {
                listOfGenerations.forEach {
                    Output(outputText = mutableStateOf(it))

                    MySpacer(type = "small")
                }
            }

        }
    }
}