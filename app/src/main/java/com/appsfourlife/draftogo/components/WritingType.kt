package com.appsfourlife.draftogo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun WritingType(
    modifier: Modifier = Modifier,
    text: String,
    imageID: Int,
    onClick: () -> Unit
) {

    Card(modifier = modifier.defaultMinSize(minHeight = 100.dp).clickable {
        onClick()
    }, shape = Shapes.medium, backgroundColor = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MyImage(
                modifier = Modifier,
                imageID = imageID,
                contentDesc = text
            )

            Spacer(modifier = Modifier.height(SpacersSize.small))

            MyText(text = text, textAlign = TextAlign.Center)
        }
    }
}