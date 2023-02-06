package com.jihad.aiwriter.helpers

import com.jihad.aiwriter.App
import com.jihad.aiwriter.SettingsNotifier

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
}