package com.jihad.aiwriter.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.jihad.aiwriter.helpers.Constants
import com.jihad.aiwriter.ui.theme.Blue
import com.jihad.aiwriter.ui.theme.Shapes

@Composable
fun MyButton(
    modifier: Modifier = Modifier,
    text: String = "",
    content: @Composable () -> Unit = {},
    onClick: () -> Unit
) {

    Button(
        modifier = modifier,
        shape = Shapes.medium,
        colors = ButtonDefaults.buttonColors(backgroundColor = Blue),
        onClick = {
            onClick()
        }
    ) {

        if (text != "")
            MyText(
                text = text,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.animateContentSize(animationSpec = tween(durationMillis = Constants.ANIMATION_LENGTH))
            )

        content()
    }
}