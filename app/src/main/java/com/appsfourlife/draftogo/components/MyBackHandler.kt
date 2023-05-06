package com.appsfourlife.draftogo.components

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.appsfourlife.draftogo.util.BottomNavScreens
import com.appsfourlife.draftogo.util.SettingsNotifier

@Composable
fun MyBackHandler(navController: NavController) {
    BackHandler(true) {
        SettingsNotifier.resetValues()
        navController.navigate(BottomNavScreens.Dashboard.route)
    }
}