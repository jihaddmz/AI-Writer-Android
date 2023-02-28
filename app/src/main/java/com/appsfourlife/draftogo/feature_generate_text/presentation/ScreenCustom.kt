package com.appsfourlife.draftogo.feature_generate_text.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.SettingsNotifier
import com.appsfourlife.draftogo.SettingsNotifier.output
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.helpers.Helpers
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@Composable
fun ScreenCustom(
    navController: NavController
) {
    val verticalScroll = rememberScrollState()
    val showDialog = remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()

    TopBar(
        text = stringResource(id = R.string.custom), navController = navController
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

            val length = length()

            MySpacer(type = "small")

            input(
                label = stringResource(id = R.string.write_a_custom_text),
                showDialog = showDialog,
                length = length,
                nbOfGenerations = nbOfGenerations,
                verticalScrollState = verticalScroll,
                checkIfInputIsEmpty = true
            )

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            if (nbOfGenerations == 1 && SettingsNotifier.outputList.isEmpty()) {
                Output(outputText = SettingsNotifier.output)
            } else if (nbOfGenerations > 0) {
                SettingsNotifier.outputList.forEach {
                    Output(outputText = mutableStateOf(it))
                    MySpacer(type = "small")
                }
            }
        }
    }
}