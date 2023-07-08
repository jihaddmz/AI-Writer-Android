package com.appsfourlife.draftogo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun AppBarTransparent(
    modifier: Modifier = Modifier,
    title: String,
    onBackBtnClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = SpacersSize.small, top = SpacersSize.small, bottom = SpacersSize.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
//        IconButton(onClick = {
//            onBackBtnClick()
//
//        }) {
//            MyIcon(
//                iconID = R.drawable.icon_arrow_back,
//                contentDesc = "back",
//                tint = Color.Black
//            )
//        }
//        MySpacer(type = "small", widthOrHeight = "width")
        Text(
            modifier = Modifier.padding(top = SpacersSize.small),
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
        )
    }
}