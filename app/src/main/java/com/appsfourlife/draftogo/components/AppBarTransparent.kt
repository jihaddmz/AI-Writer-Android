package com.appsfourlife.draftogo.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppBarTransparent(
    modifier: Modifier = Modifier,
    title: String,
    showSidebar: Boolean = false,
    scaffoldState: ScaffoldState? = null,
    onBackBtnClick: () -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = SpacersSize.small,
                bottom = SpacersSize.medium
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        if (showSidebar) {
            IconButton(onClick = {
                coroutineScope.launch {
                    scaffoldState?.drawerState?.animateTo(DrawerValue.Open, tween(durationMillis = 1000))
                }
            }) {
                MyIcon(
                    iconID = R.drawable.icon_sidebar,
                    contentDesc = "sidebar",
                    tint = Color.Black
                )
            }
            MySpacer(type = "medium", widthOrHeight = "width")
        }
        Text(
            modifier = Modifier,
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

}