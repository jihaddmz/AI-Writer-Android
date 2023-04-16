package com.appsfourlife.draftogo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.appsfourlife.draftogo.helpers.WindowInfo
import com.appsfourlife.draftogo.helpers.rememberWindowInfo

@Composable
fun MyUrlSvg(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentDesc: String,
    contentScale: ContentScale = ContentScale.Crop
) {

    val imageSize =
        when (rememberWindowInfo().screenWidthInfo) {
            is WindowInfo.WindowType.Compact -> 32.dp
            is WindowInfo.WindowType.Medium -> 50.dp
            else -> 70.dp
        }

    Image(
        modifier = modifier.size(imageSize),
        painter = rememberAsyncImagePainter(model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .decoderFactory(SvgDecoder.Factory())
            .build()),
        contentDescription = contentDesc,
        contentScale = contentScale
    )
}