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
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.SettingsNotifier
import kotlin.math.abs

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

                MyText(text = HelperSharedPreference.getUsername())

                if (HelperAuth.isSubscribed() && HelperSharedPreference.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_BASE) {
                    MySpacer(type = "small")
                    val nbOfWordsLeft =
                        abs(Constants.BASE_PLAN_MAX_NB_OF_WORDS - HelperSharedPreference.getNbOfWordsGenerated())
                    MyText(text = stringResource(id = R.string.nb_words_left, nbOfWordsLeft))
                }

            }

            TypeWriterLength()

            mySwitch(
                modifier = Modifier.padding(end = SpacersSize.small),
                label = stringResource(id = R.string.enable_save_outputs),
                initialValue = HelperSharedPreference.getIsSavedOutputsEnabled()
            ) {
                HelperSharedPreference.setBool(
                    HelperSharedPreference.SP_SETTINGS,
                    HelperSharedPreference.SP_SETTINGS_IS_SAVED_OUTPUTS_ENABLED,
                    it
                )
                SettingsNotifier.enableSheetContent.value = it

                if (it) {
                    HelperAnalytics.sendEvent("enabled_saved_outputs")
                } else {
                    HelperAnalytics.sendEvent("disabled_saved_outputs")
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val isSubscribed = HelperAuth.isSubscribed()
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
                        if (HelperSharedPreference.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_PLUS) {
                            HelperIntent.navigateToPlayStorePlusSubscription()
                        } else
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