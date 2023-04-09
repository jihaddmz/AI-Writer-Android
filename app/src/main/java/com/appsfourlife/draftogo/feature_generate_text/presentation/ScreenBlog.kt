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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.SettingsNotifier


@Composable
fun ScreenBlog(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    SettingsNotifier.templateType = "Blog"

    val verticalScroll = rememberScrollState()
    val showDialog = remember {
        mutableStateOf(false)
    }

    TopBar(
        modifier = modifier
            .fillMaxSize(),
        text = stringResource(id = R.string.write_a_blog),
        navController = navController
    ) {

        if (showDialog.value) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

        BottomSheetSaveOutputs(navController = navController) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = SpacersSize.medium, end = SpacersSize.medium, bottom = 80.dp)
                    .verticalScroll(verticalScroll)
            ) {

                val isHeadlinesEnabled = mySwitch() {
                    HelperSharedPreference.setBool(
                        HelperSharedPreference.SP_SETTINGS,
                        HelperSharedPreference.SP_SETTINGS_ENABLE_HEADLINES,
                        it,
                    )
                }

                Spacer(modifier = Modifier.height(SpacersSize.medium))

                val length = length()

                Spacer(modifier = Modifier.height(SpacersSize.medium))

                val inputPrefix = if (isHeadlinesEnabled) {
                    "${
                        stringResource(
                            id = R.string.write_a_blog
                        )
                    } with headlines "
                } else {
                    "${
                        stringResource(
                            id = R.string.write_a_blog,
                            HelperSharedPreference.getOutputLanguage()
                        )
                    } "
                }

                input(
                    label = stringResource(id = R.string.blog_input_label),
                    inputPrefix = inputPrefix,
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