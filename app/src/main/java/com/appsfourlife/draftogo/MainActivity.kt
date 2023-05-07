package com.appsfourlife.draftogo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.android.billingclient.api.*
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.extensions.animateOffsetY
import com.appsfourlife.draftogo.extensions.determineTemplateRoute
import com.appsfourlife.draftogo.feature_generate_art.presentation.ScreenArt
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelTemplate
import com.appsfourlife.draftogo.feature_generate_text.presentation.*
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.home.presentation.ScreenDashboard
import com.appsfourlife.draftogo.home.presentation.ScreenFeedback
import com.appsfourlife.draftogo.home.presentation.ScreenSettings
import com.appsfourlife.draftogo.ui.theme.*
import com.appsfourlife.draftogo.util.BottomNavScreens
import com.appsfourlife.draftogo.util.Screens
import com.appsfourlife.draftogo.util.SettingsNotifier
import com.google.android.gms.ads.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timerTask


class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onStart() {
        super.onStart()

        // Declare the launcher at the top of your Activity/Fragment:
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // FCM SDK (and your app) can post notifications.
            } else {
            }
        }
    }

    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {

            App.context = this

            MobileAds.initialize(this) {

            }

            if (SettingsNotifier.isConnected.value)
                HelperAds.loadAds {

                }

            setContent {

                Timer().schedule(timerTask {
                    askNotificationPermission(requestPermissionLauncher)
                }, 2000)

                navController = rememberNavController()
                SettingsNotifier.navHostController = navController
                val scaffoldState = rememberScaffoldState()
                val coroutineScope = rememberCoroutineScope()
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                LaunchedEffect(key1 = true, block = {
                    coroutineScope.launch(Dispatchers.IO) {
                        if (HelperSharedPreference.getBool(
                                HelperSharedPreference.SP_SETTINGS,
                                HelperSharedPreference.SP_SETTINGS_IS_FIRST_TIME_V2_LAUNCHED,
                                true
                            )
                        ) {
                            App.databaseApp.daoApp.getAllTemplates().forEach {
                                App.databaseApp.daoApp.deleteTemplate(it)
                            }
                            HelperSharedPreference.setBool(
                                HelperSharedPreference.SP_SETTINGS,
                                HelperSharedPreference.SP_SETTINGS_IS_FIRST_TIME_V2_LAUNCHED,
                                false
                            )
                        }
                        Constants.PREDEFINED_TEMPLATES.forEach { template ->
                            if (App.databaseApp.daoApp.getTemplateByQuery(
                                    template
                                ) == null
                            ) {
                                App.databaseApp.daoApp.insertTemplate(
                                    ModelTemplate(template, "", 1)
                                )
                            } else
                                return@forEach
                        }
                        SettingsNotifier.predefinedTemplates.value =
                            App.databaseApp.daoApp.getAllTemplates() as MutableList<ModelTemplate>
                    }
                })



                AIWriterTheme {

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        scaffoldState = scaffoldState,
                        backgroundColor = Glass,
                        drawerContent = {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                items(
                                    SettingsNotifier.predefinedTemplates.value.size,
                                    key = { it }) { index ->
                                    val text =
                                        SettingsNotifier.predefinedTemplates.value[index].query
                                    val imageUrl =
                                        SettingsNotifier.predefinedTemplates.value[index].imageUrl

                                    val templateDestinationRoute = remember {
                                        mutableStateOf("")
                                    }
                                    DrawerListItem(
                                        modifier = Modifier
                                            .padding(SpacersSize.medium)
                                            .determineTemplateRoute(
                                                text = text,
                                                templateDestinationRoute
                                            ),
                                        text = text,
                                        imageUrl = imageUrl
                                    ) {

                                        SettingsNotifier.resetValues() // clearing values

                                        coroutineScope.launch {
                                            scaffoldState.drawerState.animateTo(
                                                DrawerValue.Closed,
                                                anim = tween(durationMillis = 500)
                                            )
                                            navController.navigate(templateDestinationRoute.value)
                                        }
                                    }
                                }
                            }
                        },
                        drawerShape = DrawerShape,
                        bottomBar = {
                            val changeTargetValue = remember {
                                mutableStateOf(false)
                            }

                            val shouldBottomBarBeVisible =
                                (navBackStackEntry?.destination?.route != Screens.ScreenSignIn.route
                                        && navBackStackEntry?.destination?.route != Screens.ScreenLaunch.route)

                            LaunchedEffect(key1 = shouldBottomBarBeVisible, block = {
                                if (shouldBottomBarBeVisible) {
                                    delay(Constants.SPLASH_SCREEN_DURATION)
                                    changeTargetValue.value = true
                                } else {
                                    changeTargetValue.value = false
                                }
                            })

                            if (changeTargetValue.value) {
                                val listOfBottomNavScreens =
                                    listOf(
                                        BottomNavScreens.Dashboard,
                                        BottomNavScreens.Content,
                                        BottomNavScreens.Art,
                                        BottomNavScreens.Settings
                                    )
                                BottomNavigation(
                                    modifier = Modifier.animateOffsetY(
                                        initialOffsetY = 70.dp,
                                    ), backgroundColor = Glass
                                ) {
                                    val currentRoute = navBackStackEntry?.destination?.route

                                    listOfBottomNavScreens.forEach { screen ->
                                        /**
                                         * if the current screen is one of the templates, select the content bottom nav bar, otherwise select the current route
                                         **/
                                        val isSelected =
                                            if (screen == BottomNavScreens.Content && currentRoute != Screens.ScreenSignIn.route && currentRoute != BottomNavScreens.Dashboard.route && currentRoute != BottomNavScreens.Art.route && currentRoute != BottomNavScreens.Settings.route && currentRoute != Screens.ScreenFeedback.route) {
                                                true
                                            } else {
                                                currentRoute == screen.route
                                            }
                                        BottomNavigationItem(
                                            selected = isSelected,
                                            onClick = {
                                                SettingsNotifier.resetValues()

                                                if (currentRoute != screen.route) {
                                                    navController.navigate(screen.route)
                                                }
                                            },
                                            icon = {
                                                MyIcon(
                                                    iconID = screen.iconID,
                                                    contentDesc = stringResource(
                                                        id = screen.labelID
                                                    )
                                                )
                                            },
                                            label = { Text(text = stringResource(id = screen.labelID)) },
                                            selectedContentColor = Blue,
                                            unselectedContentColor = Color.Black
                                        )
                                    }
                                }
                            }
                        }
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {

                                val route = remember {
                                    mutableStateOf(Screens.ScreenEssay.route)
                                }
                                LaunchedEffect(key1 = true) {
                                    coroutineScope.launch(Dispatchers.Main) {
                                        delay(Constants.SPLASH_SCREEN_DURATION + 1000L)
                                        if (intent.hasExtra("templateClickedQuery")) {
                                            val query =
                                                intent.getStringExtra("templateClickedQuery")
                                            Modifier.determineTemplateRoute(query!!, route)
                                            navController.navigate(route.value)
                                        }
                                    }
                                }
                            }

                            /**
                             * if the android version is equal or greater than 12, remove the custom splash screen
                             * and check if the user should be navigated directly to the sign in or home screen
                             **/
                            val startScreenRoute = if (Build.VERSION.SDK_INT >= 31) {
                                setSpacersSize()
                                if (HelperSharedPreference.getUsername() == "") {
                                    Screens.ScreenSignIn.route
                                } else {
                                    BottomNavScreens.Dashboard.route
                                }

                            } else {
                                Screens.ScreenLaunch.route
                            }

                            Box(
                                modifier = Modifier,
                                contentAlignment = Alignment.Center
                            ) {
                                NavHost(
                                    navController = navController,
                                    startDestination = startScreenRoute
                                ) {
                                    // region composables
                                    composable(route = Screens.ScreenLaunch.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenLaunch(
                                            modifier = Modifier,
                                            navController = navController
                                        )
                                    }

                                    composable(route = Screens.ScreenHistory.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenHistory(
                                            modifier = Modifier,
                                            navController = navController
                                        )
                                    }

                                    composable(route = Screens.ScreenFeedback.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenFeedback(navController = navController)
                                    }

                                    composable(route = Screens.ScreenSignIn.route) {
                                        HomeBackHandler(context = this@MainActivity)
                                        ScreenSignIn(
                                        )
                                    }

                                    composable(route = BottomNavScreens.Content.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenContent(
                                            modifier = Modifier,
                                            navController = navController
                                        )
                                    }

                                    composable(route = BottomNavScreens.Dashboard.route) {
                                        HomeBackHandler(context = this@MainActivity)
                                        ScreenDashboard(navController)
                                    }

                                    composable(route = Screens.ScreenArticle.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenArticle(
                                            navController = navController,
                                            modifier = Modifier,
                                        )
                                    }

                                    composable(route = Screens.ScreenTranslate.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenTranslate(navController = navController)
                                    }

                                    composable(route = Screens.ScreenSummarize.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenSummarize(navController = navController)
                                    }

                                    composable(route = Screens.ScreenGrammar.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenGrammar(navController = navController)
                                    }

                                    composable(route = BottomNavScreens.Art.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenArt(navController = navController)
                                    }

                                    composable(route = Screens.ScreenUserAddedTemplate.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenUserAddedTemplate(navController = navController)
                                    }

                                    composable(route = Screens.ScreenBlog.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenBlog(navController = navController)
                                    }

                                    composable(route = Screens.ScreenCode.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenCode(navController = navController)
                                    }

                                    composable(route = Screens.ScreenCV.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenCV(navController = navController)
                                    }

                                    composable(route = Screens.ScreenResume.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenResume(navController = navController)
                                    }

                                    composable(route = Screens.ScreenEmail.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenEmail(navController = navController)
                                    }

                                    composable(route = Screens.ScreenEssay.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenEssay(navController = navController)
                                    }

                                    composable(route = Screens.ScreenInstagram.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenInstagram(navController = navController)
                                    }

                                    composable(route = Screens.ScreenLetter.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenLetter(navController = navController)
                                    }

                                    composable(route = Screens.ScreenPoem.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenPoem(navController = navController)
                                    }

                                    composable(route = Screens.ScreenTiktok.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenTiktok(navController = navController)
                                    }

                                    composable(route = Screens.ScreenTwitter.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenTweet(navController = navController)
                                    }

                                    composable(route = Screens.ScreenPersonalBio.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenPersonalBio(navController = navController)
                                    }

                                    composable(route = Screens.ScreenCustom.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenCustom(navController = navController)
                                    }

                                    composable(route = Screens.ScreenYoutube.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenYoutube(navController = navController)
                                    }

                                    composable(route = Screens.ScreenFacebook.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenFacebook(navController = navController)
                                    }

                                    composable(route = Screens.ScreenLinkedIn.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenLinkedIn(navController = navController)
                                    }

                                    composable(route = Screens.ScreenSong.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenSong(navController = navController)
                                    }

                                    composable(route = BottomNavScreens.Settings.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenSettings(navController = navController)
                                    }

                                    composable(route = Screens.ScreenPodcast.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenPodcast(navController = navController)
                                    }

                                    composable(route = Screens.ScreenGame.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenGame(navController = navController)
                                    }
                                    // endregion
                                }

                                if (SettingsNotifier.showLoadingDialog.value) {
                                    DialogLoading(title = stringResource(id = R.string.loading_ad))
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 12) { // it is a sign in request
            try {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                account?.let {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    HelperAuth.auth.signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            HelperSharedPreference.setUsername(account.email!!)
                            navController.navigate(BottomNavScreens.Dashboard.route) // from login screen to home screen
                        }
                    }
                }
            } catch (e: ApiException) {
//                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun askNotificationPermission(requestPermissionLauncher: ActivityResultLauncher<String>) {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            HelperPermissions.requestPermission(
                Manifest.permission.POST_NOTIFICATIONS,
                requestPermissionLauncher
            )
        }
    }

    override fun onStop() {
        super.onStop()

        SettingsNotifier.stopTTS()
    }
}