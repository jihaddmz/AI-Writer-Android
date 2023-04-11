package com.appsfourlife.draftogo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
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
import com.appsfourlife.draftogo.feature_generate_art.presentation.ScreenArt
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelTemplate
import com.appsfourlife.draftogo.feature_generate_text.presentation.*
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.presentation.ScreenFeedback
import com.appsfourlife.draftogo.ui.theme.*
import com.appsfourlife.draftogo.util.BottomNavScreens
import com.appsfourlife.draftogo.util.Screens
import com.appsfourlife.draftogo.util.SettingsNotifier
import com.google.android.gms.ads.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timerTask


class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onStart() {
        super.onStart()

        // Declare the launcher at the top of your Activity/Fragment:
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // TODO: Inform user that that your app will not show notifications.
            }
        }

        Timer().schedule(timerTask {
            askNotificationPermission(requestPermissionLauncher)
        }, 2000)

    }

    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        App.context = this

        MobileAds.initialize(this) {

        }

        if (SettingsNotifier.isConnected.value)
            HelperAds.loadAds {

            }

        setContent {

            navController = rememberNavController()
            val scaffoldState = rememberScaffoldState()
            val coroutineScope = rememberCoroutineScope()
            val navBackStackEntry by navController.currentBackStackEntryAsState()

            LaunchedEffect(key1 = true, block = {
                coroutineScope.launch(Dispatchers.IO) {
                    if (HelperSharedPreference.getBool(HelperSharedPreference.SP_SETTINGS, HelperSharedPreference.SP_SETTINGS_IS_FIRST_TIME_V120_LAUNCHED, true)) {
                        App.dbGenerateText.daoTemplates.getAllTemplates().forEach {
                            App.dbGenerateText.daoTemplates.deleteTemplate(it)
                        }
                        HelperSharedPreference.setBool(HelperSharedPreference.SP_SETTINGS, HelperSharedPreference.SP_SETTINGS_IS_FIRST_TIME_V120_LAUNCHED, false)
                    }
                    Constants.PREDEFINED_TEMPLATES.forEach { template ->
                        if (App.dbGenerateText.daoTemplates.getTemplateByQuery(
                                template
                            ) == null
                        ) {
                            App.dbGenerateText.daoTemplates.insertTemplate(
                                ModelTemplate(template, "", 1)
                            )
                        } else
                            return@forEach
                    }
                    SettingsNotifier.predefinedTemplates.value =
                        App.dbGenerateText.daoTemplates.getAllTemplates() as MutableList<ModelTemplate>
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

                                DrawerListItem(
                                    modifier = Modifier.padding(SpacersSize.medium),
                                    text = text,
                                    imageUrl = imageUrl
                                ) {

                                    SettingsNotifier.resetValues() // clearing values

                                    when (text) {
                                        App.getTextFromString(R.string.write_an_email) -> {
                                            navController.navigate(Screens.ScreenEmail.route)
                                        }
                                        App.getTextFromString(R.string.write_a_blog) -> {
                                            navController.navigate(Screens.ScreenBlog.route)
                                        }
                                        App.getTextFromString(R.string.write_an_essay) -> {
                                            navController.navigate(Screens.ScreenEssay.route)
                                        }
                                        App.getTextFromString(R.string.write_an_article) -> {
                                            navController.navigate(Screens.ScreenArticle.route)
                                        }
                                        App.getTextFromString(R.string.write_a_letter) -> {
                                            navController.navigate(Screens.ScreenLetter.route)
                                        }
                                        App.getTextFromString(R.string.write_a_cv) -> {
                                            navController.navigate(Screens.ScreenCV.route)
                                        }
                                        App.getTextFromString(R.string.write_a_resume) -> {
                                            navController.navigate(Screens.ScreenResume.route)
                                        }
                                        App.getTextFromString(R.string.write_a_personal_bio) -> {
                                            navController.navigate(Screens.ScreenPersonalBio.route)
                                        }
                                        App.getTextFromString(R.string.write_a_tweet) -> {
                                            navController.navigate(Screens.ScreenTwitter.route)
                                        }
                                        App.getTextFromString(R.string.write_a_viral_tiktok_captions) -> {
                                            navController.navigate(Screens.ScreenTiktok.route)
                                        }
                                        App.getTextFromString(R.string.write_an_instagram_caption) -> {
                                            navController.navigate(Screens.ScreenInstagram.route)
                                        }

                                        App.getTextFromString(R.string.write_a_facebook_post) -> {
                                            navController.navigate(Screens.ScreenFacebook.route)
                                        }

                                        App.getTextFromString(R.string.write_a_linkedin_post) -> {
                                            navController.navigate(Screens.ScreenLinkedIn.route)
                                        }

                                        App.getTextFromString(R.string.write_a_youtube_caption) -> {
                                            navController.navigate(Screens.ScreenYoutube.route)
                                        }

                                        App.getTextFromString(R.string.write_a_podcast_top_bar) -> {
                                            navController.navigate(Screens.ScreenPodcast.route)
                                        }

                                        App.getTextFromString(R.string.write_a_game_script_top_label) -> {
                                            navController.navigate(Screens.ScreenGame.route)
                                        }

                                        App.getTextFromString(R.string.write_a_poem) -> {
                                            navController.navigate(Screens.ScreenPoem.route)
                                        }

                                        App.getTextFromString(R.string.write_a_song) -> {
                                            navController.navigate(Screens.ScreenSong.route)
                                        }
                                        App.getTextFromString(R.string.write_a_code) -> {
                                            navController.navigate(Screens.ScreenCode.route)
                                        }
                                        App.getTextFromString(R.string.custom) -> {
                                            navController.navigate(Screens.ScreenCustom.route)
                                        }
                                        App.getTextFromString(R.string.summarize_the_following_text) -> {
                                            navController.navigate(Screens.ScreenSummarize.route)
                                        }
                                        App.getTextFromString(R.string.correct_the_following_text) -> {
                                            navController.navigate(Screens.ScreenGrammar.route)
                                        }
                                        App.getTextFromString(R.string.translate_the_following_text) -> {
                                            navController.navigate(Screens.ScreenTranslate.route)
                                        }
                                        else -> {
                                            SettingsNotifier.currentUserQuerySection = text
                                            navController.navigate(Screens.ScreenUserAddedTemplate.route)
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
                    },
                    drawerShape = DrawerShape,
                    bottomBar = {
                        val changeTargetValue = remember {
                            mutableStateOf(false)
                        }

                        val shouldBottomBarBeVisible =
                            (navBackStackEntry?.destination?.route == BottomNavScreens.Home.route
                                    || navBackStackEntry?.destination?.route == BottomNavScreens.History.route
                                    || navBackStackEntry?.destination?.route == BottomNavScreens.Settings.route
                                    || navBackStackEntry?.destination?.route == BottomNavScreens.Feedback.route
                                    || navBackStackEntry?.destination?.route == BottomNavScreens.Art.route)

                        LaunchedEffect(key1 = shouldBottomBarBeVisible, block = {
                            if (shouldBottomBarBeVisible) {
                                delay(Constants.SPLASH_SCREEN_DURATION + 200L)
                                changeTargetValue.value = true
                            } else {
                                changeTargetValue.value = false
                            }
                        })

                        if (changeTargetValue.value) {
                            val listOfBottomNavScreens =
                                listOf(
                                    BottomNavScreens.Home,
                                    BottomNavScreens.Art,
                                    BottomNavScreens.Feedback,
                                    BottomNavScreens.Settings
                                )
                            BottomNavigation(
                                modifier = Modifier.animateOffsetY(
                                    initialOffsetY = 70.dp,
                                ), backgroundColor = Glass
                            ) {
                                val currentRoute = navBackStackEntry?.destination?.route

                                listOfBottomNavScreens.forEach { screen ->
                                    BottomNavigationItem(
                                        selected = currentRoute == screen.route,
                                        onClick = {
                                            SettingsNotifier.resetValues()

                                            if (currentRoute != screen.route) {
                                                if (screen.route == BottomNavScreens.History.route) {
                                                    HelperAnalytics.sendEvent("history")
                                                    // if there is network access, navigate to history
                                                    if (SettingsNotifier.isConnected.value) {
                                                        navController.navigate(BottomNavScreens.History.route)
                                                    } else {
                                                        HelperUI.showToast(
                                                            msg = App.getTextFromString(
                                                                R.string.no_connection
                                                            )
                                                        )
                                                    }
                                                } else
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

                            if (SettingsNotifier.basePlanMaxNbOfWordsExceeded.value) {
                                DialogSubscriptionNbOfWordsExceeded(SettingsNotifier.basePlanMaxNbOfWordsExceeded)
                            }

                            if (SettingsNotifier.showDialogNbOfGenerationsLeftExceeded.value)
                                DialogSubscription(SettingsNotifier.showDialogNbOfGenerationsLeftExceeded)

                            /**
                             * if the android version is equal or greater than 12, remove the custom splash screen
                             * and check if the user should be navigated directly to the sign in or home screen
                             **/
                            /**
                             * if the android version is equal or greater than 12, remove the custom splash screen
                             * and check if the user should be navigated directly to the sign in or home screen
                             **/
                            val startScreenRoute = if (Build.VERSION.SDK_INT >= 31) {
                                setSpacersSize()
                                if (HelperSharedPreference.getUsername() == "") {
                                    Screens.ScreenSignIn.route
                                } else {
                                    BottomNavScreens.Home.route
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

                                    composable(route = BottomNavScreens.History.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenHistory(
                                            modifier = Modifier,
                                            navController = navController
                                        )
                                    }

                                    composable(route = BottomNavScreens.Feedback.route) {
                                        MyBackHandler(navController = navController)
                                        ScreenFeedback(navController = navController)
                                    }

                                    composable(route = Screens.ScreenSignIn.route) {
                                        HomeBackHandler(context = this@MainActivity)
                                        ScreenSignIn(
                                        )
                                    }

                                    composable(route = BottomNavScreens.Home.route) {
                                        HomeBackHandler(context = this@MainActivity)
                                        ScreenHome(
                                            modifier = Modifier,
                                            navController = navController
                                        )
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
//                }
            }
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
                            navController.navigate(BottomNavScreens.Home.route) // from login screen to home screen
                        }
                    }
                }
            } catch (e: ApiException) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
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
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
//                PackageManager.PERMISSION_GRANTED
//            ) {
//                // FCM SDK (and your app) can post notifications.
//            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
//                // TODO: display an educational UI explaining to the user the features that will be enabled
//                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
//                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
//                //       If the user selects "No thanks," allow the user to continue without notifications.
//            } else {
//                // Directly ask for the permission
//                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
//            }
//        }
        }
    }

    override fun onStop() {
        super.onStop()

        SettingsNotifier.stopTTS()
    }
}