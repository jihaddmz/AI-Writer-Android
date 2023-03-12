package com.appsfourlife.draftogo.helpers

import java.text.SimpleDateFormat
import java.util.*

object HelperDate {

    fun getCurrentDate(): String {
        val simpleFormatter = SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.getDefault())
        return simpleFormatter.format(Date())
    }
}