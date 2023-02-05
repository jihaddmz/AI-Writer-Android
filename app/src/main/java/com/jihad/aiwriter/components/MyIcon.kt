package com.jihad.aiwriter.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jihad.aiwriter.helpers.WindowInfo
import com.jihad.aiwriter.helpers.rememberWindowInfo
import com.jihad.aiwriter.ui.theme.Blue

@Composable
fun MyIcon(
    modifier: Modifier = Modifier,
    tint: Color = Blue,
    iconID: Int,
    contentDesc: String
) {

    val size = when (rememberWindowInfo().screenWidthInfo) {
        is WindowInfo.WindowType.Compact -> 25.dp
        is WindowInfo.WindowType.Medium -> 35.dp
        else -> 45.dp
    }

    Icon(modifier = modifier.size(size), tint = tint, painter = painterResource(id = iconID), contentDescription = contentDesc)

}