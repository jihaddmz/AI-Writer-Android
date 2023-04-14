package com.appsfourlife.draftogo.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.MyIcon
import com.appsfourlife.draftogo.components.MySpacer
import com.appsfourlife.draftogo.components.MyText
import com.appsfourlife.draftogo.components.MyTextTitle
import com.appsfourlife.draftogo.home.listitems.FavoriteTemplateItem
import com.appsfourlife.draftogo.home.model.ModelFavoriteTemplate
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
@Preview
fun BottomSheetFavoriteTemplates(
    modifier: Modifier = Modifier
) {

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

        // todo get all favorite templates
        val listOfFavoriteTemplates = listOf(
            ModelFavoriteTemplate(
                imageID = R.drawable.icon_article,
                text = stringResource(id = R.string.write_an_article)
            ),
            ModelFavoriteTemplate(
                imageID = R.drawable.icon_logo_twitter,
                text = stringResource(id = R.string.write_a_tweet)
            ),
            ModelFavoriteTemplate(
                imageID = R.drawable.icon_game_script,
                text = stringResource(id = R.string.write_a_game_script_top_label)
            ),
        )

        LazyColumn(content = {
            items(listOfFavoriteTemplates.size) { index ->
                val current = listOfFavoriteTemplates[index]

                FavoriteTemplateItem(
                    imageID = current.imageID,
                    text = current.text,
                    onFavoriteIconClick = {

                    }
                )

                MySpacer(type = "small")
            }
        })
    }
}