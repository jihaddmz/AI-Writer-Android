package com.appsfourlife.draftogo.feature_generate_text.components

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.feature_generate_art.notifiers.NotifiersArt
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelPurchaseHistory
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.helpers.HelperAuth
import com.appsfourlife.draftogo.helpers.HelperDate
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.SettingsNotifier
import com.revenuecat.purchases.*
import com.revenuecat.purchases.interfaces.PurchaseCallback
import com.revenuecat.purchases.models.StoreTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
@Preview
fun BottomSheetWritePricing(
    sheetScaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState()
) {
    val currentActivity = LocalContext.current as Activity
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize()) {

        TopImageHeader(
            sheetScaffoldState = sheetScaffoldState,
            drawableID = R.drawable.writing_header,
            title = stringResource(id = R.string.ai_content_writer)
        )

        MySpacer(type = "medium")

        MyTipText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacersSize.small),
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.writing_price_header)
        )

        MySpacer(type = "medium")

        val listOfPackages = remember {
            mutableStateOf(listOf<Package>())
        }

        Purchases.sharedInstance.getOfferingsWith({ error ->
            // An error occurred
        }) { offerings ->
            offerings.current?.availablePackages?.takeUnless {
                it.isEmpty()
            }?.let {
                listOfPackages.value = it
            }
        }

        LazyVerticalGrid(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SpacersSize.small),
            columns = GridCells.Fixed(count = 2),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            content = {
                items(listOfPackages.value.size) { index ->
                    val purchasePackage = listOfPackages.value[index]
                    val product = purchasePackage.product

                    SubscriptionItem(
                        title = purchasePackage.product.title.split("(")[0].trim(),
                        description = purchasePackage.product.description,
                        price = purchasePackage.product.price
                    ) {
                        val price = product.price.replace(product.price.filter { !it.isDigit() && it != '.' }, "")
                        coroutineScope.launch(Dispatchers.IO) {
                            if (App.databaseApp.daoApp.getPurchaseHistory(
                                    HelperDate.parseDateToString(
                                        Date(), "dd/MM/yyyy"
                                    )
                                ) == null
                            ) {
                                App.databaseApp.daoApp.insertPurchaseHistory(
                                    ModelPurchaseHistory(
                                        HelperDate.parseDateToString(
                                            Date(), "dd/MM/yyyy"
                                        ), price.toFloat()
                                    )
                                )
                            } else {
                                val purchaseHistory =
                                    App.databaseApp.daoApp.getPurchaseHistory(
                                        HelperDate.parseDateToString(
                                            Date(), "dd/MM/yyyy"
                                        )
                                    )!!
                                purchaseHistory.price += price.toFloat()
                                App.databaseApp.daoApp.updatePurchaseHistory(
                                    ModelPurchaseHistory(
                                        HelperDate.parseDateToString(
                                            Date(), "dd/MM/yyyy"
                                        ), purchaseHistory.price
                                    )
                                )
                            }
                        }
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

                        Purchases.sharedInstance.purchaseProduct(
                            currentActivity,
                            purchasePackage.product,
                            object : PurchaseCallback {
                                override fun onCompleted(
                                    storeTransaction: StoreTransaction,
                                    customerInfo: CustomerInfo
                                ) {
                                    if (customerInfo.entitlements["premium"]?.isActive == true) {
                                        HelperAuth.makeUserSubscribed()
                                        HelperSharedPreference.setSubscriptionType(Constants.SUBSCRIPTION_TYPE_BASE)
                                        coroutineScope.launch {
                                            sheetScaffoldState.bottomSheetState.collapse()
                                        }
                                    } else if (customerInfo.entitlements[Constants.SUBSCRIPTION_TYPE_PLUS]?.isActive == true) {
                                        HelperAuth.makeUserSubscribed()
                                        HelperSharedPreference.setSubscriptionType(Constants.SUBSCRIPTION_TYPE_PLUS)
                                        coroutineScope.launch {
                                            sheetScaffoldState.bottomSheetState.collapse()
                                        }
                                    }
                                }

                                override fun onError(
                                    error: PurchasesError,
                                    userCancelled: Boolean
                                ) {
                                }

                            })
                    }
                }
            })

        MySpacer(type = "medium")

        val listOfFeatures = listOf(
            stringResource(id = R.string.access_to_all_templates),
            stringResource(id = R.string.generate_content_in_different_languages),
            stringResource(id = R.string.create_custom_templates),
            stringResource(id = R.string.read_output_outload),
            stringResource(id = R.string.share_content_directly),
            stringResource(id = R.string.save_outputs_for_comparison),
        )

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(horizontal = SpacersSize.small)
        ) {

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
        }
    }
}

@Composable
fun SubscriptionItem(
    title: String,
    description: String,
    price: String,
    onSubscribeBtnClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MyTextTitle(
            text = title,
            color = Color.White,
            modifier = Modifier
                .background(color = Blue, shape = Shapes.small)
                .padding(SpacersSize.small)
        )
        MyText(text = "$price/month")

        MySpacer(type = "small")

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            MyText(text = description, textAlign = TextAlign.Center)
        }

        MySpacer(type = "small")

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            if (SettingsNotifier.isBasePlanNbOfWordsExceeded.value) {
                if (title.lowercase().contains("base")) {
                    MyOutlinedButton(
                        isEnabled = false,
                        text = stringResource(id = R.string.subscribe)
                    ) {
                        onSubscribeBtnClick()
                    }
                } else {
                    MyOutlinedButton(text = stringResource(id = R.string.upgrade)) {
                        onSubscribeBtnClick()
                    }
                }
            } else
                MyOutlinedButton(text = stringResource(id = R.string.subscribe)) {
                    onSubscribeBtnClick()
                }
        }
    }
}