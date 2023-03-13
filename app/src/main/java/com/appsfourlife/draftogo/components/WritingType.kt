package com.appsfourlife.draftogo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.R

@Composable
fun WritingType(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {

    Card(modifier = modifier
        .defaultMinSize(minHeight = 100.dp)
        .clickable {
            onClick()
        }, shape = Shapes.medium, backgroundColor = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val imageID = when (text) {
                stringResource(id = R.string.write_an_email) -> R.drawable.icon_email
                stringResource(id = R.string.write_a_blog_top_bar) -> R.drawable.icon_blog
                stringResource(id = R.string.write_an_essay) -> R.drawable.icon_essay
                stringResource(id = R.string.write_an_article_top_bar) -> R.drawable.icon_article
                stringResource(id = R.string.write_a_letter) -> R.drawable.emoji_letter
                stringResource(id = R.string.write_a_cv) -> R.drawable.icon_cv
                stringResource(id = R.string.write_a_resume) -> R.drawable.icon_resume
                stringResource(id = R.string.write_a_personal_bio_top_bar) -> R.drawable.social_bio
                stringResource(id = R.string.write_a_tweet_top_bar) -> R.drawable.icon_logo_twitter
                stringResource(id = R.string.write_a_viral_tiktok_captions_top_bar) -> R.drawable.icon_tiktok
                stringResource(id = R.string.write_an_instagram_caption_top_bar) -> R.drawable.icon_instagram
                stringResource(id = R.string.write_a_facebook_post_top_bar) -> R.drawable.icon_facebook
                stringResource(id = R.string.write_a_youtube_caption_top_bar) -> R.drawable.icon_youtube
                stringResource(id = R.string.write_a_podcast_top_bar) -> R.drawable.icon_podcast
                stringResource(id = R.string.write_a_game_script_top_label) -> R.drawable.icon_video_game
                stringResource(id = R.string.write_a_poem_top_bar) -> R.drawable.icon_poem_heart
                stringResource(id = R.string.write_a_song_top_bar) -> R.drawable.icon_song
                stringResource(id = R.string.write_a_code) -> R.drawable.icon_code
                stringResource(id = R.string.custom) -> R.drawable.icon_customize
                else -> {
                    R.drawable.icon_customize
                }
            }

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