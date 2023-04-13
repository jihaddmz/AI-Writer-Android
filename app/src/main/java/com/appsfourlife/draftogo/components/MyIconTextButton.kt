package com.appsfourlife.draftogo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun MyIconTextButton(
    modifier: Modifier = Modifier,
    iconID: Int,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .background(color = Blue, shape = Shapes.medium)
            .padding(SpacersSize.small)
            .clickable {
                onClick()
            }, horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyImage(imageID = iconID, contentDesc = "")
        MyText(text = text, color = Color.White)
    }
}