package com.appsfourlife.draftogo.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelFavoriteTemplate
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WritingType(
    modifier: Modifier = Modifier,
    text: String,
    imageUrl: String,
    onLongClick: () -> Unit,
    onClick: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = modifier
            .defaultMinSize(minHeight = 100.dp)
            .combinedClickable(onClick = {
                onClick()
            }, onLongClick = {
                onLongClick()
            }), shape = Shapes.medium, backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val imageID = when (text) {
                stringResource(id = R.string.write_an_email) -> R.drawable.icon_email
                stringResource(id = R.string.write_a_blog) -> R.drawable.icon_blog
                stringResource(id = R.string.write_an_essay) -> R.drawable.icon_essay
                stringResource(id = R.string.write_an_article) -> R.drawable.icon_article
                stringResource(id = R.string.write_a_letter) -> R.drawable.icon_letter
                stringResource(id = R.string.write_a_cv) -> R.drawable.icon_cv
                stringResource(id = R.string.write_a_resume) -> R.drawable.icon_resume
                stringResource(id = R.string.write_a_personal_bio) -> R.drawable.icon_personal_bio
                stringResource(id = R.string.summarize_the_following_text) -> R.drawable.icon_summarize
                stringResource(id = R.string.correct_the_following_text) -> R.drawable.icon_grammar
                stringResource(id = R.string.translate_the_following_text) -> R.drawable.icon_translate
                stringResource(id = R.string.write_a_tweet) -> R.drawable.icon_logo_twitter
                stringResource(id = R.string.write_a_viral_tiktok_captions) -> R.drawable.icon_tiktok
                stringResource(id = R.string.write_an_instagram_caption) -> R.drawable.icon_instagram
                stringResource(id = R.string.write_a_facebook_post) -> R.drawable.icon_facebook
                stringResource(id = R.string.write_a_linkedin_post) -> R.drawable.icon_linkedin
                stringResource(id = R.string.write_a_youtube_caption) -> R.drawable.icon_youtube
                stringResource(id = R.string.write_a_podcast_top_bar) -> R.drawable.icon_podcast
                stringResource(id = R.string.write_a_game_script_top_label) -> R.drawable.icon_game_script
                stringResource(id = R.string.write_a_poem) -> R.drawable.icon_poem
                stringResource(id = R.string.write_a_song) -> R.drawable.icon_song
                stringResource(id = R.string.write_a_code) -> R.drawable.icon_code
                stringResource(id = R.string.custom) -> R.drawable.icon_custom
                else -> {
                    if (imageUrl.isEmpty())
                        R.drawable.icon_custom
                    else
                        0
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                val starIconId = remember {
                    mutableStateOf(R.drawable.icon_outlined_star)
                }

                /**
                 * checking if this entry is in the database make the star icon filled, otherwise use the outlined
                 * icon
                 **/
                LaunchedEffect(key1 = true, block = {
                    coroutineScope.launch(Dispatchers.IO) {
                        if (App.databaseApp.daoApp.getFavoriteTemplate(text) != null) {
                            starIconId.value = R.drawable.icon_favorite
                        }
                    }
                })

                if (imageID != 0)
                    MyImage(
                        modifier = Modifier,
                        imageID = imageID,
                        contentDesc = text
                    )
                else
                    MyUrlSvg(imageUrl = imageUrl, contentDesc = text)

                MyImage(
                    imageID = starIconId.value,
                    contentDesc = "",
                    modifier = Modifier.clickable {
                        if (starIconId.value == R.drawable.icon_outlined_star) {
                            starIconId.value = R.drawable.icon_favorite
                            coroutineScope.launch(Dispatchers.IO) {
                                if (imageUrl.isEmpty())
                                    App.databaseApp.daoApp.insertFavoriteTemplate(ModelFavoriteTemplate(text, imageID, null))
                                else
                                    App.databaseApp.daoApp.insertFavoriteTemplate(ModelFavoriteTemplate(text, null, imageUrl))
                            }
                        }
                        else {
                            starIconId.value = R.drawable.icon_outlined_star
                            coroutineScope.launch(Dispatchers.IO) {
                                if (imageUrl.isEmpty())
                                    App.databaseApp.daoApp.deleteFavoriteTemplate(ModelFavoriteTemplate(text, imageID, null))
                                else
                                    App.databaseApp.daoApp.deleteFavoriteTemplate(ModelFavoriteTemplate(text, null, imageUrl))
                            }
                        }

                    })
            }

            Spacer(modifier = Modifier.height(SpacersSize.small))

            MyText(text = text, textAlign = TextAlign.Center)
        }
    }
}