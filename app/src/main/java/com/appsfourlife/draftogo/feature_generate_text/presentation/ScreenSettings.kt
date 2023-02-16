package com.appsfourlife.draftogo.feature_generate_text.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.helpers.HelperAuth
import com.appsfourlife.draftogo.helpers.HelperIntent
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
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
                .padding(horizontal = SpacersSize.medium)
                .animateContentSize(animationSpec = tween(durationMillis = Constants.ANIMATION_LENGTH)),
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

            TypeWriterLength()

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val isSubscribed = HelperAuth.getUserSubscriptionState()
                if (isSubscribed) { // if the user is subscribed
                    MyAnnotatedText(
                        textAlign = TextAlign.Center,
                        text = AnnotatedString(
                            text = "${stringResource(id = R.string.subscription_status)}: ",
                            spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
                        ).plus(
                            AnnotatedString(
                                text = stringResource(
                                    id = R.string.active
                                ), spanStyle = SpanStyle(color = Color.Green)
                            )
                        ),
                    )
                } else // if the user is not subscribed
                    MyAnnotatedText(
                        textAlign = TextAlign.Center,
                        text = AnnotatedString(
                            text = "${stringResource(id = R.string.subscription_status)}: ",
                            spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
                        ).plus(
                            AnnotatedString(
                                text = stringResource(
                                    id = R.string.inactive
                                ), spanStyle = SpanStyle(color = Color.Red)
                            )
                        ),
                    )

                MySpacer(type = "small")

                if (HelperAuth.willRenew()) { // if the subscription will be renewed
                    MyAnnotatedText(
                        textAlign = TextAlign.Center,
                        text = AnnotatedString(
                            text = "${stringResource(id = R.string.expiration_date)}: ",
                            spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
                        ).plus(AnnotatedString(text = HelperAuth.getExpirationDate())),
                    )

                    MySpacer(type = "small")

                    MyOutlinedButton(text = stringResource(id = R.string.manage_subscription)) {
                        HelperIntent.navigateToPlayStoreSubscription()
                    }

                    MySpacer(type = "small")
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
}