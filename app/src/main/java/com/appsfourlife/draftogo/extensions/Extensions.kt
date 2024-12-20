package com.appsfourlife.draftogo.extensions

import androidx.compose.animation.core.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.WritingType
import com.appsfourlife.draftogo.data.model.ModelTemplate
import com.appsfourlife.draftogo.util.Screens
import com.appsfourlife.draftogo.util.SettingsNotifier

fun LazyGridScope.sectionsGridContent(
    list: List<ModelTemplate>,
    navController: NavController,
) {
    if (list.isNotEmpty())
        items(list.size, key = { list[it].query }) { index ->
            val text = list[index].query
            val imageUrl = list[index].imageUrl
            val userAdded = list[index].userAdded
            WritingType(
                modifier = Modifier,
                text = text,
                imageUrl = imageUrl,
                onLongClick = {
                    SettingsNotifier.showDeleteTemplateDialog.value = true
                    SettingsNotifier.templateToDelete =
                        ModelTemplate(query = text, imageUrl, userAdded)
                }
            ) {
                when (text) {
                    App.getTextFromString(R.string.write_an_email) -> {
                        navController.navigate(Screens.ScreenEmail.route)
                    }
                    App.getTextFromString(R.string.write_a_blog) -> {
                        navController.navigate(Screens.ScreenBlog.route)
                    }
                    App.getTextFromString(R.string.write_an_essay) -> {
                        navController.navigate(Screens.ScreenEssay.route)
                    }
                    App.getTextFromString(R.string.write_an_article) -> {
                        navController.navigate(Screens.ScreenArticle.route)
                    }
                    App.getTextFromString(R.string.write_a_letter) -> {
                        navController.navigate(Screens.ScreenLetter.route)
                    }
                    App.getTextFromString(R.string.write_a_cv) -> {
                        navController.navigate(Screens.ScreenCV.route)
                    }
                    App.getTextFromString(R.string.write_a_resume) -> {
                        navController.navigate(Screens.ScreenResume.route)
                    }
                    App.getTextFromString(R.string.write_a_personal_bio) -> {
                        navController.navigate(Screens.ScreenPersonalBio.route)
                    }
                    App.getTextFromString(R.string.write_a_tweet) -> {
                        navController.navigate(Screens.ScreenTwitter.route)
                    }
                    App.getTextFromString(R.string.write_a_viral_tiktok_captions) -> {
                        navController.navigate(Screens.ScreenTiktok.route)
                    }
                    App.getTextFromString(R.string.write_an_instagram_caption) -> {
                        navController.navigate(Screens.ScreenInstagram.route)
                    }

                    App.getTextFromString(R.string.write_a_facebook_post) -> {
                        navController.navigate(Screens.ScreenFacebook.route)
                    }

                    App.getTextFromString(R.string.write_a_linkedin_post) -> {
                        navController.navigate(Screens.ScreenLinkedIn.route)
                    }

                    App.getTextFromString(R.string.write_a_youtube_caption) -> {
                        navController.navigate(Screens.ScreenYoutube.route)
                    }

                    App.getTextFromString(R.string.write_a_podcast_top_bar) -> {
                        navController.navigate(Screens.ScreenPodcast.route)
                    }

                    App.getTextFromString(R.string.write_a_game_script_top_label) -> {
                        navController.navigate(Screens.ScreenGame.route)
                    }

                    App.getTextFromString(R.string.write_a_poem) -> {
                        navController.navigate(Screens.ScreenPoem.route)
                    }

                    App.getTextFromString(R.string.write_a_song) -> {
                        navController.navigate(Screens.ScreenSong.route)
                    }
                    App.getTextFromString(R.string.write_a_code) -> {
                        navController.navigate(Screens.ScreenCode.route)
                    }
                    App.getTextFromString(R.string.custom) -> {
                        navController.navigate(Screens.ScreenCustom.route)
                    }
                    App.getTextFromString(R.string.summarize_the_following_text) -> {
                        navController.navigate(Screens.ScreenSummarize.route)
                    }
                    App.getTextFromString(R.string.correct_the_following_text) -> {
                        navController.navigate(Screens.ScreenGrammar.route)
                    }
                    App.getTextFromString(R.string.translate_the_following_text) -> {
                        navController.navigate(Screens.ScreenTranslate.route)
                    }
                    else -> {
                        SettingsNotifier.currentUserQuerySection = text
                        navController.navigate(Screens.ScreenUserAddedTemplate.route)
                    }
                }

            }
        }
}

@Composable
private fun LazyListState.calculateDelayAndEasing(index: Int, columnCount: Int): Pair<Int, Easing> {
    val row = index / columnCount
    val column = index % columnCount
    val firstVisibleRow = firstVisibleItemIndex
    val visibleRows = layoutInfo.visibleItemsInfo.count()
    val scrollingToBottom = firstVisibleRow < row
    val isFirstLoad = visibleRows == 0
    val rowDelay = 200 * when {
        isFirstLoad -> row // initial load
        scrollingToBottom -> visibleRows + firstVisibleRow - row // scrolling to bottom
        else -> 1 // scrolling to top
    }
    val scrollDirectionMultiplier = if (scrollingToBottom || isFirstLoad) 1 else -1
    val columnDelay = column * 150 * scrollDirectionMultiplier
    val easing =
        if (scrollingToBottom || isFirstLoad) LinearOutSlowInEasing else FastOutSlowInEasing
    return rowDelay + columnDelay to easing
}

private enum class State { PLACING, PLACED }

data class ScaleAndAlphaArgs(
    val fromScale: Float,
    val toScale: Float,
    val fromAlpha: Float,
    val toAlpha: Float
)

@Composable
fun scaleAndAlpha(
    args: ScaleAndAlphaArgs,
    animation: FiniteAnimationSpec<Float>
): Pair<Float, Float> {
    val transitionState =
        remember { MutableTransitionState(State.PLACING).apply { targetState = State.PLACED } }
    val transition = updateTransition(transitionState, label = "")
    val alpha by transition.animateFloat(transitionSpec = { animation }, label = "") { state ->
        when (state) {
            State.PLACING -> args.fromAlpha
            State.PLACED -> args.toAlpha
        }
    }
    val scale by transition.animateFloat(transitionSpec = { animation }, label = "") { state ->
        when (state) {
            State.PLACING -> args.fromScale
            State.PLACED -> args.toScale
        }
    }
    return alpha to scale
}