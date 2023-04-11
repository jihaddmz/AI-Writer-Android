package com.appsfourlife.draftogo.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import com.appsfourlife.draftogo.ui.theme.Blue

@Composable
fun MyTextLink(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start
) {
    MyText(text = text, color = Blue, textDecoration = TextDecoration.Underline, modifier = modifier, textAlign = textAlign)
}