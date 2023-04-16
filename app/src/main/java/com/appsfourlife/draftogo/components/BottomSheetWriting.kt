package com.appsfourlife.draftogo.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.appsfourlife.draftogo.feature_generate_text.components.BottomSheetWritePricing
import com.appsfourlife.draftogo.helpers.Helpers
import com.appsfourlife.draftogo.ui.theme.Glass
import com.appsfourlife.draftogo.ui.theme.SheetShape
import com.appsfourlife.draftogo.util.SettingsNotifier

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
    val sheetScaffoldState = rememberBottomSheetScaffoldState()
    SettingsNotifier.sheetScaffoldState = sheetScaffoldState

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = sheetScaffoldState,
        sheetContent = {
            if (SettingsNotifier.isPricingBottomSheets.value) {
                Helpers.logD("1")
                BottomSheetWritePricing(sheetScaffoldState)
            } else {
                BottomSheetSavedOutputs()
                Helpers.logD("2")
            }
        },
        sheetPeekHeight = 0.dp,
        sheetShape = SheetShape,
        backgroundColor = Glass
    ) {
        content()
    }
}