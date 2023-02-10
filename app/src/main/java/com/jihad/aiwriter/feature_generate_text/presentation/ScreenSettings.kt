package com.jihad.aiwriter.feature_generate_text.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.jihad.aiwriter.R
import com.jihad.aiwriter.components.*
import com.jihad.aiwriter.helpers.Constants
import com.jihad.aiwriter.ui.theme.SpacersSize

@Composable
fun ScreenSettings(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    TopBar(
        text = stringResource(id = R.string.settings),
        navController = navController,
        isContextInSettings = true
    ) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = SpacersSize.medium),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .padding(top = SpacersSize.large),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    AppLogo(
                        modifier = Modifier.fillMaxWidth(),
                        imageID = R.drawable.icon_email,
                        contentDesc = stringResource(id = R.string.logo)
                    )

                    MySpacer(type = "small")

                    MyText(text = "AI Writer", fontWeight = FontWeight.Bold)
                }

                myDropDown(list = Constants.OUTPUT_LANGUAGES, label = stringResource(id = R.string.output_language))
            }

            MyText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = SpacersSize.large),
                text = stringResource(id = R.string.app_version),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}