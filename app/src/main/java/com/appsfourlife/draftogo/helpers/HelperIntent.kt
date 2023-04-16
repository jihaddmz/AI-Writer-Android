package com.appsfourlife.draftogo.helpers

import android.content.Intent
import android.net.Uri
import com.appsfourlife.draftogo.App

object HelperIntent {

    fun navigateToPlayStoreSubscription(){
        val intent = Intent(Intent.parseUri("https://play.google.com/store/account/subscriptions?sku=${Constants.SUBSCRIPTION_PRODUCT_MONTHLY_ID}&package=com.appsfourlife.draftogo", 0))
        App.context.startActivity(intent)
    }

    fun navigateToPlayStorePlusSubscription(){
        val intent = Intent(Intent.parseUri("https://play.google.com/store/account/subscriptions?sku=${Constants.SUBSCRIPTION_PRODUCT_MONTHLY_PLUS_ID}&package=com.appsfourlife.draftogo", 0))
        App.context.startActivity(intent)
    }

    fun navigateToUrl(url: String){
        val intent = Intent(Intent.parseUri(url, 0))
        App.context.startActivity(intent)
    }

    fun sendEmail(recipient: String, subject: String, message: String) {
        /*ACTION_SEND action to launch an email client installed on your Android device.*/
        val mIntent = Intent(Intent.ACTION_SEND)
        /*To send an email you need to specify mailto: as URI using setData() method
        and data type will be to text/plain using setType() method*/
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.`package` = "com.google.android.gm"
        // put recipient email in intent
        /* recipient is put as array because you may wanna send email to multiple emails
           so enter comma(,) separated emails, it will be stored in array*/
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        //put the Subject in the intent
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        //put the message in the intent
        mIntent.putExtra(Intent.EXTRA_TEXT, message)


        try {
            //start email intent
            App.context.startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
        }

    }
}