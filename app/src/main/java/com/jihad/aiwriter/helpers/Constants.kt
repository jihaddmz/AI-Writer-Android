package com.jihad.aiwriter.helpers

import com.jihad.aiwriter.App
import com.jihad.aiwriter.R


object Constants {

    val TAG = "Jihad"
    val MAX_GENERATION_LENGTH = 1000f
    val MAX_NB_OF_GENERATIONS = 10f
    val MONTHLY_PRICE = 2
    val ANIMATION_LENGTH = 1000
    val MAX_NB_OF_TRIES_ALLOWED = 3
    val SUBSCRIPTION_MONTHLY_ID = "generations_monthly_fee"
    val EXIT_TIME_BETWEEN = 3000L
    val Toast_Lenght = 6000
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