package com.appsfourlife.draftogo.helpers

import android.content.Intent
import com.appsfourlife.draftogo.App

object HelperIntent {

    fun navigateToPlayStoreSubscription(){
        val intent = Intent(Intent.parseUri("https://play.google.com/store/account/subscriptions?sku=${Constants.SUBSCRIPTION_MONTHLY_ID}&package=com.appsfourlife.draftogo", 0))
        App.context.startActivity(intent)
    }
}