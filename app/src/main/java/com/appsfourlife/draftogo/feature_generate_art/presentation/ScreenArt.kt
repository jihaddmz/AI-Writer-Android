package com.appsfourlife.draftogo.feature_generate_art.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.extensions.animateOffsetX
import com.appsfourlife.draftogo.extensions.animateVisibility
import com.appsfourlife.draftogo.feature_generate_art.components.BottomSheetArtPricing
import com.appsfourlife.draftogo.feature_generate_art.data.model.ModelArtHistory
import com.appsfourlife.draftogo.feature_generate_art.notifiers.NotifiersArt
import com.appsfourlife.draftogo.feature_generate_art.util.ConstantsArt
import com.appsfourlife.draftogo.feature_generate_art.util.ConstantsArt.LISTOF_ART_SHOWCASES
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Orange
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.SettingsNotifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import java.util.*
import kotlin.concurrent.timerTask

@OptIn(ExperimentalPagerApi::class, ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun ScreenArt(
    navController: NavController
) {

    val localKeyboard = LocalSoftwareKeyboardController.current
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val sheetScaffoldState = rememberBottomSheetScaffoldState()

    HelperFirebaseDatabase.getNbOfArtCredits()

    LaunchedEffect(key1 = true, block = {
        Timer().scheduleAtFixedRate(timerTask {
            coroutineScope.launch {
                val secondPage =
                    if (pagerState.currentPage == LISTOF_ART_SHOWCASES.size - 1) 0 else pagerState.currentPage + 1
                pagerState.animateScrollToPage(
                    secondPage,
                )
            }
        }, 1000L, 5000L)
    })

    TopBarArt(
        modifier = Modifier,
        text = stringResource(id = R.string.generate_arts),
        navController = navController,
    ) {

        val prompt = remember {
            mutableStateOf("")
        }
        val imageUrl = remember {
            mutableStateOf("")
        }
        val resolution = remember {
            mutableStateOf("")
        }
        val style = remember {
            mutableStateOf("")
        }
        val showLoadingIndicator = remember {
            mutableStateOf(false)
        }
        val bitmap = remember {
            mutableStateOf<Bitmap?>(null)
        }
        val doneIconColor = remember {
            mutableStateOf(Color.LightGray)
        }
        val isBottomSheetArtHistory = remember {
            mutableStateOf(true)
        }

        BottomSheet(modifier = Modifier
            .fillMaxSize(),
            sheetScaffoldState = sheetScaffoldState,
            bottomSheet = {
                if (isBottomSheetArtHistory.value) {
                    BottomSheetArtHistory(
                        sheetScaffoldState = sheetScaffoldState,
                        onArtHistoryClick = {
                            prompt.value = it
                            doneIconColor.value = Color.Green
                        }
                    )
                } else {
                    BottomSheetArtPricing(sheetScaffoldState = sheetScaffoldState)
                }
            }) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = SpacersSize.medium), contentAlignment = Alignment.CenterEnd
                ) {
                    Row(modifier = Modifier.clickable {
                        prompt.value =
                            ConstantsArt.LISTOF_ART_SHOWCASES[Random().nextInt(ConstantsArt.LISTOF_ART_SHOWCASES.size)].bio
                        doneIconColor.value = Color.Green
                    }, verticalAlignment = Alignment.CenterVertically) {
                        MyTextLink(
                            text = stringResource(id = R.string.i_need_inspiration),
                            modifier = Modifier
                        )
                        MyIcon(iconID = R.drawable.icon_light, contentDesc = "", tint = Orange)
                    }
                }
                MyCardView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SpacersSize.medium),
                ) {
                    Column {
                        MyTextField(
                            modifier = Modifier, value = prompt.value,
                            onValueChanged = {
                                prompt.value = it
                                if (it.isNotEmpty())
                                    doneIconColor.value = Color.Green
                                else
                                    doneIconColor.value = Color.LightGray
                            },
                            placeholder = stringResource(id = R.string.generate_art_textfield_label),
                        )

                        MySpacer(type = "small")

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(
                                modifier = Modifier.animateOffsetX(initialOffsetX = (-70).dp),
                                onClick = {
                                    coroutineScope.launch {
                                        isBottomSheetArtHistory.value = true
                                        sheetScaffoldState.bottomSheetState.expand()
                                    }
                                }) {
                                MyIcon(
                                    iconID = R.drawable.icon_history,
                                    contentDesc = "",
                                    tint = Color.DarkGray
                                )
                            }

                            IconButton(modifier = Modifier.animateVisibility(), onClick = {
                                prompt.value = ""
                                doneIconColor.value = Color.LightGray
                            }) {
                                MyIcon(
                                    iconID = R.drawable.clear,
                                    contentDesc = "",
                                    tint = Color.Red
                                )
                            }

                            IconButton(modifier = Modifier
                                .padding(end = SpacersSize.small)
                                .animateOffsetX(initialOffsetX = 70.dp),
                                enabled = doneIconColor.value == Color.Green,
                                onClick = {
                                    HelperAnalytics.sendEvent("generate_art")

                                    if (!SettingsNotifier.isConnected.value) {
                                        HelperUI.showToast(msg = App.getTextFromString(R.string.no_connection))
                                        return@IconButton
                                    }

                                    if (NotifiersArt.credits.value == 0) {
                                        coroutineScope.launch {
                                            isBottomSheetArtHistory.value = false
                                            sheetScaffoldState.bottomSheetState.expand()
                                        }
                                        return@IconButton
                                    }

//                                    imageUrl.value = ""
                                    localKeyboard?.hide()
                                    showLoadingIndicator.value = true

//                                    coroutineScope.launch {
//                                        Timer().schedule(timerTask {
//                                            showLoadingIndicator.value = false
//                                            imageUrl.value = "12"
//                                            NotifiersArt.credits.value -= 1
//                                            HelperFirebaseDatabase.setNbOfArtCredits()
//                                        }, 2000)
//                                    }
                                    val finalPrompt =
                                        if (style.value == App.getTextFromString(R.string.none)) prompt.value else "${prompt.value} with a $style style"
                                    HelperChatGPT.getImageResponse(
                                        size = resolution.value,
                                        prompt = finalPrompt,
                                    ) {
                                        showLoadingIndicator.value = false
                                        imageUrl.value = it
                                        coroutineScope.launch(Dispatchers.IO) {
                                            NotifiersArt.credits.value -= 1
                                            HelperSharedPreference.setNbOfArtsCredits()
                                            HelperSharedPreference.incrementNbOfArtsGenerated()
                                            HelperFirebaseDatabase.setNbOfArtCredits()
                                            bitmap.value = BitmapFactory.decodeStream(
                                                URL(it)
                                                    .openConnection()
                                                    .getInputStream()
                                            )
                                        }
                                    }
                                }) {
                                if (!showLoadingIndicator.value)
                                    MyIcon(
                                        iconID = R.drawable.icon_checkcircle,
                                        contentDesc = "",
                                        tint = doneIconColor.value
                                    )
                                MyAnimatedVisibility(visible = showLoadingIndicator.value) {
                                    MyIconLoading(tint = Color.Green)
                                }
                            } // end done icon
                        }
                    }
                } // end input card
                MyTipText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SpacersSize.medium),
                    text = stringResource(id = R.string.generate_art_tip)
                )

                MySpacer(type = "large")

                resolution.value = myOptionsSelector(
                    list = listOf("256x256", "512x512", "1024x1024"),
                    defaultSelectedText = "512x512"
                )
                MyTipText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SpacersSize.medium),
                    text = stringResource(id = R.string.art_res_tip)
                )

                MySpacer(type = "medium")

                style.value = myDropDown(
                    modifier = Modifier.padding(horizontal = SpacersSize.medium),
                    list = ConstantsArt.LISTOF_ART_STYLES,
                    label = stringResource(id = R.string.style)
                )

                MySpacer(type = "large")

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth(),
                    count = LISTOF_ART_SHOWCASES.size
                ) { index ->
                    val current = LISTOF_ART_SHOWCASES[index]

                    Card(shape = Shapes.large, modifier = Modifier.padding(SpacersSize.medium)) {

                        Box(modifier = Modifier.fillMaxSize()) {
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                painter = painterResource(id = current.artID),
                                contentDescription = "",
                                contentScale = ContentScale.Fit
                            )
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Color.Transparent, Color.Black)
                                            )
                                        ),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Bottom
                                ) {
                                    MyText(
                                        text = current.bio,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                    MySpacer(type = "small")
                                    HorizontalPagerIndicator(
                                        pagerState = pagerState,
                                        activeColor = Blue,
                                        inactiveColor = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                val showDialog = remember {
                    mutableStateOf(false)
                }
                LaunchedEffect(key1 = imageUrl.value, block = {
                    showDialog.value = imageUrl.value.isNotEmpty()
                })

                if (showDialog.value) {
                    MyCustomConfirmationDialog(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.75f),
                        showDialog = showDialog,
                        negativeBtnText = stringResource(id = R.string.done),
                        positiveBtnText = stringResource(id = R.string.save_to_history),
                        onPositiveBtnClick = {
                            coroutineScope.launch(Dispatchers.IO) {
                                if (App.databaseApp.daoApp.getArt(prompt.value) == null) {
                                    App.databaseApp.daoApp.insertArt(
                                        ModelArtHistory(
                                            prompt.value,
                                            imageUrl.value,
                                            HelperDate.parseStringToDate(
                                                HelperDate.getCurrentDateWithSec(),
                                                "yyyy-MM-dd hh:mm:ss"
                                            )!!.time
                                        )
                                    )
                                } else {
                                    App.databaseApp.daoApp.updateArt(
                                        ModelArtHistory(
                                            prompt.value,
                                            imageUrl.value,
                                            HelperDate.parseStringToDate(
                                                HelperDate.getCurrentDateWithSec(),
                                                "yyyy-MM-dd hh:mm:ss"
                                            )!!.time
                                        )
                                    )
                                }
                                NotifiersArt.listOfPromptHistory.value =
                                    App.databaseApp.daoApp.getAllArts().sortedBy { it.dateTime }
                                NotifiersArt.listOfPromptHistory.value =
                                    NotifiersArt.listOfPromptHistory.value.reversed()
                            }
                        }
                    ) {
                        MyTipText(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = stringResource(id = R.string.tip_art_download_image)
                        )
//                        Image(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .fillMaxHeight(0.85f),
//                            painter = painterResource(id = R.drawable.test),
//                            contentDescription = ""
//                        )
                        MyUrlImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.85f)
                                .clickable {
                                    coroutineScope.launch(Dispatchers.IO) {
                                        if (bitmap.value != null) {
                                            HelperImage.mSaveMediaToStorage(
                                                bitmap.value
                                            ) {
                                                coroutineScope.launch(Dispatchers.Main) {
                                                    HelperUI.showToast(
                                                        App.context,
                                                        msg = App.getTextFromString(R.string.image_saved_to_gallery)
                                                    )
                                                }
                                            }
                                        } else {
                                            coroutineScope.launch(Dispatchers.IO) {
                                                bitmap.value = BitmapFactory.decodeStream(
                                                    URL(imageUrl.value)
                                                        .openConnection()
                                                        .getInputStream()
                                                )
                                                HelperImage.mSaveMediaToStorage(
                                                    bitmap.value
                                                ) {
                                                    coroutineScope.launch(Dispatchers.Main) {
                                                        HelperUI.showToast(
                                                            App.context,
                                                            msg = App.getTextFromString(R.string.image_saved_to_gallery)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                },
                            imageUrl = imageUrl.value,
                            contentDesc = "",
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TopBarArt(
    modifier: Modifier = Modifier,
    navController: NavController,
    text: String,
    content: @Composable () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Blue)
                .padding(SpacersSize.small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            MyTextTitle(text = text, color = Color.White)

            Row(verticalAlignment = Alignment.CenterVertically) {

                MyText(
                    modifier = Modifier
                        .padding(end = SpacersSize.small),
                    color = Color.White,
                    text = stringResource(
                        id = R.string.nbof_credits,
                        NotifiersArt.credits.value
                    ),
                    textAlign = TextAlign.End
                )
                MyIcon(iconID = R.drawable.icon_star, contentDesc = "", tint = Color.Yellow)
            }
        }

        Spacer(modifier = Modifier.height(SpacersSize.small))

        content()
    }
}