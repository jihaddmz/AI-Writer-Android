package com.appsfourlife.draftogo.helpers

import java.text.SimpleDateFormat
import java.util.*

object HelperDate {

    fun getCurrentDateWithSec(): String {
        val simpleFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return simpleFormatter.format(Date())
    }

    fun parseDateToString(date: Date, format: String): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(date)
    }

    fun parseStringToDate(date: String, format: String): Date? {
        return SimpleDateFormat(format, Locale.getDefault()).parse(date)
    }

    fun getCurrentDateInString(): String {
        val simpleFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return simpleFormatter.format(Date())
    }
}