package com.appsfourlife.draftogo.util

import com.appsfourlife.draftogo.R

sealed class BottomNavScreens(val route: String, val iconID: Int, val labelID: Int) {
    object Settings : BottomNavScreens("screen_settings", R.drawable.icon_settings, R.string.settings)
    object Content : BottomNavScreens("screen_content", R.drawable.icon_writing, R.string.content)
    object Art : BottomNavScreens("screen_art", R.drawable.icon_image, R.string.art)
    object Dashboard : BottomNavScreens("screen_dashboard", R.drawable.icon_dashboard, R.string.dashboard)
}
