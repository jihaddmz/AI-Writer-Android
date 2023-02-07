package com.jihad.aiwriter

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.billingclient.api.*
import com.jihad.aiwriter.components.*
import com.jihad.aiwriter.feature_generate_text.presentation.*
import com.jihad.aiwriter.feature_generate_text.util.Screens
import com.jihad.aiwriter.helpers.Constants
import com.jihad.aiwriter.ui.theme.*
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : ComponentActivity() {

    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()
            val scaffoldState = rememberScaffoldState()
            val coroutineScope = rememberCoroutineScope()

            App.context = this

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
                                    MyBackHandler(context = this@MainActivity)
                                    ScreenLaunch(
                                        modifier = Modifier,
                                        navController = navController
                                    )
                                }

                                composable(route = Screens.ScreenHome.route) {
                                    MyBackHandler(context = this@MainActivity)
                                    ScreenHome(
                                        modifier = Modifier,
                                        navController = navController
                                    )
                                }

                                composable(route = Screens.ScreenArticle.route) {
                                    MyBackHandler(context = this@MainActivity)
                                    ScreenArticle(navController = navController)
                                }

                                composable(route = Screens.ScreenBlog.route) {
                                    MyBackHandler(context = this@MainActivity)
                                    ScreenBlog(navController = navController)
                                }

                                composable(route = Screens.ScreenCode.route) {
                                    MyBackHandler(context = this@MainActivity)
                                    ScreenCode(navController = navController)
                                }

                                composable(route = Screens.ScreenCV.route) {
                                    MyBackHandler(context = this@MainActivity)
                                    ScreenCV(navController = navController)
                                }

                                composable(route = Screens.ScreenResume.route) {
                                    MyBackHandler(context = this@MainActivity)
                                    ScreenResume(navController = navController)
                                }

                                composable(route = Screens.ScreenEmail.route) {
                                    MyBackHandler(context = this@MainActivity)
                                    ScreenEmail(navController = navController)
                                }

                                composable(route = Screens.ScreenEssay.route) {
                                    MyBackHandler(context = this@MainActivity)
                                    ScreenEssay(navController = navController)
                                }

                                composable(route = Screens.ScreenInstagram.route) {
                                    MyBackHandler(context = this@MainActivity)
                                    ScreenInstagram(navController = navController)
                                }

                                composable(route = Screens.ScreenLetter.route) {
                                    MyBackHandler(context = this@MainActivity)
                                    ScreenLetter(navController = navController)
                                }

                                composable(route = Screens.ScreenPoem.route) {
                                    MyBackHandler(context = this@MainActivity)
                                    ScreenPoem(navController = navController)
                                }

                                composable(route = Screens.ScreenTiktok.route) {
                                    MyBackHandler(context = this@MainActivity)
                                    ScreenTiktok(navController = navController)
                                }

                                composable(route = Screens.ScreenTwitter.route) {
                                    MyBackHandler(context = this@MainActivity)
                                    ScreenTweet(navController = navController)
                                }

                                composable(route = Screens.ScreenPersonalBio.route) {
                                    MyBackHandler(context = this@MainActivity)
                                    ScreenPersonalBio(navController = navController)
                                }

                                composable(route = Screens.ScreenCustom.route) {
                                    MyBackHandler(context = this@MainActivity)
                                    ScreenCustom(navController = navController)
                                }

                                composable(route = Screens.ScreenYoutube.route) {
                                    MyBackHandler(context = this@MainActivity)
                                    ScreenYoutube(navController = navController)
                                }

                                composable(route = Screens.ScreenFacebook.route) {
                                    MyBackHandler(context = this@MainActivity)
                                    ScreenFacebook(navController = navController)
                                }

                                composable(route = Screens.ScreenSong.route) {
                                    MyBackHandler(context = this@MainActivity)
                                    ScreenSong(navController = navController)
                                }

                                composable(route = Screens.ScreenSettings.route) {
                                    MyBackHandler(context = this@MainActivity)
                                    ScreenSettings(navController = navController)
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