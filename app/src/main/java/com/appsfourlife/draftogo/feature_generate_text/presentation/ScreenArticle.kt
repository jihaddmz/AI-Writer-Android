package com.appsfourlife.draftogo.feature_generate_text.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.Output
import com.appsfourlife.draftogo.components.TopBar
import com.appsfourlife.draftogo.components.input
import com.appsfourlife.draftogo.components.length
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.SettingsNotifier

@Composable
fun ScreenArticle(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    SettingsNotifier.templateType = "Article"

    val verticalScroll = rememberScrollState()
    val showDialog = remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()

    TopBar(
        text = stringResource(id = R.string.write_an_article_top_bar),
        navController = navController
    ) {
        if (showDialog.value)
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(SpacersSize.medium)
                .verticalScroll(verticalScroll),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val length = length()

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            input(
                label = stringResource(id = R.string.article_input_label),
                inputPrefix = stringResource(
                    id = R.string.write_an_article,
                    HelperSharedPreference.getOutputLanguage()
                ),
                length = length,
                showDialog = showDialog,
                verticalScrollState = verticalScroll
            )

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            Output(outputText = SettingsNotifier.output)
        }
    }
}