package com.appsfourlife.draftogo.home.listitems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.MyCardView
import com.appsfourlife.draftogo.components.MyImage
import com.appsfourlife.draftogo.components.MyText
import com.appsfourlife.draftogo.components.MyUrlImage
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.Screens
import com.appsfourlife.draftogo.util.SettingsNotifier

@Composable
fun FavoriteTemplateItem(
    modifier: Modifier = Modifier,
    imageID: Any,
    text: String,
    onFavoriteIconClick: (String) -> Unit
) {

    MyCardView(modifier = modifier
        .fillMaxSize()
        .clickable {
            when (text) {
                App.getTextFromString(R.string.write_an_email) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenEmail.route)
                }
                App.getTextFromString(R.string.write_a_blog) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenBlog.route)
                }
                App.getTextFromString(R.string.write_an_essay) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenEssay.route)
                }
                App.getTextFromString(R.string.write_an_article) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenArticle.route)
                }
                App.getTextFromString(R.string.write_a_letter) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenLetter.route)
                }
                App.getTextFromString(R.string.write_a_cv) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenCV.route)
                }
                App.getTextFromString(R.string.write_a_resume) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenResume.route)
                }
                App.getTextFromString(R.string.write_a_personal_bio) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenPersonalBio.route)
                }
                App.getTextFromString(R.string.write_a_tweet) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenTwitter.route)
                }
                App.getTextFromString(R.string.write_a_viral_tiktok_captions) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenTiktok.route)
                }
                App.getTextFromString(R.string.write_an_instagram_caption) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenInstagram.route)
                }

                App.getTextFromString(R.string.write_a_facebook_post) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenFacebook.route)
                }

                App.getTextFromString(R.string.write_a_linkedin_post) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenLinkedIn.route)
                }

                App.getTextFromString(R.string.write_a_youtube_caption) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenYoutube.route)
                }

                App.getTextFromString(R.string.write_a_podcast_top_bar) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenPodcast.route)
                }

                App.getTextFromString(R.string.write_a_game_script_top_label) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenGame.route)
                }

                App.getTextFromString(R.string.write_a_poem) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenPoem.route)
                }

                App.getTextFromString(R.string.write_a_song) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenSong.route)
                }
                App.getTextFromString(R.string.write_a_code) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenCode.route)
                }
                App.getTextFromString(R.string.custom) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenCustom.route)
                }
                App.getTextFromString(R.string.summarize_the_following_text) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenSummarize.route)
                }
                App.getTextFromString(R.string.correct_the_following_text) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenGrammar.route)
                }
                App.getTextFromString(R.string.translate_the_following_text) -> {
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenTranslate.route)
                }
                else -> {
                    SettingsNotifier.currentUserQuerySection = text
                    SettingsNotifier.navHostController!!.navigate(Screens.ScreenUserAddedTemplate.route)
                }
            }

        }) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpacersSize.medium),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                if (imageID is Int) {
                    MyImage(imageID = imageID, contentDesc = "")
                } else {
                    MyUrlImage(imageUrl = imageID.toString(), contentDesc = "")
                }
                MyText(text = text)
            }
            MyImage(imageID = R.drawable.icon_favorite, contentDesc = "", modifier = Modifier.clickable {
                onFavoriteIconClick(text)
            })
        }
    }
}