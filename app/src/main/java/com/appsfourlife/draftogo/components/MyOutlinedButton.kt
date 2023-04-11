package com.appsfourlife.draftogo.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes

@Composable
fun MyOutlinedButton(
    modifier: Modifier = Modifier,
    text: String,
    isEnabled: Boolean = true,
    borderColor: Color = Blue,
    textColor: Color = Color.Black,
    onCLick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        onClick = {
            onCLick()
        },
        shape = Shapes.medium,
        enabled = isEnabled,
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
        border = BorderStroke(2.dp, borderColor)
    ) {
        MyText(text = text, color = textColor)
    }
}