package com.appsfourlife.draftogo.helpers

import android.app.Activity
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.util.SettingsNotifier
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object HelperAuth {

    val auth by lazy { Firebase.auth }

    fun makeUserSubscribed() {
        SettingsNotifier.isSubscribed.value = true
        HelperSharedPreference.setBool(
            HelperSharedPreference.SP_AUTHENTICATION,
            HelperSharedPreference.SP_AUTHENTICATION_IS_SUBSCRIBED,
            true,
            App.context
        )
    }

    fun makeUserNotSubscribed() {
        SettingsNotifier.isSubscribed.value = false
        HelperSharedPreference.setBool(
            HelperSharedPreference.SP_AUTHENTICATION,
            HelperSharedPreference.SP_AUTHENTICATION_IS_SUBSCRIBED,
            false,
            App.context
        )
    }

    fun isSubscribed(): Boolean {
        return SettingsNotifier.isSubscribed.value || HelperSharedPreference.getBool(
            HelperSharedPreference.SP_AUTHENTICATION,
            HelperSharedPreference.SP_AUTHENTICATION_IS_SUBSCRIBED,
            false,
            App.context
        )
    }

    fun willRenew(): Boolean {
        return SettingsNotifier.isRenewable.value || HelperSharedPreference.getBool(
            HelperSharedPreference.SP_AUTHENTICATION,
            HelperSharedPreference.SP_AUTHENTICATION_WILL_RENEW,
            false,
            App.context
        )
    }

    fun getExpirationDate(): String {
        return HelperSharedPreference.getString(
            HelperSharedPreference.SP_AUTHENTICATION,
            HelperSharedPreference.SP_AUTHENTICATION_EXPIRATION_DATE,
            "",
            App.context
        )

    }

    fun signIn(context: Activity) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Constants.WEB_CLIENT_ID)
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)

        val intent = mGoogleSignInClient.signInIntent
        context.startActivityForResult(intent, 12)
    }

    fun signOut() {
        auth.signOut()
        HelperSharedPreference.setUsername("")
    }
}