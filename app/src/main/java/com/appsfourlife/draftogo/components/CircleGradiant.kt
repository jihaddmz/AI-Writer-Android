package com.appsfourlife.draftogo.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
@Preview
fun CircleGradiant(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp, brush = Brush.horizontalGradient(
                    listOf(Blue, Color.Cyan),
                    startX = 50f,
                    endX = Float.POSITIVE_INFINITY
                ),
                shape = CircleShape
            )
            .padding(SpacersSize.small),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}