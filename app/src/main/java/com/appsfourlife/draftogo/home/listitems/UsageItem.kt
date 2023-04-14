package com.appsfourlife.draftogo.home.listitems

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.appsfourlife.draftogo.components.MyCardView
import com.appsfourlife.draftogo.components.MyImage
import com.appsfourlife.draftogo.components.MyText
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun UsageItem(
    nb: Int,
    text: String,
    imageID: Int
) {
    MyCardView {
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