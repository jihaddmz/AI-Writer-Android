package com.appsfourlife.draftogo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.ui.theme.Glass
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopImageHeader(
    modifier: Modifier = Modifier,
    sheetScaffoldState: BottomSheetScaffoldState?,
    showQuitBtn: Boolean = true,
    drawableID: Int,
    title: String
) {
    val coroutineScope = rememberCoroutineScope()

    Card(
        elevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f),
        border = null
    ) {

        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                painter = painterResource(id = drawableID),
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Glass),
//                                startY = startY,
//                                endY = endY
                            )
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    MyTextTitle(
                        text = title,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            if (showQuitBtn)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SpacersSize.small),
                    contentAlignment = Alignment.TopStart
                ) {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            sheetScaffoldState!!.bottomSheetState.collapse()
                        }
                    }) {
                        MyIcon(iconID = R.drawable.clear, contentDesc = "", tint = Color.LightGray)
                    }
                }
        }
    }
}