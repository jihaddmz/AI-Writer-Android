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
fun ScreenBlog(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val verticalScroll = rememberScrollState()
    val generatedText = remember {
        mutableStateOf("")
    }
    val showDialog = remember {
        mutableStateOf(false)
    }

    TopBar(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(verticalScroll),
        text = stringResource(id = R.string.write_a_blog_top_bar),
        navController = navController
    ) {

        if (showDialog.value) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpacersSize.medium)
        ) {

            val isHeadlinesEnabled = headlines()

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            val length = length()

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            val inputPrefix = if (isHeadlinesEnabled) {
                "${
                    stringResource(
                        id = R.string.write_a_blog,
                        HelperSharedPreference.getOutputLanguage()
                    )
                } with headlines "
            } else {
                "${stringResource(id = R.string.write_a_blog, HelperSharedPreference.getOutputLanguage())} "
            }

            val output = input(
                label = stringResource(id = R.string.blog_input_label),
                inputPrefix = inputPrefix,
                length = length,
                showDialog = showDialog
            )

            generatedText.value = output

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            Output(outputText = generatedText)
        }
    }
}