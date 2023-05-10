package com.appsfourlife.draftogo.components

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.helpers.HelperIntent

@Composable
fun IconLink(modifier: Modifier = Modifier, url: String) {
    MyIcon(iconID = R.drawable.icon_link, contentDesc = "", modifier = modifier.clickable {
        HelperIntent.navigateToUrl(url)
    })
}