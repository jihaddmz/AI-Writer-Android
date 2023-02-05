package com.jihad.aiwriter

import android.app.Application
import android.content.Context
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration

class App : Application() {

    companion object {
        lateinit var context: Context
        val listOfCVTypes by lazy { mutableListOf<String>() }
        val listOfLetterTypes by lazy { mutableListOf<String>() }
        val modelManager by lazy { RemoteModelManager.getInstance() }
        val languageIdentifier by lazy { LanguageIdentification.getClient() }
        val listOfLanguages by lazy { mutableListOf<String>() }
        val listOfEssays by lazy { mutableListOf<String>() }
        lateinit var mapOfScreens: HashMap<Int, List<Any>>
        fun getTextFromString(textID: Int): String {
            return context.getString(textID)
        }
    }

    override fun onCreate() {
        super.onCreate()

        App.context = this

//        val qonversionConfig = QonversionConfig.Builder(
//            this,
//            "KZvH_dnMjzNV1wkNcLJAu5aNSwSVeScT",
//            QLaunchMode.SubscriptionManagement
//        ).setEnvironment(QEnvironment.Sandbox).build()
//        Qonversion.initialize(qonversionConfig)

        Purchases.debugLogsEnabled = true
        Purchases.configure(PurchasesConfiguration.Builder(this, "goog_NECfLQxMlFFmYmWUVNINyyiEEmb").build())

        mapOfScreens = hashMapOf(
            0 to listOf(
                getTextFromString(R.string.write_an_email),
                R.drawable.icon_email
            ),
            1 to listOf(getTextFromString(R.string.write_a_blog), R.drawable.icon_blog),
            2 to listOf(getTextFromString(R.string.write_an_essay), R.drawable.icon_essay),
            3 to listOf(getTextFromString(R.string.write_an_article), R.drawable.icon_article),
            4 to listOf(getTextFromString(R.string.write_a_letter), R.drawable.emoji_letter),
            5 to listOf(getTextFromString(R.string.write_a_cv), R.drawable.icon_cv),
            6 to listOf(getTextFromString(R.string.write_a_resume), R.drawable.icon_resume),
            7 to listOf(getTextFromString(R.string.write_a_poem), R.drawable.icon_poem_heart),
            8 to listOf(getTextFromString(R.string.write_a_song), R.drawable.icon_song),
            9 to listOf(
                getTextFromString(R.string.write_a_tweet),
                R.drawable.icon_logo_twitter
            ),
            10 to listOf(
                getTextFromString(R.string.write_a_viral_tiktok_captions),
                R.drawable.icon_tiktok
            ),
            11 to listOf(
                getTextFromString(R.string.write_an_instagram_caption),
                R.drawable.icon_instagram
            ),
            12 to listOf(
                getTextFromString(R.string.write_a_facebook_post),
                R.drawable.icon_facebook
            ),
            13 to listOf(
                getTextFromString(R.string.write_a_youtube_caption),
                R.drawable.icon_youtube
            ),
            14 to listOf(
                getTextFromString(R.string.write_a_personal_bio),
                R.drawable.social_bio
            ),
            15 to listOf(getTextFromString(R.string.write_a_code), R.drawable.icon_code),
            16 to listOf(getTextFromString(R.string.custom), R.drawable.icon_customize),
        )

        listOfCVTypes.add(getString(R.string.chronological))
        listOfCVTypes.add(getString(R.string.functional))
        listOfCVTypes.add(getString(R.string.combination))

        listOfLetterTypes.add(getString(R.string.normal_letter))
        listOfLetterTypes.add(getString(R.string.cover_letter))
        listOfLetterTypes.add(getString(R.string.reference_letter))
        listOfLetterTypes.add(getString(R.string.resignation_letter))

        listOfLanguages.add(getString(R.string.en))
        listOfLanguages.add(getString(R.string.ar))
        listOfLanguages.add(getString(R.string.de))
        listOfLanguages.add(getString(R.string.fr))

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