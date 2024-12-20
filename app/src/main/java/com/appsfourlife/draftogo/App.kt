package com.appsfourlife.draftogo

import android.app.Application
import android.content.Context
import android.view.Window
import androidx.appcompat.app.AppCompatDelegate
import com.appsfourlife.draftogo.data.data_source.DatabaseApp
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.util.SettingsNotifier
import com.google.firebase.FirebaseApp
import com.onesignal.OneSignal
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.interfaces.ReceiveCustomerInfoCallback
import kotlinx.coroutines.*
import java.util.*
import kotlin.concurrent.timerTask

class App : Application() {

    private val ONESIGNAL_APP_ID = "dbea2cd9-7f66-47c1-8941-a14fe77d1f45"

    companion object {
        lateinit var context: Context
        lateinit var window: Window
        lateinit var databaseApp: DatabaseApp
        val listOfCVTypes by lazy { mutableListOf<String>() }
        val listOfLetterTypes by lazy { mutableListOf<String>() }
        val listOfEssays by lazy { mutableListOf<String>() }
        fun getTextFromString(textID: Int, args: Any? = null): String {
            return context.getString(textID, args)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()

        context = this
        databaseApp = DatabaseApp.getInstance(this)!!

        FirebaseApp.initializeApp(this)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        OneSignal.promptForPushNotifications();
        OneSignal.addEmailSubscriptionObserver {

        }

        Purchases.debugLogsEnabled = true
        Purchases.configure(
            PurchasesConfiguration.Builder(this, "goog_NECfLQxMlFFmYmWUVNINyyiEEmb").build()
        )

        Timer().scheduleAtFixedRate(timerTask {
            GlobalScope.launch(Dispatchers.IO) {
                SettingsNotifier.isConnected.value = Helpers.isInternetAvailable()
            }
        }, 0, 2000)

        // mark checking purchase state
        Timer().scheduleAtFixedRate(timerTask {
            Purchases.sharedInstance.getCustomerInfo(object : ReceiveCustomerInfoCallback {
                override fun onError(error: PurchasesError) {
                }

                override fun onReceived(customerInfo: CustomerInfo) {
                    if (customerInfo.entitlements["premium"]?.isActive == true) { // if the user is subscribed

                        customerInfo.entitlements["premium"]?.expirationDate?.let {
                            val date = HelperDate.parseDateToString(it, "dd/MM/yyyy")
                            HelperSharedPreference.setString(
                                HelperSharedPreference.SP_AUTHENTICATION,
                                HelperSharedPreference.SP_AUTHENTICATION_EXPIRATION_DATE,
                                date
                            )
                        }

                        HelperAuth.makeUserSubscribed()
                        HelperSharedPreference.setSubscriptionType(Constants.SUBSCRIPTION_TYPE_BASE)
                        customerInfo.entitlements["premium"]?.willRenew?.let {
                            SettingsNotifier.isRenewable.value = it
                            HelperSharedPreference.setBool(
                                HelperSharedPreference.SP_AUTHENTICATION,
                                HelperSharedPreference.SP_AUTHENTICATION_WILL_RENEW,
                                it
                            )
                        }
                    } else if (customerInfo.entitlements[Constants.SUBSCRIPTION_TYPE_PLUS]?.isActive == true) {

                        customerInfo.entitlements[Constants.SUBSCRIPTION_TYPE_PLUS]?.expirationDate?.let {
                            val date = HelperDate.parseDateToString(it, "dd/MM/yyyy")
                            HelperSharedPreference.setString(
                                HelperSharedPreference.SP_AUTHENTICATION,
                                HelperSharedPreference.SP_AUTHENTICATION_EXPIRATION_DATE,
                                date
                            )
                        }

                        HelperAuth.makeUserSubscribed()
                        HelperSharedPreference.setSubscriptionType(Constants.SUBSCRIPTION_TYPE_PLUS)
                        customerInfo.entitlements[Constants.SUBSCRIPTION_TYPE_PLUS]?.willRenew?.let {
                            SettingsNotifier.isRenewable.value = it
                            HelperSharedPreference.setBool(
                                HelperSharedPreference.SP_AUTHENTICATION,
                                HelperSharedPreference.SP_AUTHENTICATION_WILL_RENEW,
                                it
                            )
                        }
                    } else { // user has no access to the product
                        HelperAuth.makeUserNotSubscribed()
                        HelperSharedPreference.setSubscriptionType("not subscribed")
                    }
                }
            })
        }, 0, 5000)

        listOfCVTypes.add(getString(R.string.chronological))
        listOfCVTypes.add(getString(R.string.functional))
        listOfCVTypes.add(getString(R.string.combination))

        listOfLetterTypes.add(getString(R.string.normal_letter))
        listOfLetterTypes.add(getString(R.string.cover_letter))
        listOfLetterTypes.add(getString(R.string.reference_letter))
        listOfLetterTypes.add(getString(R.string.resignation_letter))

        listOfEssays.add(getTextFromString(R.string.normal_essay))
        listOfEssays.add(getTextFromString(R.string.narrative))
        listOfEssays.add(getTextFromString(R.string.descriptive))
        listOfEssays.add(getTextFromString(R.string.expository))
        listOfEssays.add(getTextFromString(R.string.persuasive))
        listOfEssays.add(getTextFromString(R.string.technical))
        listOfEssays.add(getTextFromString(R.string.diary))
        listOfEssays.add(getTextFromString(R.string.business))
        listOfEssays.add(getTextFromString(R.string.copy_writing))
        listOfEssays.add(getTextFromString(R.string.travel_writing))

    }
}