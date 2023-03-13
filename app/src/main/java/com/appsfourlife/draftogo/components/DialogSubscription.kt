package com.appsfourlife.draftogo.components

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.helpers.HelperAuth
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.getOfferingsWith
import com.revenuecat.purchases.interfaces.PurchaseCallback
import com.revenuecat.purchases.models.StoreTransaction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialogSubscription(
    showDialog: MutableState<Boolean>
) {

    val currentActivity = LocalContext.current as Activity
    val state = rememberLazyListState()
    val listOfPackages = remember {
        mutableStateOf(listOf<com.revenuecat.purchases.Package>())
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

        LazyVerticalGrid(modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = Shapes.medium)
            .padding(horizontal = 5.dp, vertical = SpacersSize.medium),
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
                                    HelperAuth.makeUserSubscribed()
                                }

                                override fun onError(
                                    error: PurchasesError,
                                    userCancelled: Boolean
                                ) {
//                                    HelperUI.showToast(context, error.message)
                                }

                            })
                    }
                }
            })
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
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MyTextTitle(
            text = title,
            color = Color.White,
            modifier = Modifier
                .background(color = Blue, shape = Shapes.small)
                .padding(SpacersSize.small)
        )
        MyText(text = price)

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