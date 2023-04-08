package com.appsfourlife.draftogo.components

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.SettingsNotifier
import com.revenuecat.purchases.*
import com.revenuecat.purchases.interfaces.PurchaseCallback
import com.revenuecat.purchases.interfaces.ReceiveOfferingsCallback
import com.revenuecat.purchases.models.StoreTransaction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialogSubscription(
    showDialog: MutableState<Boolean>,
) {

    val currentActivity = LocalContext.current as Activity
    val state = rememberLazyListState()
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

    Dialog(onDismissRequest = {
        showDialog.value = false
    }) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = Shapes.medium
                )
                .padding(horizontal = 5.dp, vertical = SpacersSize.medium),
        ) {

            LazyVerticalGrid(modifier = Modifier
                .fillMaxWidth(),
                state = state,
                cells = GridCells.Fixed(count = 2),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                content = {
                    items(listOfPackages.value.size) { index ->
                        val purchasePackage = listOfPackages.value[index]
                        SubscriptionItem(
                            title = purchasePackage.product.title.split("(")[0].trim(),
                            description = purchasePackage.product.description,
                            price = purchasePackage.product.price
                        ) {

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
                                            showDialog.value = false
                                        } else if (customerInfo.entitlements[Constants.SUBSCRIPTION_TYPE_PLUS]?.isActive == true) {
                                            HelperAuth.makeUserSubscribed()
                                            HelperSharedPreference.setSubscriptionType(Constants.SUBSCRIPTION_TYPE_PLUS)
                                            showDialog.value = false
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

            MySpacer(type = "small")

            MyText(
                text = stringResource(id = R.string.watch_an_ad),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (SettingsNotifier.isConnected.value) {
                            SettingsNotifier.showDialogNbOfGenerationsLeftExceeded.value = false
                            SettingsNotifier.showLoadingDialog.value = true
                            HelperAds.loadAds {
                                HelperAds.showAds(currentActivity) { amount ->
                                    SettingsNotifier.nbOfGenerationsConsumed.value -= 1
                                    HelperSharedPreference.setInt(
                                        HelperSharedPreference.SP_SETTINGS,
                                        HelperSharedPreference.SP_SETTINGS_NB_OF_GENERATIONS_CONSUMED,
                                        SettingsNotifier.nbOfGenerationsConsumed.value
                                    )
                                    HelperFirebaseDatabase.decrementNbOfConsumed()
                                    showDialog.value = false
                                }
                            }
                        } else {
                            HelperUI.showToast(msg = App.getTextFromString(textID = R.string.no_connection))
                        }

                    }, color = Blue,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }

}

@Composable
fun DialogSubscriptionNbOfWordsExceeded(
    showDialog: MutableState<Boolean>
) {

    val plusPackage = remember {
        mutableStateOf<Package?>(null)
    }
    val currentActivity = LocalContext.current as Activity

    Dialog(onDismissRequest = {
        showDialog.value = false
    }) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = Shapes.medium
                )
                .padding(horizontal = 5.dp, vertical = SpacersSize.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MyText(
                text = stringResource(id = R.string.you_have_reached_max_nb_of_words_generated),
                textAlign = TextAlign.Center
            )

            MySpacer(type = "medium")

            Purchases.sharedInstance.getOfferings(object : ReceiveOfferingsCallback {
                override fun onError(error: PurchasesError) {
                }

                override fun onReceived(offerings: Offerings) {
                    plusPackage.value =
                        offerings.current?.get("plus")
                }

            })

            MyAnimatedVisibility(
                modifier = Modifier.fillMaxWidth(),
                visible = plusPackage.value != null
            ) {
                SubscriptionItem(
                    title = plusPackage.value!!.product.title.split("(")[0].trim(),
                    description = plusPackage.value!!.product.description,
                    price = plusPackage.value!!.product.price
                ) {
                    Purchases.sharedInstance.purchaseProductWith(
                        currentActivity,
                        plusPackage.value!!.product,
                        upgradeInfo = UpgradeInfo(oldSku = Constants.SUBSCRIPTION_PRODUCT_MONTHLY_ID),
                        onError = { error, userCancelled ->
                        },
                        onSuccess = { purchase: StoreTransaction?, customerInfo: CustomerInfo ->
                            if (customerInfo.entitlements[Constants.SUBSCRIPTION_TYPE_PLUS]?.isActive == true) {
                                HelperAuth.makeUserSubscribed()
                                HelperSharedPreference.setSubscriptionType(Constants.SUBSCRIPTION_TYPE_PLUS)
                                showDialog.value = false
                            }
                        }
                    )
                }
            }

            MyText(
                text = stringResource(id = R.string.watch_an_ad),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (SettingsNotifier.isConnected.value) {
                            SettingsNotifier.showDialogNbOfGenerationsLeftExceeded.value = false
                            SettingsNotifier.showLoadingDialog.value = true
                            if (SettingsNotifier.mRewardedAds == null) {
                                HelperAds.loadAds {
                                    HelperAds.showAds(currentActivity) { amount ->
                                        HelperFirebaseDatabase.updateNbOfWords()
                                        HelperSharedPreference.setInt(
                                            HelperSharedPreference.SP_SETTINGS,
                                            HelperSharedPreference.SP_SETTINGS_NB_OF_WORDS_GENERATED,
                                            Constants.BASE_PLAN_MAX_NB_OF_WORDS - 500
                                        )
                                        showDialog.value = false
                                    }
                                }
                            } else {
                                HelperAds.showAds(currentActivity) { amount ->
                                    HelperFirebaseDatabase.updateNbOfWords()
                                    HelperSharedPreference.setInt(
                                        HelperSharedPreference.SP_SETTINGS,
                                        HelperSharedPreference.SP_SETTINGS_NB_OF_WORDS_GENERATED,
                                        Constants.BASE_PLAN_MAX_NB_OF_WORDS - 500
                                    )
                                    showDialog.value = false
                                }
                            }
                        } else {
                            HelperUI.showToast(msg = App.getTextFromString(textID = R.string.no_connection))
                        }

                    }, color = Blue,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
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

        MySpacer(type = "medium")

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            MyText(text = description, textAlign = TextAlign.Center)
        }

        MySpacer(type = "medium")

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            MyOutlinedButton(text = stringResource(id = R.string.subscribe)) {
                onSubscribeBtnClick()
            }
        }
    }
}