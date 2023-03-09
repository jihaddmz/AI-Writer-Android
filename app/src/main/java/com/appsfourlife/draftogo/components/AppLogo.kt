package com.appsfourlife.draftogo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.helpers.WindowInfo
import com.appsfourlife.draftogo.helpers.rememberWindowInfo
import com.appsfourlife.draftogo.ui.theme.Shapes

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


    Image(painter = painterResource(id = imageID), modifier = modifier.size(imageSize).clip(Shapes.small), contentDescription = contentDesc)

}