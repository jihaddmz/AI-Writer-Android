package com.appsfourlife.draftogo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import com.appsfourlife.draftogo.helpers.Helpers

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val data = mutableStateOf(intent.getStringExtra("text"))
        Helpers.logD("Entered")
        data.value = "Hello there"
        AccessNotifiers.event!!.source!!.text = "Hello there"

    }
}