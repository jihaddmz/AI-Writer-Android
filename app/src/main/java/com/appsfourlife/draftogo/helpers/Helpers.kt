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
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.helpers.Constants.TAG
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

object Helpers {

    fun copyToClipBoard(text: String, msgID: Int, context: Context = App.context) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Copied text", text)
        clipboardManager.setPrimaryClip(clipData)
        HelperUI.showToast(msg = App.getTextFromString(msgID))
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

    fun shareOutputToFacebook(context: Context, text: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.`package` = "com.facebook.katana"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(intent, "Share with"))
    }

    fun shareOutputToLinkedIn(context: Context, text: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.`package` = "com.linkedin.android"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(intent, "Share with"))
    }

    fun shareOutputToTwitter(context: Context, text: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.`package` = "com.twitter.android"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(
            Intent.createChooser(
                intent,
                App.getTextFromString(R.string.share_using)
            )
        )
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
        context.startActivity(
            Intent.createChooser(
                intent,
                App.getTextFromString(R.string.share_using)
            )
        )
    }

    fun isConnected(): Boolean {
        return try {
            val sock = Socket()
            sock.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            sock.close()
            true
        } catch (e: IOException) {
            false
        }
    }

    fun isInternetAvailable(): Boolean {
        val command = Runtime.getRuntime().exec("ping -c 2 www.google.com")
        return command.waitFor() == 0
    }

    fun logD(msg: String) {
        Log.d(TAG, "logD: ${msg}")
    }
}