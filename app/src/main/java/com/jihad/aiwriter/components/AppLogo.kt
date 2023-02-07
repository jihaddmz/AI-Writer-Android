package com.jihad.aiwriter.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jihad.aiwriter.helpers.WindowInfo
import com.jihad.aiwriter.helpers.rememberWindowInfo

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