package com.appsfourlife.draftogo.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes

@Composable
fun MyOutlinedButton(
    modifier: Modifier = Modifier,
    text: String,
    isEnabled: Boolean = true,
    onCLick: () -> Unit
) {
    OutlinedButton(
        onClick = {
            onCLick()
        },
        shape = Shapes.medium,
        enabled = isEnabled,
        border = BorderStroke(2.dp, Blue)
    ) {
        MyText(text = text, color = Blue)
    }
}