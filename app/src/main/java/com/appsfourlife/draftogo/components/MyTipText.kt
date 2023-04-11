package com.appsfourlife.draftogo.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MyTipText(
    modifier: Modifier = Modifier,
    text: String
) {
    MyText(
        modifier = modifier,
        text = text,
        color = Color.LightGray
    )
}