package com.jihad.aiwriter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.billingclient.api.*
import com.jihad.aiwriter.components.DialogNbOfGenerationsExceeded
import com.jihad.aiwriter.components.DrawerListItem
import com.jihad.aiwriter.feature_generate_text.presentation.*
import com.jihad.aiwriter.feature_generate_text.util.Screens
import com.jihad.aiwriter.helpers.Constants
import com.jihad.aiwriter.helpers.HelperSharedPreference
import com.jihad.aiwriter.helpers.HelperUI
import com.jihad.aiwriter.ui.theme.*
import com.jihad.aiwriter.viewModels.ViewModelSettings
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.interfaces.ReceiveCustomerInfoCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL
import java.util.*

class MainActivity : ComponentActivity() {
    lateinit var clientBilling: BillingClient

    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()
            val scaffoldState = rememberScaffoldState()
            val coroutineScope = rememberCoroutineScope()

            App.context = this

            // mark checking purchase state
            Purchases.sharedInstance.getCustomerInfo(object : ReceiveCustomerInfoCallback {
                override fun onError(error: PurchasesError) {
                }

                override fun onReceived(customerInfo: CustomerInfo) {
                    if (customerInfo.entitlements["premium"]?.isActive == true) { // if the user is subscribed
                        // set the nb of generations left to unlimited
                        HelperSharedPreference.setInt(
                            HelperSharedPreference.SP_SETTINGS,
                            HelperSharedPreference.SP_SETTINGS_NB_OF_GENERATIONS_LEFT,
                            Constants.SUBSCRIBED_NB_OF_TRIES_ALLOWED
                        )
                    } else { // user has no access to the product
                        if (HelperSharedPreference.getInt(
                                HelperSharedPreference.SP_SETTINGS,
                                HelperSharedPreference.SP_SETTINGS_NB_OF_GENERATIONS_LEFT,
                                0
                            ) == Constants.SUBSCRIBED_NB_OF_TRIES_ALLOWED
                        ) { // if the user was been subscribed, set the nb of generations left to 0, so he nedd
                            // to resubscribed
                            HelperSharedPreference.setInt(
                                HelperSharedPreference.SP_SETTINGS,
                                HelperSharedPreference.SP_SETTINGS_NB_OF_GENERATIONS_LEFT,
                                0
                            )
                            SettingsNotifier.nbOfGenerationsLeft.value = 0
                        }
                    }
                }
            })

