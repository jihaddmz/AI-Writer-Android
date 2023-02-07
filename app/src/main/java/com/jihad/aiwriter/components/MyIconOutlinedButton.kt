package com.jihad.aiwriter.components

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MyIconOutlinedButton(
    modifier: Modifier = Modifier,
    imageID: Int,
    contentDesc: String,
    onClick: () -> Unit,
) {

    OutlinedButton(
        modifier = modifier.wrapContentSize(),
        shape = CircleShape,
        border = null,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        onClick = {
            onClick()
        }
    ) {
        MyIcon(iconID = imageID, contentDesc = contentDesc)
    }

}