package com.appsfourlife.draftogo.components

import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.ui.theme.Glass
import com.appsfourlife.draftogo.ui.theme.SheetShape

/**
 * this is a bottom sheet holder for the art feature
 **/

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    peekSize: Dp = 0.dp,
    sheetScaffoldState: BottomSheetScaffoldState,
    bottomSheet: @Composable () -> Unit = { BottomSheetSavedOutputs() },
    content: @Composable () -> Unit
) {
    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = sheetScaffoldState,
        sheetShape = SheetShape,
        sheetContent = { bottomSheet() },
        sheetPeekHeight = peekSize,
        backgroundColor = Glass
    ) {
        content()
    }
}