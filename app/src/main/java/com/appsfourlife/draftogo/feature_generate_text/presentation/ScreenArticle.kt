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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
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
        text = stringResource(id = R.string.write_an_article),
        navController = navController
    ) {
        if (showDialog.value)
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

        BottomSheet(
            modifier = modifier
                .fillMaxSize(),
            navController = navController
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(verticalScroll)
                    .padding(start = SpacersSize.medium, end = SpacersSize.medium, bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val length = length()

                Spacer(modifier = Modifier.height(SpacersSize.medium))

                input(
                    label = stringResource(id = R.string.article_input_label),
                    inputPrefix = stringResource(
                        id = R.string.write_an_article
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
}