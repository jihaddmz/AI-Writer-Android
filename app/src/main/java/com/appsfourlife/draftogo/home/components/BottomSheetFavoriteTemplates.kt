package com.appsfourlife.draftogo.home.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.MyIcon
import com.appsfourlife.draftogo.components.MySpacer
import com.appsfourlife.draftogo.components.MyText
import com.appsfourlife.draftogo.components.MyTextTitle
import com.appsfourlife.draftogo.data.model.ModelFavoriteTemplate
import com.appsfourlife.draftogo.home.listitems.FavoriteTemplateItem
import com.appsfourlife.draftogo.home.util.NotifiersHome.listOfFavoriteTemplates
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun BottomSheetFavoriteTemplates(
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = true, block = {
        coroutineScope.launch(Dispatchers.IO) {
            listOfFavoriteTemplates.value = App.databaseApp.daoApp.getAllFavoriteTemplates()
        }
    })

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
            MyIcon(iconID = R.drawable.icon_favorite, contentDesc = "", tint = Color.LightGray)
            MySpacer(type = "small", widthOrHeight = "width")
            MyTextTitle(text = stringResource(id = R.string.favorite_templates))
        }

        MySpacer(type = "medium")

        MyText(
            text = stringResource(id = R.string.favorite_template_bottom_sheet_explanation),
            color = Color.LightGray
        )

        MySpacer(type = "medium")

        LazyColumn(modifier = Modifier.fillMaxSize(), content = {
            items(
                count = listOfFavoriteTemplates.value.size,
                key = { listOfFavoriteTemplates.value[it].query }) { index ->
                val current = listOfFavoriteTemplates.value[index]

                FavoriteTemplateItem(
                    modifier = Modifier.animateItemPlacement(),
                    imageID = current.iconID ?: current.imageUrl!!,
                    text = current.query
                ) {
                    coroutineScope.launch(Dispatchers.IO) {
                        if (current.imageUrl.isNullOrEmpty())
                            App.databaseApp.daoApp.deleteFavoriteTemplate(
                                ModelFavoriteTemplate(
                                    current.query,
                                    current.iconID,
                                    null
                                )
                            )
                        else
                            App.databaseApp.daoApp.deleteFavoriteTemplate(
                                ModelFavoriteTemplate(
                                    current.query,
                                    null,
                                    current.imageUrl
                                )
                            )

                        listOfFavoriteTemplates.value =
                            App.databaseApp.daoApp.getAllFavoriteTemplates()

                    }
                }

                MySpacer(type = "small")
            }
        })
    }
}