            AIWriterTheme {
                Scaffold(
                    scaffoldState = scaffoldState,
                    drawerShape = DrawerShape,
                    // region drawer content
                    drawerContent = {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            items(App.mapOfScreens.size, key = { it }) { index ->
                                val text = App.mapOfScreens[index]!![0] as String
                                val iconID = App.mapOfScreens[index]!![1] as Int

                                DrawerListItem(
                                    modifier = Modifier.padding(SpacersSize.medium),
                                    iconID = iconID,
                                    text = text
                                ) {
                                    when (index) {
                                        0 -> {
                                            navController.navigate(Screens.ScreenEmail.route)
                                        }
                                        1 -> {
                                            navController.navigate(Screens.ScreenBlog.route)
                                        }
                                        2 -> {
                                            navController.navigate(Screens.ScreenEssay.route)
                                        }
                                        3 -> {
                                            navController.navigate(Screens.ScreenArticle.route)
                                        }
                                        4 -> {
                                            navController.navigate(Screens.ScreenLetter.route)
                                        }
                                        5 -> {
                                            navController.navigate(Screens.ScreenCV.route)
                                        }
                                        6 -> {
                                            navController.navigate(Screens.ScreenResume.route)
                                        }
                                        7 -> {
                                            navController.navigate(Screens.ScreenPoem.route)
                                        }
                                        8 -> {
                                            navController.navigate(Screens.ScreenSong.route)
                                        }
                                        9 -> {
                                            navController.navigate(Screens.ScreenTwitter.route)
                                        }
                                        10 -> {
                                            navController.navigate(Screens.ScreenTiktok.route)
                                        }

                                        11 -> {
                                            navController.navigate(Screens.ScreenInstagram.route)
                                        }

                                        12 -> {
                                            navController.navigate(Screens.ScreenFacebook.route)
                                        }

                                        13 -> {
                                            navController.navigate(Screens.ScreenYoutube.route)
                                        }

                                        14 -> {
                                            navController.navigate(Screens.ScreenPersonalBio.route)
                                        }

                                        15 -> {
                                            navController.navigate(Screens.ScreenCode.route)
                                        }

                                        16 -> {
                                            navController.navigate(Screens.ScreenCustom.route)
                                        }
                                    }
                                    coroutineScope.launch {
                                        scaffoldState.drawerState.animateTo(
                                            DrawerValue.Closed,
                                            anim = tween(durationMillis = Constants.ANIMATION_LENGTH)
                                        )
                                    }

                                }
                            }
                        }
                    }
                    // endregion
                ) {
                    androidx.compose.material.Surface(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {

                            if (SettingsNotifier.showDialogNbOfGenerationsLeftExceeded.value)
                                DialogNbOfGenerationsExceeded()

                            NavHost(
                                navController = navController,
                                startDestination = Screens.ScreenLaunch.route
                            ) {

                                // region composables
                                composable(route = Screens.ScreenLaunch.route) {
                                    BackHandler(true) {
                                        HelperUI.showToast(
                                            this@MainActivity,
                                            getString(R.string.back_button_disabled)
                                        )
                                    }
                                    ScreenLaunch(navController = navController)
                                }

                                composable(route = Screens.ScreenHome.route) {
                                    BackHandler(true) {
                                        HelperUI.showToast(
                                            this@MainActivity,
                                            getString(R.string.back_button_disabled)
                                        )
                                    }
                                    ScreenHome(
                                        modifier = Modifier.padding(top = SpacersSize.medium),
                                        navController = navController
                                    )
                                }

                                composable(route = Screens.ScreenArticle.route) {
                                    BackHandler(true) {
                                        HelperUI.showToast(
                                            this@MainActivity,
                                            getString(R.string.back_button_disabled)
                                        )
                                    }
                                    ScreenArticle(navController = navController)
                                }

                                composable(route = Screens.ScreenBlog.route) {
                                    BackHandler(true) {
                                        HelperUI.showToast(
                                            this@MainActivity,
                                            getString(R.string.back_button_disabled)
                                        )
                                    }
                                    ScreenBlog(navController = navController)
                                }

                                composable(route = Screens.ScreenCode.route) {
                                    BackHandler(true) {
                                        HelperUI.showToast(
                                            this@MainActivity,
                                            getString(R.string.back_button_disabled)
                                        )
                                    }
                                    ScreenCode(navController = navController)
                                }

                                composable(route = Screens.ScreenCV.route) {
                                    BackHandler(true) {
                                        HelperUI.showToast(
                                            this@MainActivity,
                                            getString(R.string.back_button_disabled)
                                        )
                                    }
                                    ScreenCV(navController = navController)
                                }

                                composable(route = Screens.ScreenResume.route) {
                                    BackHandler(true) {
                                        HelperUI.showToast(
                                            this@MainActivity,
                                            getString(R.string.back_button_disabled)
                                        )
                                    }
                                    ScreenResume(navController = navController)
                                }

                                composable(route = Screens.ScreenEmail.route) {
                                    BackHandler(true) {
                                        HelperUI.showToast(
                                            this@MainActivity,
                                            getString(R.string.back_button_disabled)
                                        )
                                    }
                                    ScreenEmail(navController = navController)
                                }

                                composable(route = Screens.ScreenEssay.route) {
                                    BackHandler(true) {
                                        HelperUI.showToast(
                                            this@MainActivity,
                                            getString(R.string.back_button_disabled)
                                        )
                                    }
                                    ScreenEssay(navController = navController)
                                }

                                composable(route = Screens.ScreenInstagram.route) {
                                    BackHandler(true) {
                                        HelperUI.showToast(
                                            this@MainActivity,
                                            getString(R.string.back_button_disabled)
                                        )
                                    }
                                    ScreenInstagram(navController = navController)
                                }

                                composable(route = Screens.ScreenLetter.route) {
                                    BackHandler(true) {
                                        HelperUI.showToast(
                                            this@MainActivity,
                                            getString(R.string.back_button_disabled)
                                        )
                                    }
                                    ScreenLetter(navController = navController)
                                }

                                composable(route = Screens.ScreenPoem.route) {
                                    BackHandler(true) {
                                        HelperUI.showToast(
                                            this@MainActivity,
                                            getString(R.string.back_button_disabled)
                                        )
                                    }
                                    ScreenPoem(navController = navController)
                                }

                                composable(route = Screens.ScreenTiktok.route) {
                                    BackHandler(true) {
                                        HelperUI.showToast(
                                            this@MainActivity,
                                            getString(R.string.back_button_disabled)
                                        )
                                    }
                                    ScreenTiktok(navController = navController)
                                }

                                composable(route = Screens.ScreenTwitter.route) {
                                    BackHandler(true) {
                                        HelperUI.showToast(
                                            this@MainActivity,
                                            getString(R.string.back_button_disabled)
                                        )
                                    }
                                    ScreenTweet(navController = navController)
                                }

                                composable(route = Screens.ScreenPersonalBio.route) {
                                    BackHandler(true) {
                                        HelperUI.showToast(
                                            this@MainActivity,
                                            getString(R.string.back_button_disabled)
                                        )
                                    }
                                    ScreenPersonalBio(navController = navController)
                                }

                                composable(route = Screens.ScreenCustom.route) {
                                    BackHandler(true) {
                                        HelperUI.showToast(
                                            this@MainActivity,
                                            getString(R.string.back_button_disabled)
                                        )
                                    }
                                    ScreenCustom(navController = navController)
                                }

                                composable(route = Screens.ScreenYoutube.route) {
                                    BackHandler(true) {
                                        HelperUI.showToast(
                                            this@MainActivity,
                                            getString(R.string.back_button_disabled)
                                        )
                                    }
                                    ScreenYoutube(navController = navController)
                                }

                                composable(route = Screens.ScreenFacebook.route) {
                                    BackHandler(true) {
                                        HelperUI.showToast(
                                            this@MainActivity,
                                            getString(R.string.back_button_disabled)
                                        )
                                    }
                                    ScreenFacebook(navController = navController)
                                }

                                composable(route = Screens.ScreenSong.route) {
                                    BackHandler(true) {
                                        HelperUI.showToast(
                                            this@MainActivity,
                                            getString(R.string.back_button_disabled)
                                        )
                                    }
                                    ScreenSong(navController = navController)
                                }
                                // endregion
                            }
                        }
                    }
                }
            }
        }
    }
}