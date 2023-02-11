package com.appsfourlife.draftogo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.helpers.WindowInfo
import com.appsfourlife.draftogo.helpers.rememberWindowInfo

@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    imageID: Int,
    contentDesc: String
) {
    val imageSize =
        when (rememberWindowInfo().screenWidthInfo) {
            is WindowInfo.WindowType.Compact -> 100.dp
            is WindowInfo.WindowType.Medium -> 200.dp
            else -> 250.dp
        }


    Image(modifier = modifier.size(imageSize), painter = painterResource(id = imageID), contentDescription = contentDesc)

}