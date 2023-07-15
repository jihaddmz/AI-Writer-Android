package com.appsfourlife.draftogo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.appsfourlife.draftogo.helpers.WindowInfo
import com.appsfourlife.draftogo.helpers.rememberWindowInfo

@Composable
fun MyUrlImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentDesc: String,
    contentScale: ContentScale = ContentScale.Crop,
    baseSize: Dp = 32.dp
) {
    val imageSize =
        when (rememberWindowInfo().screenWidthInfo) {
            is WindowInfo.WindowType.Compact -> baseSize
            is WindowInfo.WindowType.Medium -> baseSize + 18.dp
            else -> baseSize + 38.dp
        }

    Image(
        modifier = modifier.size(imageSize),
        painter = rememberAsyncImagePainter(imageUrl),
        contentDescription = contentDesc,
        contentScale = contentScale
    )
}