package com.appsfourlife.draftogo.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import com.appsfourlife.draftogo.ui.theme.Blue

@Composable
fun MyTextLink(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    color: Color = Blue
) {
    MyText(text = text, color = color, textDecoration = TextDecoration.Underline, modifier = modifier, textAlign = textAlign)
}