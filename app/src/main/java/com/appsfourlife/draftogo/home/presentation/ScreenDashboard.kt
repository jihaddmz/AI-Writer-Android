package com.appsfourlife.draftogo.home.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.BuildConfig
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.data.model.ModelFavoriteTemplate
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.home.components.BottomSheetFavoriteTemplates
import com.appsfourlife.draftogo.home.components.ChartPurchasesHistory
import com.appsfourlife.draftogo.home.listitems.FavoriteTemplateItem
import com.appsfourlife.draftogo.home.listitems.UsageItem
import com.appsfourlife.draftogo.home.model.ModelDashboardUsage
import com.appsfourlife.draftogo.home.util.NotifiersHome.listOfFavoriteTemplates
import com.appsfourlife.draftogo.ui.theme.*
import com.appsfourlife.draftogo.util.BottomNavScreens
import com.appsfourlife.draftogo.util.SettingsNotifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun ScreenDashboard(navController: NavController, scaffoldState: ScaffoldState) {
    val sheetScaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val nbOfWordsLeft = remember {
        mutableStateOf(AnnotatedString(text = "0"))
    }
    val nbOfArtsLeft = remember {
        mutableStateOf(AnnotatedString(text = "0"))
    }
    val isAppOutDated = remember {
        mutableStateOf(false)
    }

    /**
     * showing the navigation drawer introduction
     **/
    val showNavigationDrawerIntroduction = remember {
        mutableStateOf(
            false
        )
    }

    if (showNavigationDrawerIntroduction.value) {
        MyDialog(
            showDialog = showNavigationDrawerIntroduction,
            text = App.getTextFromString(textID = R.string.introducing_navigation_changed),
            title = App.getTextFromString(textID = R.string.attention),
            showOkBtn = true,
            onOkBtnClick = {
                HelperSharedPreference.setBool(
                    HelperSharedPreference.SP_SETTINGS,
                    HelperSharedPreference.SP_SETTINGS_IS_FIRST_TIME_V230_LAUNCHED,
                    false
                )
            }
        )
    }

    LaunchedEffect(key1 = true, block = {
        val isFirstTimeV230Launched = HelperSharedPreference.getBool(
            HelperSharedPreference.SP_SETTINGS,
            HelperSharedPreference.SP_SETTINGS_IS_FIRST_TIME_V230_LAUNCHED,
            true
        )

        if (isFirstTimeV230Launched) {
            coroutineScope.launch {
                delay(2000)
                isAppOutDated.value = false
                scaffoldState.drawerState.animateTo(
                    DrawerValue.Open,
                    tween(durationMillis = 1000)
                )
                delay(300)
                showNavigationDrawerIntroduction.value = true
            }
        }
    })
    /**
     * end of showing the navigation drawer introduction
     **/


    /**
     * Subscribing users to email sender
     **/
    if (SettingsNotifier.isConnected.value && !HelperSharedPreference.getBool(
            HelperSharedPreference.SP_SETTINGS,
            HelperSharedPreference.SP_SETTINGS_SUBSCRIBED_TO_EMAIL_SENDER,
            false
        )
    )
        LaunchedEffect(key1 = true, block = {
            coroutineScope.launch(Dispatchers.IO) {
                HelperAPISender.subscribeUser(HelperAuth.auth.currentUser!!)
            }
        })
    /**
     * end of Subscribing users to email sender
     **/


    LaunchedEffect(key1 = true, block = {
        coroutineScope.launch(Dispatchers.IO) {
            if (SettingsNotifier.isConnected.value)
                HelperFirebaseDatabase.fetchAppVersion {
                    isAppOutDated.value = it != BuildConfig.VERSION_NAME
                }

            if (SettingsNotifier.isConnected.value)
                HelperFirebaseDatabase.fetchNbOfGenerationsConsumedAndNbOfWordsGenerated() {
                    // todo uncomment this when we want to re-enable the paid plans
                    nbOfWordsLeft.value =
//                        if (HelperAuth.isSubscribed()) {
//                            if (HelperSharedPreference.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_BASE) {
//                                AnnotatedString(
//                                    "${Constants.BASE_PLAN_MAX_NB_OF_WORDS - HelperSharedPreference.getNbOfWordsGenerated()}\n",
//                                    spanStyle = (SpanStyle(fontWeight = FontWeight.Bold))
//                                ).plus(
//                                    AnnotatedString(
//                                        text = App.getTextFromString(R.string.words)
//                                    )
//                                )
//                            } else {
                        AnnotatedString(
                            text = "∞\n",
                            spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
                        ).plus(
                            AnnotatedString(
                                App.getTextFromString(R.string.words)
                            )
                        )
//                            }
//                        } else {
//                            val nbOfGenerationsLeft =
//                                if (Constants.NB_OF_MAX_ALLOWED_GENERATIONS - HelperSharedPreference.getNbOfGenerationsConsumed() < 0) {
//                                    0
//                                } else {
//                                    Constants.NB_OF_MAX_ALLOWED_GENERATIONS - HelperSharedPreference.getNbOfGenerationsConsumed()
//                                }
//                            AnnotatedString(
//                                text = "$nbOfGenerationsLeft\n",
//                                spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
//                            ).plus(
//                                AnnotatedString(text = App.getTextFromString(R.string.generations))
//                            )
//                        }
                }

            if (SettingsNotifier.isConnected.value)
                HelperFirebaseDatabase.getNbOfArtCredits() {
                    nbOfArtsLeft.value = AnnotatedString(
                        text = "∞\n", // todo change this to HelperSharedPreference.getNbOfArtsCredits() when paid plans are enabled
                        spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
                    ).plus(
                        AnnotatedString(
                            text = App.getTextFromString(R.string.arts)
                        )
                    )
                }

            listOfFavoriteTemplates.value = App.databaseApp.daoApp.getAllFavoriteTemplates()

            // if the user is on base plan subscription, check if we are currently on the same renewal date, if so
            // reset all values on firebase and set the new renewal date, if no, check if currently we are after the
            // renewal date on firebase, if so reset the value on firebase and set the new renewal date
            if (SettingsNotifier.isConnected.value)
                HelperFirebaseDatabase.getRenewalDate {
                    if (it != "null" && it != "")
                        if (it == HelperDate.getCurrentDateInString()) {
                            HelperFirebaseDatabase.resetNbOfGenerationsConsumedAndNbOfWordsGeneratedAndNbOfArtsGenerated()
                            HelperSharedPreference.setNbOfArtsGenerated(0)
                            HelperFirebaseDatabase.setRenewalDate()
                        } else {
                            val dateInFirebase =
                                HelperDate.parseStringToDate(it, Constants.DAY_MONTH_YEAR_FORMAT)
                            val dateNow = HelperDate.parseStringToDate(
                                HelperDate.parseDateToString(
                                    Date(),
                                    Constants.DAY_MONTH_YEAR_FORMAT
                                ), Constants.DAY_MONTH_YEAR_FORMAT
                            )
                            dateNow?.let { dateNow ->
                                if (dateNow.after(dateInFirebase)) {
                                    HelperFirebaseDatabase.resetNbOfGenerationsConsumedAndNbOfWordsGeneratedAndNbOfArtsGenerated()
                                    HelperSharedPreference.setNbOfArtsGenerated(0)
                                    HelperFirebaseDatabase.setRenewalDate()
                                }
                            }
                        }
                }
        }
    })

    BottomSheet(sheetScaffoldState = sheetScaffoldState, bottomSheet = {
        BottomSheetFavoriteTemplates()
    }) {

        /**
         * show reward dialog if there is a text reward in firebase for this particular user
         **/
        val rewardedDialogText = remember {
            mutableStateOf("")
        }
        val showRewardDialog = remember {
            mutableStateOf(false)
        }
        LaunchedEffect(key1 = true, block = {
            coroutineScope.launch(Dispatchers.IO) {
                delay(1000)
                HelperFirebaseDatabase.getRewardText {
                    if (!it.isNullOrEmpty() && it != "null") {
                        rewardedDialogText.value = it
                        showRewardDialog.value = true
                    }
                }
            }
        })
        if (showRewardDialog.value) {
            DialogReward(text = rewardedDialogText.value, showDialog = showRewardDialog)
        }


        if (isAppOutDated.value) // if the app is outdated show the alert dialog to update
            MyDialog(
                modifier = Modifier,
                showDialog = isAppOutDated,
                text = stringResource(id = R.string.app_is_outdated),
                title = stringResource(id = R.string.attention),
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                )
            )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpacersSize.small)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.35f)
                    .weight(0.4f)
                    .background(color = Glass, shape = Shapes.medium)
                    .padding(SpacersSize.small)
            ) {

                Column(verticalArrangement = Arrangement.SpaceBetween) {
                    Column() {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MyUrlImage(
                                modifier = Modifier.clip(CircleShape),
                                imageUrl = HelperAuth.auth.currentUser?.photoUrl.toString(),
                                contentDesc = ""
                            )

                            MyAnnotatedText(
                                text = nbOfWordsLeft.value,
                                textAlign = TextAlign.Center
                            )
                            MyText(text = "|", color = Color.Black)
                            MyAnnotatedText(text = nbOfArtsLeft.value, textAlign = TextAlign.Center)
                        }
                        MyText(text = HelperAuth.auth.currentUser?.displayName!!)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MyText(text = HelperAuth.auth.currentUser?.email!!)
                            MyIcon(iconID = R.drawable.icon_settings, contentDesc = stringResource(
                                id = R.string.settings
                            ), modifier = Modifier.clickable {
                                navController.navigate(BottomNavScreens.Settings.route)
                            })

                        }
                    }

                    MySpacer(type = "large")

                    Column {
                        MyTextTitle(text = "${stringResource(id = R.string.monthly_usage)}:")

                        MySpacer(type = "small")

                        val listOfUsages = listOf(
                            ModelDashboardUsage(
                                nb = HelperSharedPreference.getNbOfChatWordsGenerated(),
                                text = stringResource(
                                    id = R.string.chat
                                ),
                                iconID = R.drawable.icon_chat_green
                            ),
                            ModelDashboardUsage(
                                nb = HelperSharedPreference.getNbOfArtsGenerated(),
                                text = stringResource(
                                    id = R.string.arts
                                ),
                                iconID = R.drawable.icon_image
                            ),
                            ModelDashboardUsage(
                                nb = HelperSharedPreference.getNbOfCompletionWordsGenerated(),
                                text = stringResource(id = R.string.completion),
                                iconID = R.drawable.icon_template
                            ),
                            ModelDashboardUsage(
                                nb = HelperSharedPreference.getNbOfVideosGenerated(),
                                text = stringResource(id = R.string.videos),
                                iconID = R.drawable.icon_video_pink
                            )
                        )
                        LazyVerticalGrid(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            columns = GridCells.Fixed(2)
                        ) {
                            items(listOfUsages.size) { index ->
                                val current = listOfUsages[index]
                                UsageItem(
                                    nb = current.nb,
                                    text = current.text,
                                    imageID = current.iconID,
                                    navController = navController
                                )
                            }
                        }

                    }
                }
            } // end of first section

            MySpacer(type = "small")

            /**
             * second section of the screen
             **/
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
                    .weight(0.2f)
                    .background(color = Azure, shape = Shapes.medium)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(SpacersSize.small)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MyTextTitle(
                            text = stringResource(id = R.string.favorite_templates),
                            color = Color.White
                        )
                        MyTextLink(
                            text = stringResource(id = R.string.view_all),
                            color = Color.White,
                            modifier = Modifier.clickable {
                                coroutineScope.launch {
                                    sheetScaffoldState.bottomSheetState.animateTo(BottomSheetValue.Expanded)
                                }
                            })
                    }

                    LazyRow(content = {
                        items(
                            listOfFavoriteTemplates.value.size,
                            key = { listOfFavoriteTemplates.value[it].query }) { index ->
                            val current = listOfFavoriteTemplates.value[index]

                            FavoriteTemplateItem(
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .animateItemPlacement(),
                                imageID = current.iconID ?: current.imageUrl!!,
                                text = current.query
                            ) {
                                coroutineScope.launch(Dispatchers.IO) {
                                    if (current.imageUrl.isNullOrEmpty())
                                        App.databaseApp.daoApp.deleteFavoriteTemplate(
                                            ModelFavoriteTemplate(
                                                current.query,
                                                current.iconID,
                                                null
                                            )
                                        )
                                    else
                                        App.databaseApp.daoApp.deleteFavoriteTemplate(
                                            ModelFavoriteTemplate(
                                                current.query,
                                                null,
                                                current.imageUrl
                                            )
                                        )

                                    listOfFavoriteTemplates.value =
                                        App.databaseApp.daoApp.getAllFavoriteTemplates()

                                }
                            }

                            MySpacer(type = "small", widthOrHeight = "width")
                        }
                    })
                }
            }

            MySpacer(type = "small")

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.35f)
                    .weight(0.4f)
                    .background(color = Amber, shape = Shapes.medium)
                    .padding(SpacersSize.small)
            ) {
                ChartPurchasesHistory()
            }
        }
    }
}