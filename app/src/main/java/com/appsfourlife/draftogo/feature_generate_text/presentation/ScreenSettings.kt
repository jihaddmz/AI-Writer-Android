package com.appsfourlife.draftogo.feature_generate_text.presentation

import android.app.Activity
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.util.SettingsNotifier
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.Screens

@Composable
fun ScreenSettings(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val verticalScroll = rememberScrollState()
    val currentActivity = LocalContext.current as Activity

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

                MySpacer(type = "small")

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MyOutlinedButton(text = stringResource(id = R.string.sign_out)) {
                        HelperAuth.signOut()
                        navController.navigate(Screens.ScreenSignIn.route)
                    }

                    MyText(text = HelperSharedPreference.getUsername())
                }
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

                    MySpacer(type = "small")

                    MyOutlinedButton(text = stringResource(id = R.string.manage_subscription)) {
                        HelperIntent.navigateToPlayStoreSubscription()
                    }

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

                if (HelperAuth.willRenew()) {
                    MyAnnotatedText(
                        textAlign = TextAlign.Center,
                        text = AnnotatedString(
                            text = "${stringResource(id = R.string.renewal_date)}: ",
                            spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
                        ).plus(AnnotatedString(text = HelperAuth.getExpirationDate())),
                    )
                } else {
                    MyAnnotatedText(
                        textAlign = TextAlign.Center,
                        text = AnnotatedString(
                            text = "${stringResource(id = R.string.renewal_date)}: ",
                            spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
                        ).plus(
                            AnnotatedString(
                                text = stringResource(id = R.string.not_renewable),
                                spanStyle = SpanStyle(color = Color.Red)
                            )
                        ),
                    )
                }

                MySpacer(type = "small")

                MyText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = SpacersSize.large),
                    text = stringResource(
                        id = R.string.app_version,
                        LocalContext.current.packageManager.getPackageInfo(
                            LocalContext.current.packageName,
                            0
                        ).versionName
                    ),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}