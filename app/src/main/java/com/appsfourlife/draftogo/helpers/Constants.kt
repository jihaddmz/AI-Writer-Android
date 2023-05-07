package com.appsfourlife.draftogo.helpers

import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R


object Constants {

    const val SUBSCRIPTION_PRODUCT_MONTHLY_ID = "generations_monthly_fee"
    const val SUBSCRIPTION_PRODUCT_MONTHLY_PLUS_ID = "generations_monthly_fee_plus"
    const val SUBSCRIPTION_TYPE_BASE = "base"
    const val SUBSCRIPTION_TYPE_PLUS = "plus"

    const val TAG = "Jihad"
    const val SPLASH_SCREEN_DURATION: Long = 800L
    const val MAX_GENERATION_LENGTH = 750f
    const val DEFAULT_POSTING_GENERATION_LENGTH = 400f
    const val MAX_NB_OF_GENERATIONS = 5f
    const val MAX_TYPEWRITER_LENGTH = 100f
    const val ANIMATION_LENGTH = 1000
    const val BASE_PLAN_MAX_NB_OF_WORDS = 45000
    const val WEB_CLIENT_ID = "1057573893043-i2303utnm9oufbqnbdpr8558csaqh066.apps.googleusercontent.com"
    const val AD_UNIT_ID = "ca-app-pub-6005833463272552/3492799476" // test unit ad ca-app-pub-3940256099942544/5224354917 // my ad unit ca-app-pub-6005833463272552/3492799476
    const val DAY_MONTH_YEAR_FORMAT = "dd/MM/yyyy"
    const val EXIT_TIME_BETWEEN = 3000L
    const val Toast_Lenght = 6000
    const val NB_OF_MAX_ALLOWED_GENERATIONS = 2


    /**
     * lists
     **/
    val FEEDBACK_TYPES = listOf(
        App.getTextFromString(R.string.feature_request),
        App.getTextFromString(R.string.bug),
        App.getTextFromString(R.string.comment),
        App.getTextFromString(R.string.other)
    )
    val PREDEFINED_TEMPLATES = listOf(
        App.getTextFromString(R.string.write_an_email),
        App.getTextFromString(R.string.write_a_blog),
        App.getTextFromString(R.string.write_an_essay),
        App.getTextFromString(R.string.write_an_article),
        App.getTextFromString(R.string.write_a_letter),
        App.getTextFromString(R.string.write_a_cv),
        App.getTextFromString(R.string.write_a_resume),
        App.getTextFromString(R.string.write_a_personal_bio),
        App.getTextFromString(R.string.summarize_the_following_text),
        App.getTextFromString(R.string.correct_the_following_text),
        App.getTextFromString(R.string.write_a_tweet),
        App.getTextFromString(R.string.write_a_viral_tiktok_captions),
        App.getTextFromString(R.string.write_an_instagram_caption),
        App.getTextFromString(R.string.write_a_facebook_post),
        App.getTextFromString(R.string.write_a_linkedin_post),
        App.getTextFromString(R.string.write_a_youtube_caption),
        App.getTextFromString(R.string.translate_the_following_text),
        App.getTextFromString(R.string.write_a_podcast_top_bar),
        App.getTextFromString(R.string.write_a_game_script_top_label),
        App.getTextFromString(R.string.write_a_poem),
        App.getTextFromString(R.string.write_a_song),
        App.getTextFromString(R.string.write_a_code),
        App.getTextFromString(R.string.custom)
        )
    val OUTPUT_LANGUAGES by lazy {
        listOf(
            App.getTextFromString(R.string.english),
            App.getTextFromString(R.string.french),
            App.getTextFromString(R.string.arabic),
            App.getTextFromString(R.string.german),
            App.getTextFromString(R.string.hindi),
            App.getTextFromString(R.string.italian),
            App.getTextFromString(R.string.purtaguese),
            App.getTextFromString(R.string.russian),
            App.getTextFromString(R.string.turkish),
            App.getTextFromString(R.string.swedish),
            App.getTextFromString(R.string.dutch),
        )
    }
    val listOfGameConsoleTypes by lazy {
        listOf(
            App.getTextFromString(R.string.playStation_5),
            App.getTextFromString(R.string.playStation_4),
            App.getTextFromString(R.string.desktop),
            App.getTextFromString(R.string.xbox),
            App.getTextFromString(R.string.mobile),
            App.getTextFromString(R.string.nintendo),
        )
    }

    val listOfPodcastTypes by lazy {
        listOf(
            App.getTextFromString(R.string.conversational),
            App.getTextFromString(R.string.educational),
            App.getTextFromString(R.string.solo),
            App.getTextFromString(R.string.storytelling),
            App.getTextFromString(R.string.podcast_theater),
        )
    }

    val listOfWritingModels = listOf(
        "gpt-3.5-turbo",
        "text-davinci-003"
    )

    val listOfProgrammingLanguages by lazy {
        listOf(
            "Java",
            "Kotlin",
            "C++",
            "C",
            "C#",
            "Swift",
            "Objective-c",
            "Python",
            "Javascript",
            "Typescript",
            "Go",
            "Dart",
            "Rust",
            "Php",
            "Ruby",
            "Sql",
            "R",
            "HTML",
            "CSS"
        )
    }
}