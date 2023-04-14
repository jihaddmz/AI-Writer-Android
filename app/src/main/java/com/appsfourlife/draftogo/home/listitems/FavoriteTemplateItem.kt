package com.appsfourlife.draftogo.home.listitems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.MyCardView
import com.appsfourlife.draftogo.components.MyImage
import com.appsfourlife.draftogo.components.MyText
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun FavoriteTemplateItem(
    modifier: Modifier = Modifier,
    imageID: Int,
    text: String,
    onFavoriteIconClick: (String) -> Unit
) {
    MyCardView(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxSize().padding(SpacersSize.medium),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                MyImage(imageID = imageID, contentDesc = "")
                MyText(text = text)
            }
            MyImage(imageID = R.drawable.icon_favorite, contentDesc = "", modifier = Modifier.clickable {
                onFavoriteIconClick(text)
            })
        }
    }
}