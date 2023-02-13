package com.appsfourlife.draftogo.feature_generate_text.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.helpers.HelperAuth
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun ScreenSettings(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val verticalScroll = rememberScrollState()

    TopBar(
        text = stringResource(id = R.string.settings),
        navController = navController,
        isContextInSettings = true
    ) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(verticalScroll)
                .padding(horizontal = SpacersSize.medium),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = SpacersSize.large),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                AppLogo(
                    modifier = Modifier,
                    imageID = R.drawable.logo,
                    contentDesc = stringResource(id = R.string.logo)
                )

                MySpacer(type = "small")

                MyText(
                    text = stringResource(id = R.string.app_name),
                    fontWeight = FontWeight.Bold
                )

            }

            myDropDown(
                list = Constants.OUTPUT_LANGUAGES,
                label = stringResource(id = R.string.output_language)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val isSubscribed = HelperAuth.getUserSubscriptionState()
                if (isSubscribed)
                    MyLabelText(
                        label = "${stringResource(id = R.string.subscription_status)}:",
                        text = stringResource(
                            id = R.string.active
                        ),
                        textColor = Color.Green,
                        labelColor = Color.Black
                    )
                else
                    MyLabelText(
                        label = "${stringResource(id = R.string.subscription_status)}:",
                        text = stringResource(
                            id = R.string.inactive
                        ),
                        textColor = Color.Red,
                        labelColor = Color.Black
                    )

                MySpacer(type = "medium")

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
}