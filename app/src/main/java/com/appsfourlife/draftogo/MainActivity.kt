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
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.billingclient.api.*
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.feature_generate_text.presentation.*
import com.appsfourlife.draftogo.util.Screens
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.ui.theme.*
import com.appsfourlife.draftogo.util.SettingsNotifier
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.GoogleAuthProvider
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

        setContent {

            navController = rememberNavController()
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

                                    SettingsNotifier.resetValues() // clearing values

                                    when (text) {
                                        App.getTextFromString(R.string.write_an_email) -> {
                                            navController.navigate(Screens.ScreenEmail.route)
                                        }
                                        App.getTextFromString(R.string.write_a_blog_top_bar) -> {
                                            navController.navigate(Screens.ScreenBlog.route)
                                        }
                                        App.getTextFromString(R.string.write_an_essay) -> {
                                            navController.navigate(Screens.ScreenEssay.route)
                                        }
                                        App.getTextFromString(R.string.write_an_article_top_bar) -> {
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
                                        App.getTextFromString(R.string.write_a_personal_bio_top_bar) -> {
                                            navController.navigate(Screens.ScreenPersonalBio.route)
                                        }
                                        App.getTextFromString(R.string.write_a_tweet_top_bar) -> {
                                            navController.navigate(Screens.ScreenTwitter.route)
                                        }
                                        App.getTextFromString(R.string.write_a_viral_tiktok_captions_top_bar) -> {
                                            navController.navigate(Screens.ScreenTiktok.route)
                                        }
                                        App.getTextFromString(R.string.write_an_instagram_caption_top_bar) -> {
                                            navController.navigate(Screens.ScreenInstagram.route)
                                        }

                                        App.getTextFromString(R.string.write_a_facebook_post_top_bar) -> {
                                            navController.navigate(Screens.ScreenFacebook.route)
                                        }

                                        App.getTextFromString(R.string.write_a_youtube_caption_top_bar) -> {
                                            navController.navigate(Screens.ScreenYoutube.route)
                                        }

                                        App.getTextFromString(R.string.write_a_podcast_top_bar) -> {
                                            navController.navigate(Screens.ScreenPodcast.route)
                                        }

                                        App.getTextFromString(R.string.write_a_game_script_top_label) -> {
                                            navController.navigate(Screens.ScreenGame.route)
                                        }

                                        App.getTextFromString(R.string.write_a_poem_top_bar) -> {
                                            navController.navigate(Screens.ScreenPoem.route)
                                        }

                                        App.getTextFromString(R.string.write_a_song_top_bar) -> {
                                            navController.navigate(Screens.ScreenSong.route)
                                        }
                                        App.getTextFromString(R.string.write_a_code) -> {
                                            navController.navigate(Screens.ScreenCode.route)
                                        }
                                        App.getTextFromString(R.string.custom) -> {
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
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {

                            if (SettingsNotifier.basePlanMaxNbOfWordsExceeded.value) {
                                // todo show base plan monthly max nb of words generations exceeded
                            }

                            if (SettingsNotifier.showDialogNbOfGenerationsLeftExceeded.value)
                                DialogSubscription(SettingsNotifier.showDialogNbOfGenerationsLeftExceeded)

                            NavHost(
                                navController = navController,
                                startDestination = Screens.ScreenLaunch.route
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

                                composable(route = Screens.ScreenSignIn.route) {
                                    HomeBackHandler(context = this@MainActivity)
                                    ScreenSignIn(
                                    )
                                }

                                composable(route = Screens.ScreenHome.route) {
                                    HomeBackHandler(context = this@MainActivity)
                                    ScreenHome(
                                        modifier = Modifier,
                                        navController = navController
                                    )
                                }

                                composable(route = Screens.ScreenArticle.route) {
                                    MyBackHandler(navController = navController)
                                    ScreenArticle(navController = navController)
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

                                composable(route = Screens.ScreenSong.route) {
                                    MyBackHandler(navController = navController)
                                    ScreenSong(navController = navController)
                                }

                                composable(route = Screens.ScreenSettings.route) {
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
                        }
                    }
                }
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
                            navController.navigate(Screens.ScreenHome.route) // from login screen to home screen
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
}