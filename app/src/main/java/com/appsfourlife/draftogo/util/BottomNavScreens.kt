package com.appsfourlife.draftogo.util

import com.appsfourlife.draftogo.R

sealed class BottomNavScreens(val route: String, val iconID: Int, val labelID: Int) {
    object Settings : BottomNavScreens("screen_settings", R.drawable.icon_settings, R.string.settings)
    object History : BottomNavScreens("screen_history", R.drawable.icon_history, R.string.history)
    object Home : BottomNavScreens("screen_home", R.drawable.icon_home, R.string.home)
}
