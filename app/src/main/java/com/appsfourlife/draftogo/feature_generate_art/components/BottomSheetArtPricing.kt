package com.appsfourlife.draftogo.feature_generate_art.components

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.feature_generate_art.notifiers.NotifiersArt
import com.appsfourlife.draftogo.ui.theme.Glass
import com.appsfourlife.draftogo.ui.theme.Orange
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.getOfferingsWith
import com.revenuecat.purchases.interfaces.PurchaseCallback
import com.revenuecat.purchases.models.StoreTransaction
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetArtPricing(
    modifier: Modifier = Modifier,
    sheetScaffoldState: BottomSheetScaffoldState,
) {
    val coroutineScope = rememberCoroutineScope()
    val currentActivity = LocalContext.current as Activity

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            elevation = 0.dp, modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f),
            border = null
        ) {

            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth(),
                    painter = painterResource(id = R.drawable.astronaut),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
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
                                    listOf(Color.Transparent, Glass),
                                    startY = 50f,
                                    endY = 700f
                                )
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        MyTextTitle(
                            text = stringResource(id = R.string.purchase_credits),
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SpacersSize.small),
                    contentAlignment = Alignment.TopStart
                ) {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            sheetScaffoldState.bottomSheetState.collapse()
                        }
                    }) {
                        MyIcon(iconID = R.drawable.clear, contentDesc = "", tint = Color.LightGray)
                    }
                }
            }
        }

        MySpacer(type = "medium")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SpacersSize.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MyTipText(text = stringResource(id = R.string.art_purchase_credits_desc))

            MySpacer(type = "medium")

            val listOfFeatures = listOf(
                stringResource(id = R.string.arts_per_credits),
                stringResource(id = R.string.download_high_resolution_artwork),
                stringResource(id = R.string.access_to_all_styles),
                stringResource(id = R.string.use_some_inspiration),
            )

            listOfFeatures.forEach {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyIcon(iconID = R.drawable.icon_checkcircle, contentDesc = "")
                    MySpacer(type = "medium", widthOrHeight = "width")
                    MyText(text = it, modifier = Modifier.fillMaxWidth())
                }

                MySpacer(type = "medium")
            }

            val listOfPackages = remember {
                mutableStateOf(listOf<com.revenuecat.purchases.Package>())
            }

            Purchases.sharedInstance.getOfferingsWith({ error ->
            }) {
                listOfPackages.value = it["art"]!!.availablePackages
            }

            LazyColumn {
                items(listOfPackages.value.size) { index ->
                    val product = listOfPackages.value[index].product

                    MyCardView(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            Purchases.sharedInstance.purchaseProduct(currentActivity, product,
                                object : PurchaseCallback {
                                    override fun onCompleted(
                                        storeTransaction: StoreTransaction,
                                        customerInfo: CustomerInfo
                                    ) {
                                        coroutineScope.launch {
                                            sheetScaffoldState.bottomSheetState.collapse()
                                            NotifiersArt.credits.value += product.title.split("(")[0]
                                                .trim()
                                                .split(" ")[0].toInt()
                                        }
                                    }

                                    override fun onError(
                                        error: PurchasesError,
                                        userCancelled: Boolean
                                    ) {
                                    }
                                })
                        }) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            if (product.title.split("(")[0].trim().contains("50"))
                                MyIcon(
                                    iconID = R.drawable.icon_fire,
                                    contentDesc = "",
                                    tint = Orange
                                )

                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(SpacersSize.small)
                        ) {
                            MyTipText(text = stringResource(id = R.string.billed_once))
                            MyText(text = "${product.price}/${product.title.split("(")[0].trim()}")
                        }

                    }

                    MySpacer(type = "small")
                }
            }
        }
    }
}