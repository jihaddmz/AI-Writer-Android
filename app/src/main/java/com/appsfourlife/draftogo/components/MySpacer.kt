package com.appsfourlife.draftogo.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.helpers.WindowInfo
import com.appsfourlife.draftogo.helpers.rememberWindowInfo

@Composable
fun MySpacer(
    modifier: Modifier = Modifier,
    type: String,
    widthOrHeight: String = "height"
) {
    if (widthOrHeight == "height") {
        val height: Dp

        if (type.lowercase() == "small") {
            height = when (rememberWindowInfo().screenWidthInfo) {
                is WindowInfo.WindowType.Compact -> 8.dp
                is WindowInfo.WindowType.Medium -> 20.dp
                else -> 40.dp
            }
        } else if (type.lowercase() == "medium") {
            height = when (rememberWindowInfo().screenWidthInfo) {
                is WindowInfo.WindowType.Compact -> 20.dp
                is WindowInfo.WindowType.Medium -> 40.dp
                else -> 60.dp
            }
        } else {
            height = when (rememberWindowInfo().screenWidthInfo) {
                is WindowInfo.WindowType.Compact -> 30.dp
                is WindowInfo.WindowType.Medium -> 50.dp
                else -> 70.dp
            }
        }

        Spacer(modifier = modifier.height(height))
    }else{
        val width: Dp

        if (type.lowercase() == "small") {
            width = when (rememberWindowInfo().screenWidthInfo) {
                is WindowInfo.WindowType.Compact -> 8.dp
                is WindowInfo.WindowType.Medium -> 20.dp
                else -> 40.dp
            }
        } else if (type.lowercase() == "medium") {
            width = when (rememberWindowInfo().screenWidthInfo) {
                is WindowInfo.WindowType.Compact -> 20.dp
                is WindowInfo.WindowType.Medium -> 40.dp
                else -> 60.dp
            }
        } else {
            width = when (rememberWindowInfo().screenWidthInfo) {
                is WindowInfo.WindowType.Compact -> 30.dp
                is WindowInfo.WindowType.Medium -> 50.dp
                else -> 70.dp
            }
        }

        Spacer(modifier = modifier.width(width))
    }
}