package com.appsfourlife.draftogo.home.presentation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelFavoriteTemplate
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.home.components.BottomSheetFavoriteTemplates
import com.appsfourlife.draftogo.home.components.ChartPurchasesHistory
import com.appsfourlife.draftogo.home.listitems.FavoriteTemplateItem
import com.appsfourlife.draftogo.home.listitems.UsageItem
import com.appsfourlife.draftogo.home.model.ModelDashboardUsage
import com.appsfourlife.draftogo.home.util.NotifiersHome.listOfFavoriteTemplates
import com.appsfourlife.draftogo.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun ScreenDashboard() {
    val sheetScaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val nbOfWordsLeft = remember {
        mutableStateOf(AnnotatedString(text = "0"))
    }
    val nbOfArtsLeft = remember {
        mutableStateOf(AnnotatedString(text = "0"))
    }

    LaunchedEffect(key1 = true, block = {
        coroutineScope.launch(Dispatchers.IO) {
            // todo uncomment app version check
//            HelperFirebaseDatabase.fetchAppVersion {
//                isAppOutDated.value = it != BuildConfig.VERSION_NAME
//            }

            HelperFirebaseDatabase.fetchNbOfGenerationsConsumedAndNbOfWordsGenerated() {
                nbOfWordsLeft.value = if (HelperAuth.isSubscribed()) {
                    if (HelperSharedPreference.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_BASE) {
                        AnnotatedString(
                            "${Constants.BASE_PLAN_MAX_NB_OF_WORDS - HelperSharedPreference.getNbOfWordsGenerated()}\n",
                            spanStyle = (SpanStyle(fontWeight = FontWeight.Bold))
                        ).plus(
                            AnnotatedString(
                                text = App.getTextFromString(R.string.words)
                            )
                        )
                    } else {
                        AnnotatedString(
                            text = "∞\n",
                            spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
                        ).plus(
                            AnnotatedString(
                                App.getTextFromString(R.string.words)
                            )
                        )
                    }
                } else {
                    val nbOfGenerationsLeft =
                        if (HelperSharedPreference.getNbOfGenerationsConsumed() - Constants.NB_OF_MAX_ALLOWED_GENERATIONS < 0) {
                            0
                        } else {
                            HelperSharedPreference.getNbOfGenerationsConsumed() - Constants.NB_OF_MAX_ALLOWED_GENERATIONS
                        }
                    AnnotatedString(
                        text = "$nbOfGenerationsLeft\n",
                        spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
                    ).plus(
                        AnnotatedString(text = App.getTextFromString(R.string.generations))
                    )
                }
            }

            HelperFirebaseDatabase.getNbOfArtCredits() {
                nbOfArtsLeft.value = AnnotatedString(
                    text = "${HelperSharedPreference.getNbOfArtsCredits()}\n",
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
            HelperFirebaseDatabase.getRenewalDate {
                if (it != "null" && it != "")
                    if (it == HelperDate.getCurrentDateInString()) {
                        HelperFirebaseDatabase.resetNbOfGenerationsConsumedAndNbOfWordsGenerated()
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
                                HelperFirebaseDatabase.resetNbOfGenerationsConsumedAndNbOfWordsGenerated()
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
                        MyText(text = HelperAuth.auth.currentUser?.email!!)
                    }

                    MySpacer(type = "large")

                    Column {
                        MyTextTitle(text = "${stringResource(id = R.string.Usage)}:")

                        MySpacer(type = "small")

                        val listOfUsages = listOf(
                            ModelDashboardUsage(
                                nb = HelperSharedPreference.getNbOfWordsGenerated(),
                                text = stringResource(
                                    id = R.string.words
                                ),
                                iconID = R.drawable.icon_article
                            ),
                            ModelDashboardUsage(
                                nb = HelperSharedPreference.getNbOfArtsGenerated(),
                                text = stringResource(
                                    id = R.string.arts
                                ),
                                iconID = R.drawable.icon_image
                            ),
                            ModelDashboardUsage(
                                nb = listOfFavoriteTemplates.value.size,
                                text = stringResource(id = R.string.templates),
                                iconID = R.drawable.icon_template
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
                                    imageID = current.iconID
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