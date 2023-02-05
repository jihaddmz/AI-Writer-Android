package com.jihad.aiwriter.components

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jihad.aiwriter.ui.theme.Blue
import com.jihad.aiwriter.ui.theme.Shapes

@Composable
fun MyIconButton(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    onClick: () -> Unit,
) {

    Button(
        modifier = modifier,
        shape = Shapes.medium,
        colors = ButtonDefaults.buttonColors(backgroundColor = Blue),
        onClick = {
            onClick()
        }
    ) {
        content()
    }

}