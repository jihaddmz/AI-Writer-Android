package com.appsfourlife.draftogo.components

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.appsfourlife.draftogo.ui.theme.Blue

@Composable
fun MyLabelText(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    textColor: Color = Color.Black,
    textWeight: FontWeight = FontWeight.Normal,
    labelColor: Color = Blue,
    labelWeight: FontWeight = FontWeight.Bold
) {

    Row(modifier = modifier) {
        MyText(text = label, color = labelColor, fontWeight = labelWeight, textAlign = TextAlign.Center)

        MyText(text = text, color = textColor, fontWeight = textWeight, textAlign = TextAlign.Center)
    }
}