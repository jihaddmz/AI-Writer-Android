package com.appsfourlife.draftogo.components

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.appsfourlife.draftogo.SettingsNotifier
import com.appsfourlife.draftogo.feature_generate_text.util.Screens

@Composable
fun MyBackHandler(navController: NavController) {
    BackHandler(true) {
        SettingsNotifier.resetValues()
        navController.navigate(Screens.ScreenHome.route)
    }
}