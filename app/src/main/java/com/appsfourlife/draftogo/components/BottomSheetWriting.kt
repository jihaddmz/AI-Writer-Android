package com.appsfourlife.draftogo.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.ui.theme.Glass
import com.appsfourlife.draftogo.util.BottomNavScreens
import com.appsfourlife.draftogo.util.Screens
import com.appsfourlife.draftogo.util.SettingsNotifier
import kotlinx.coroutines.delay

/**
 * this a bottom sheet holder for the content writer feature
 **/

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetWriting(
    navController: NavController,
    modifier: Modifier = Modifier,
    bottomSheet: @Composable () -> Unit = { BottomSheetSavedOutputs() },
    content: @Composable () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val sheetScaffoldState = rememberBottomSheetScaffoldState()
    val changeTargetValue = remember {
        mutableStateOf(false)
    }
    val dpSize = animateDpAsState(
        targetValue = if (changeTargetValue.value && (HelperSharedPreference.getIsSavedOutputsEnabled() || SettingsNotifier.enableSheetContent.value)
            && (navBackStackEntry?.destination?.route != Screens.ScreenSignIn.route
                    && navBackStackEntry?.destination?.route != Screens.ScreenLaunch.route
                    && navBackStackEntry?.destination?.route != BottomNavScreens.Settings.route
                    && navBackStackEntry?.destination?.route != BottomNavScreens.History.route
                    && navBackStackEntry?.destination?.route != BottomNavScreens.Home.route)
        ) 70.dp else (-80).dp,
        animationSpec = tween(durationMillis = Constants.ANIMATION_LENGTH)
    )
    LaunchedEffect(key1 = true, block = {
        delay(Constants.SPLASH_SCREEN_DURATION)
        changeTargetValue.value = true
    })
    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = sheetScaffoldState,
        sheetContent = { bottomSheet() },
        sheetPeekHeight = dpSize.value,
        backgroundColor = Glass
    ) {
        content()
    }
}