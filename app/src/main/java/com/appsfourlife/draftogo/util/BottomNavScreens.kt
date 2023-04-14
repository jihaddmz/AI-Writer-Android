package com.appsfourlife.draftogo.util

import com.appsfourlife.draftogo.R

sealed class BottomNavScreens(val route: String, val iconID: Int, val labelID: Int) {
    object Settings : BottomNavScreens("screen_settings", R.drawable.icon_settings, R.string.settings)
    object History : BottomNavScreens("screen_history", R.drawable.icon_history, R.string.history)
    object Home : BottomNavScreens("screen_home", R.drawable.icon_writing, R.string.content)
    object Feedback : BottomNavScreens("screen_feedback", R.drawable.icon_feedback, R.string.Feedback)
    object Art : BottomNavScreens("screen_art", R.drawable.icon_image, R.string.art)
    object Dashboard : BottomNavScreens("screen_dashboard", R.drawable.icon_dashboard, R.string.dashboard)
}
