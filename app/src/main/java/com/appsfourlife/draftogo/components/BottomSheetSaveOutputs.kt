package com.appsfourlife.draftogo.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Glass
import com.appsfourlife.draftogo.util.BottomNavScreens
import com.appsfourlife.draftogo.util.Screens
import com.appsfourlife.draftogo.util.SettingsNotifier
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetSaveOutputs(
    navController: NavController,
    modifier: Modifier = Modifier,
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
        sheetContent = { BottomSheet() },
        sheetPeekHeight = dpSize.value,
        backgroundColor = Glass
    ) {
        content()
    }
}

@Composable
fun BottomSheet() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Text(
                text = stringResource(id = R.string.save_outputs),
                style = MaterialTheme.typography.h6
            )
        }

        if (SettingsNotifier.comparisonGenerationEntries.value.isEmpty()) {
            item {
                MyText(text = stringResource(id = R.string.label_no_saved_items_added))

                MySpacer(type = "medium")

                MyLottieAnim(
                    lottieID = R.raw.empty_box,
                    isLottieAnimationPlaying = mutableStateOf(true)
                )
            }
        }

        items(SettingsNotifier.comparisonGenerationEntries.value.size) { index ->
            MySpacer(type = "medium")
            val modelComparedGenerationItem =
                SettingsNotifier.comparisonGenerationEntries.value[index]
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                MyLabelText(
                    label = "${stringResource(id = R.string.input)}:",
                    text = "\n${modelComparedGenerationItem.input}"
                )
                MySpacer(type = "small")
                SelectionContainer {
                    MyLabelText(
                        label = "${stringResource(id = R.string.output)}:",
                        text = "\n${modelComparedGenerationItem.output}"
                    )
                }

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    MyOutlinedButton(text = stringResource(id = R.string.delete)) {
                        SettingsNotifier.deleteComparisonGenerationEntry(index)
                    }
                }
                Divider(color = Blue, thickness = 2.dp)
            }
        }
    }
}