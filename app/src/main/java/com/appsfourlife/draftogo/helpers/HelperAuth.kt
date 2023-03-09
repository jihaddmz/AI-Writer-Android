package com.appsfourlife.draftogo.helpers

import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.SettingsNotifier

object HelperAuth {

    fun makeUserSubscribed(){
        SettingsNotifier.isSubscribed.value = true
        HelperSharedPreference.setBool(HelperSharedPreference.SP_AUTHENTICATION, HelperSharedPreference.SP_AUTHENTICATION_IS_SUBSCRIBED, true, App.context)
    }

    fun makeUserNotSubscribed(){
        SettingsNotifier.isSubscribed.value = false
        HelperSharedPreference.setBool(HelperSharedPreference.SP_AUTHENTICATION, HelperSharedPreference.SP_AUTHENTICATION_IS_SUBSCRIBED, false, App.context)
    }

    fun getUserSubscriptionState(): Boolean{
        return SettingsNotifier.isSubscribed.value || HelperSharedPreference.getBool(HelperSharedPreference.SP_AUTHENTICATION, HelperSharedPreference.SP_AUTHENTICATION_IS_SUBSCRIBED, false, App.context)
    }

    fun willRenew(): Boolean {
        return SettingsNotifier.isRenewable.value || HelperSharedPreference.getBool(HelperSharedPreference.SP_AUTHENTICATION, HelperSharedPreference.SP_AUTHENTICATION_WILL_RENEW, false, App.context)
    }

    fun getExpirationDate(): String {
        return HelperSharedPreference.getString(HelperSharedPreference.SP_AUTHENTICATION, HelperSharedPreference.SP_AUTHENTICATION_EXPIRATION_DATE, "", App.context)

    }
}