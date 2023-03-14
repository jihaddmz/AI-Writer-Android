package com.appsfourlife.draftogo.helpers

import androidx.compose.runtime.mutableStateOf
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R


object Constants {

    val TAG = "Jihad"
    val MAX_GENERATION_LENGTH = 1000f
    val MAX_NB_OF_GENERATIONS = 10f
    val MONTHLY_PRICE = 2
    val MAX_TYPEWRITER_LENGTH = 100f
    val ANIMATION_LENGTH = 1000
    val MAX_NB_OF_TRIES_ALLOWED = 2
    val BASE_PLAN_MAX_NB_OF_WORDS = 52500
    val SUBSCRIPTION_MONTHLY_ID = "generations_monthly_fee"
    val WEB_CLIENT_ID = "1057573893043-i2303utnm9oufbqnbdpr8558csaqh066.apps.googleusercontent.com"
    val EXIT_TIME_BETWEEN = 3000L
    val Toast_Lenght = 6000
    val PREDEFINED_TEMPLATES = listOf(
        App.getTextFromString(R.string.write_an_email),
        App.getTextFromString(R.string.write_a_blog_top_bar),
        App.getTextFromString(R.string.write_an_essay),
        App.getTextFromString(R.string.write_an_article_top_bar),
        App.getTextFromString(R.string.write_a_letter),
        App.getTextFromString(R.string.write_a_cv),
        App.getTextFromString(R.string.write_a_resume),
        App.getTextFromString(R.string.write_a_personal_bio_top_bar),
        App.getTextFromString(R.string.write_a_tweet_top_bar),
        App.getTextFromString(R.string.write_a_viral_tiktok_captions_top_bar),
        App.getTextFromString(R.string.write_an_instagram_caption_top_bar),
        App.getTextFromString(R.string.write_a_facebook_post_top_bar),
        App.getTextFromString(R.string.write_a_youtube_caption_top_bar),
        App.getTextFromString(R.string.write_a_podcast_top_bar),
        App.getTextFromString(R.string.write_a_game_script_top_label),
        App.getTextFromString(R.string.write_a_poem_top_bar),
        App.getTextFromString(R.string.write_a_song_top_bar),
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