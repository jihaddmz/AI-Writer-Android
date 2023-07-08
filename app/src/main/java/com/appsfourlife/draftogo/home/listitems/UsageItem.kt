package com.appsfourlife.draftogo.home.listitems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.MyCardView
import com.appsfourlife.draftogo.components.MyImage
import com.appsfourlife.draftogo.components.MyText
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.BottomNavScreens

@Composable
fun UsageItem(
    nb: Int,
    text: String,
    imageID: Int,
    navController: NavController
) {
    MyCardView(modifier = Modifier
        .padding(SpacersSize.small)
        .clickable {
            when (text) {
                App.getTextFromString(R.string.chat) -> navController.navigate(BottomNavScreens.Chat.route)
                App.getTextFromString(R.string.completion) -> navController.navigate(
                    BottomNavScreens.Content.route
                )

                App.getTextFromString(R.string.arts) -> navController.navigate(BottomNavScreens.Art.route)
                App.getTextFromString(R.string.videos) -> navController.navigate(
                    BottomNavScreens.Video.route
                )
            }
        })

    {
        Row(
            modifier = Modifier.padding(SpacersSize.small),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyImage(imageID = imageID, contentDesc = "")
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MyText(text = "$nb", textAlign = TextAlign.Center)
                MyText(text = text)
            }
        }
    }
}