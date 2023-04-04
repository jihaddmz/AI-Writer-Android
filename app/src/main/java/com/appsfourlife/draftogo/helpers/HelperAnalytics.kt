package com.appsfourlife.draftogo.helpers

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object HelperAnalytics {

    private val analytics by lazy { Firebase.analytics }

    fun sendEvent(name: String) {
        analytics.logEvent(name, null)
    }
}