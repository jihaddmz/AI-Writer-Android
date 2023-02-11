package com.jihad.aiwriter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.jihad.aiwriter.MainActivity
import com.jihad.aiwriter.R
import com.jihad.aiwriter.SettingsNotifier
import com.jihad.aiwriter.helpers.Constants
import com.jihad.aiwriter.helpers.HelperAuth
import com.jihad.aiwriter.helpers.HelperUI
import com.jihad.aiwriter.ui.theme.Shapes
import com.jihad.aiwriter.ui.theme.SpacersSize
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.getOfferingsWith
import com.revenuecat.purchases.interfaces.PurchaseCallback
import com.revenuecat.purchases.models.StoreTransaction
import kotlinx.coroutines.launch

@Composable
fun DialogNbOfGenerationsExceeded(
    modifier: Modifier = Modifier,
) {

    val coroutineScope = rememberCoroutineScope()
    val currentActivity = LocalContext.current as MainActivity
    val context = LocalContext.current

    Dialog(onDismissRequest = {
        coroutineScope.launch {
            SettingsNotifier.showDialogNbOfGenerationsLeftExceeded.value = false
        }
    }) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = Shapes.medium)
                .padding(SpacersSize.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MyText(text = stringResource(id = R.string.attention), fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            MyText(
                text = stringResource(
                    id = R.string.nb_of_text_generations_exceeded,
                    Constants.MONTHLY_PRICE
                ), textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(SpacersSize.medium))

            MyButton(text = stringResource(id = R.string.subscribe)) {

                // dismissing the dialog
                SettingsNotifier.showDialogNbOfGenerationsLeftExceeded.value = false

                // mark on subscribe button click
                Purchases.sharedInstance.getOfferingsWith({ error ->
                    // An error occurred
                }) { offerings ->
                    offerings.current?.monthly?.product?.also {
                        Purchases.sharedInstance.purchaseProduct(
                            currentActivity,
                            it,
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
                                    HelperUI.showToast(context, error.message)
                                }

                            })
                        // Get the price and introductory period from the SkuDetails
                    }
                }
            }
        }
    }
}