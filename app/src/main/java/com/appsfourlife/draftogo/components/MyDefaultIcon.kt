package com.appsfourlife.draftogo.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.helpers.WindowInfo
import com.appsfourlife.draftogo.helpers.rememberWindowInfo

@Composable
fun MyDefaultIcon(
    modifier: Modifier = Modifier,
    iconID: Int,
    contentDesc: String
) {

    val size = when (rememberWindowInfo().screenWidthInfo) {
        is WindowInfo.WindowType.Compact -> 25.dp
        is WindowInfo.WindowType.Medium -> 35.dp
        else -> 45.dp
    }

    Icon(modifier = modifier.size(size), painter = painterResource(id = iconID), contentDescription = contentDesc)

}