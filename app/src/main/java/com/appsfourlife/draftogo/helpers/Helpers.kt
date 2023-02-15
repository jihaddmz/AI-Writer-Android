package com.appsfourlife.draftogo.helpers

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.content.ContextCompat.startActivity
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.helpers.Constants.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Helpers {

    fun copyToClipBoard(text: String, context: Context = App.context) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Copied text", text)
        clipboardManager.setPrimaryClip(clipData)
    }

    fun pasteFromClipBoard(text: MutableState<TextFieldValue>, context: Context): TextFieldValue {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = clipboardManager.primaryClip
        val concatenatedText = text.value.text.plus(clipData?.getItemAt(0)?.text)
        return TextFieldValue(
            text = concatenatedText, selection = TextRange(concatenatedText.length)
        )
    }

    fun shareOutput(text: String, context: Context) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(intent, "Share using"))
    }

    fun shareOutputToFacebook(context: Context, text: String){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.`package` = "com.facebook.katana"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(intent, "Share with"))
    }

    fun shareOutputToTwitter(context: Context, text: String){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.`package` = "com.twitter.android"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(intent, App.getTextFromString(R.string.share_using)))
    }

    fun shareOutputToInstagram(context: Context, text: String){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.`package` = "com.instagram.android"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(intent, App.getTextFromString(R.string.share_using)))
    }

    fun shareEmailOutput(text: String, email: String, context: Context) {
        var body = ""
        val parts = text.lines()
        val intent = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null
            )
        )
        parts.forEach {
            if (it.contains("Subject", true)) {
                intent.putExtra(Intent.EXTRA_SUBJECT, it.split(":")[1].trim())
                body = text.replace(it, "")
            }
        }
        intent.putExtra(Intent.EXTRA_TEXT, body.trim())
        context.startActivity(Intent.createChooser(intent, App.getTextFromString(R.string.share_using)))
    }

    fun checkForConnection(
        coroutineScope: CoroutineScope, ifConnected: () -> Unit, notConnected: () -> Unit
    ) {
        val command = "ping -c 1 google.com"
        coroutineScope.launch(Dispatchers.IO) {

            if (Runtime.getRuntime().exec(command).waitFor() == 0){
                ifConnected()
            }else {
                notConnected()
            }
        }
    }

    fun logD(msg: String) {
        Log.d(TAG, "logD: ${msg}")
    }
}