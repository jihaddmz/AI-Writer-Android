package com.appsfourlife.draftogo.components

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.feature_generate_art.notifiers.NotifiersArt
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun BottomSheetArtHistory(
    modifier: Modifier = Modifier,
    sheetScaffoldState: BottomSheetScaffoldState,
    onArtHistoryClick: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    coroutineScope.launch(Dispatchers.IO) {
        NotifiersArt.listOfPromptHistory.value =
            App.databaseApp.daoApp.getAllArts().sortedBy { it.dateTime }
        NotifiersArt.listOfPromptHistory.value = NotifiersArt.listOfPromptHistory.value.reversed()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .padding(SpacersSize.medium)
    ) {

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .height(5.dp)
                    .background(shape = Shapes.large, color = Color.LightGray),
                thickness = 3.dp,
            )
        }

        MySpacer(type = "medium")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyIcon(iconID = R.drawable.icon_history, contentDesc = "", tint = Color.LightGray)
            MySpacer(type = "small", widthOrHeight = "width")
            MyTextTitle(text = stringResource(id = R.string.prompt_history))
        }

        MySpacer(type = "medium")

        MyText(
            text = stringResource(id = R.string.art_history_explanation),
            color = Color.LightGray
        )

        MySpacer(type = "medium")

        LazyVerticalGrid(cells = GridCells.Fixed(2),
            content = {

                items(NotifiersArt.listOfPromptHistory.value.size) { index ->
                    val current = NotifiersArt.listOfPromptHistory.value[index]

                    MyCardView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(SpacersSize.small)
                            .clickable {
                                coroutineScope.launch {
                                    sheetScaffoldState.bottomSheetState.collapse()
                                    onArtHistoryClick(current.prompt)
                                }
                            }
                    ) {
                        MyText(
                            text = current.prompt,
                            modifier = Modifier
                                .padding(SpacersSize.medium),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            })
    }
}