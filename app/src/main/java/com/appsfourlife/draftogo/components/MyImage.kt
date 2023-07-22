package com.appsfourlife.draftogo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.helpers.WindowInfo
import com.appsfourlife.draftogo.helpers.rememberWindowInfo

@Composable
fun MyImage(
    modifier: Modifier = Modifier,
    imageID: Int, contentDesc: String,
    contentScale: ContentScale = ContentScale.None,
    baseSize: Dp = 32.dp
) {

    val imageSize =
        when (rememberWindowInfo().screenWidthInfo) {
            is WindowInfo.WindowType.Compact -> baseSize
            is WindowInfo.WindowType.Medium -> (baseSize.value + 18f).dp
            else -> (baseSize.value + 38f).dp
        }

    Image(
        modifier = modifier.size(imageSize),
        painter = painterResource(id = imageID),
        contentDescription = contentDesc,
        contentScale = contentScale
    )
}