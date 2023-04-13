package com.appsfourlife.draftogo.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun MyTipText(
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    text: String
) {
    MyText(
        modifier = modifier,
        text = text,
        color = Color.LightGray,
        textAlign = textAlign
    )
}