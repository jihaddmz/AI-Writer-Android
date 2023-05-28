package com.appsfourlife.draftogo.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Gray
import com.appsfourlife.draftogo.ui.theme.Shapes

@Composable
fun MyOutlinedButton(
    modifier: Modifier = Modifier,
    text: String,
    isEnabled: MutableState<Boolean> = mutableStateOf(true),
    textColor: Color = Color.Black,
    onCLick: () -> Unit
) {
    val borderStroke = remember {
        mutableStateOf(
            BorderStroke(
                1.dp, brush = Brush.horizontalGradient(
                    listOf(Blue, Color.Cyan),
                    startX = 50f,
                    endX = Float.POSITIVE_INFINITY
                )
            )
        )
    }

    if (!isEnabled.value)
        borderStroke.value = BorderStroke(
            1.dp, color = Gray
        )
    else {
        borderStroke.value = BorderStroke(
            1.dp, brush = Brush.horizontalGradient(
                listOf(Blue, Color.Cyan),
                startX = 50f,
                endX = Float.POSITIVE_INFINITY
            )
        )
    }

    OutlinedButton(
        modifier = modifier,
        onClick = {
            onCLick()
        },
        shape = Shapes.medium,
        enabled = isEnabled.value,
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
        border = borderStroke.value
    ) {
        MyText(text = text, color = textColor)
    }
}