package com.appsfourlife.draftogo.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes

@Composable
fun MyCardView(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .border(
                1.dp,
                shape = Shapes.medium,
                brush = Brush.horizontalGradient(
                    listOf(Blue, Color.Cyan),
                    startX = 50f,
                    endX = Float.POSITIVE_INFINITY
                )
            )
    ) {
        content()
    }
}