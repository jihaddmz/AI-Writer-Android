package com.appsfourlife.draftogo.home.presentation

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.BottomNavScreens
import com.appsfourlife.draftogo.util.Screens
import com.appsfourlife.draftogo.util.SettingsNotifier.navHostController

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenSettings(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val verticalScroll = rememberScrollState()
    val context = LocalContext.current
    val currentActivity = LocalContext.current as Activity
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()

//    TopBar(
//        text = stringResource(id = R.string.settings),
//        navController = navController,
//        hideNbOfGenerationsLeft = true
//    ) {

    BottomSheet(sheetScaffoldState = bottomSheetScaffoldState, bottomSheet = {
        BottomSheetRedeemCode()
    }) {

        AppBarTransparent(title = stringResource(id = R.string.settings)) {
            navHostController?.navigate(BottomNavScreens.Dashboard.route)
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(verticalScroll)
                .padding(horizontal = SpacersSize.medium)
                .animateContentSize(animationSpec = tween(durationMillis = Constants.ANIMATION_LENGTH)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier.padding(top = SpacersSize.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                MyUrlImage(
                    modifier = Modifier.clip(CircleShape),
                    imageUrl = HelperAuth.auth.currentUser?.photoUrl.toString(),
                    contentDesc = "",
                    baseSize = 55.dp
                )
                MyText(text = HelperAuth.auth.currentUser?.email!!)

                MySpacer(type = "large")

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

                MySpacer(type = "small")

                MyTextLink(
                    text = stringResource(id = R.string.website),
                    modifier = Modifier.clickable {
                        HelperIntent.navigateToUrl("https://appsfourlife.com/draftogo/")
                    })

                MySpacer(type = "small")

                MyOutlinedButton(text = stringResource(id = R.string.send_feedback)) {
                    navController.navigate(Screens.ScreenFeedback.route)
                }

//                MySpacer(type = "small")
//
//                MyOutlinedButton(text = stringResource(id = R.string.redeem_code)) {
//                    coroutineScope.launch(Dispatchers.IO) {
//                        bottomSheetScaffoldState.bottomSheetState.expand()
//                    }
//                }
//            }

//            MySpacer(type = "large")
//
//            TypeModelChooser()
//
//            MySpacer(type = "small")
//
//            TypeWriterLength()

//            MySpacer(type = "small")
//
//            mySwitch(
//                modifier = Modifier.padding(end = SpacersSize.small),
//                label = stringResource(id = R.string.enable_save_outputs),
//                initialValue = HelperSharedPreference.getIsSavedOutputsEnabled()
//            ) {
//                HelperSharedPreference.setBool(
//                    HelperSharedPreference.SP_SETTINGS,
//                    HelperSharedPreference.SP_SETTINGS_IS_SAVED_OUTPUTS_ENABLED,
//                    it
//                )
//                SettingsNotifier.enableSheetContent.value = it
//
//                if (it) {
//                    HelperAnalytics.sendEvent("enabled_saved_outputs")
//                } else {
//                    HelperAnalytics.sendEvent("disabled_saved_outputs")
//                }
//            }

                MySpacer(type = "large")

                val showAccessibilityPermissionRequester = remember {
                    mutableStateOf(false)
                }
                if (showAccessibilityPermissionRequester.value)
                    HelperUI.ShowAccessibilityPermissionRequester(false)

                mySwitch(
                    modifier = Modifier.padding(end = SpacersSize.small),
                    label = stringResource(id = R.string.enable_write_anywhere),
                    initialValue = HelperPermissions.isAccessibilityEnabled(context = context),
                    onCheckedChange = {
                        showAccessibilityPermissionRequester.value = it
                        if (!it) {
                            val intent =
                                Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            // request permission via start activity for result
                            context.startActivity(intent)
                        }
                    })

                MySpacer(type = "large")

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // todo uncomment this when we want to re-enable the paid plans
//                val isSubscribed = HelperAuth.isSubscribed()
//                if (isSubscribed) { // if the user is subscribed
//                    MyAnnotatedText(
//                        textAlign = TextAlign.Center,
//                        text = AnnotatedString(
//                            text = "${stringResource(id = R.string.subscription_status)}: ",
//                            spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
//                        ).plus(
//                            AnnotatedString(
//                                text = stringResource(
//                                    id = R.string.active
//                                ), spanStyle = SpanStyle(color = Color.Green)
//                            )
//                        ),
//                    )
//
//                    MySpacer(type = "small")
//
//                    MyOutlinedButton(text = stringResource(id = R.string.manage_subscription)) {
//                        if (HelperSharedPreference.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_PLUS) {
//                            HelperIntent.navigateToPlayStorePlusSubscription()
//                        } else
//                            HelperIntent.navigateToPlayStoreSubscription()
//                    }
//
//                } else // if the user is not subscribed
//                    MyAnnotatedText(
//                        textAlign = TextAlign.Center,
//                        text = AnnotatedString(
//                            text = "${stringResource(id = R.string.subscription_status)}: ",
//                            spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
//                        ).plus(
//                            AnnotatedString(
//                                text = stringResource(
//                                    id = R.string.inactive
//                                ), spanStyle = SpanStyle(color = Color.Red)
//                            )
//                        ),
//                    )
//
//                MySpacer(type = "small")
//
//                if (HelperAuth.willRenew()) {
//                    MyAnnotatedText(
//                        textAlign = TextAlign.Center,
//                        text = AnnotatedString(
//                            text = "${stringResource(id = R.string.renewal_date)}: ",
//                            spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
//                        ).plus(AnnotatedString(text = HelperAuth.getExpirationDate())),
//                    )
//                } else {
//                    MyAnnotatedText(
//                        textAlign = TextAlign.Center,
//                        text = AnnotatedString(
//                            text = "${stringResource(id = R.string.renewal_date)}: ",
//                            spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
//                        ).plus(
//                            AnnotatedString(
//                                text = stringResource(id = R.string.not_renewable),
//                                spanStyle = SpanStyle(color = Color.Red)
//                            )
//                        ),
//                    )
//                }
//
//                MySpacer(type = "small")

                }
            }
        }
    }
}
//}