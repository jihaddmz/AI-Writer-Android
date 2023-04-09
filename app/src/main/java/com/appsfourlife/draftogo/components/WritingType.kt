package com.appsfourlife.draftogo.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WritingType(
    modifier: Modifier = Modifier,
    text: String,
    imageUrl: String,
    onLongClick: () -> Unit,
    onClick: () -> Unit,
) {

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
                stringResource(id = R.string.summarize_this) -> R.drawable.icon_summarize
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

            if (imageID != 0)
                MyImage(
                    modifier = Modifier,
                    imageID = imageID,
                    contentDesc = text
                )
            else
                MyUrlImage(imageUrl = imageUrl, contentDesc = text)

            Spacer(modifier = Modifier.height(SpacersSize.small))

            MyText(text = text, textAlign = TextAlign.Center)
        }
    }
}