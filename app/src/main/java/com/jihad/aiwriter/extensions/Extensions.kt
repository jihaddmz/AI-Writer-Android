package com.jihad.aiwriter.extensions

import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyGridScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.navigation.NavController
import com.jihad.aiwriter.components.WritingType
import com.jihad.aiwriter.feature_generate_text.util.Screens

@OptIn(ExperimentalFoundationApi::class)
fun LazyGridScope.sectionsGridContent(
    map: HashMap<Int, List<Any>>,
    columns: Int,
    state: LazyListState,
    navController: NavController,
) {
    items(map.size) { index ->
        val (delay, easing) = state.calculateDelayAndEasing(index, columns)
        val animation = tween<Float>(durationMillis = 500, delayMillis = delay, easing = easing)
        val args = ScaleAndAlphaArgs(fromScale = 2f, toScale = 1f, fromAlpha = 0f, toAlpha = 1f)
        val (scale, alpha) = scaleAndAlpha(args = args, animation = animation)
        val text = map[index]?.get(0)
        val imageID = map[index]?.get(1)
        WritingType(modifier = Modifier.graphicsLayer(alpha = alpha, scaleX = scale, scaleY = scale) , text = text.toString(), imageID = imageID as Int) {
            when (index) {
                0 -> {
                    navController.navigate(Screens.ScreenEmail.route)
                }
                1 -> {
                    navController.navigate(Screens.ScreenBlog.route)
                }
                2 -> {
                    navController.navigate(Screens.ScreenEssay.route)
                }
                3 -> {
                    navController.navigate(Screens.ScreenArticle.route)
                }
                4 -> {
                    navController.navigate(Screens.ScreenLetter.route)
                }
                5 -> {
                    navController.navigate(Screens.ScreenCV.route)
                }
                6 -> {
                    navController.navigate(Screens.ScreenResume.route)
                }
                7 -> {
                    navController.navigate(Screens.ScreenPoem.route)
                }
                8 -> {
                    navController.navigate(Screens.ScreenSong.route)
                }
                9 -> {
                    navController.navigate(Screens.ScreenTwitter.route)
                }
                10 -> {
                    navController.navigate(Screens.ScreenTiktok.route)
                }

                11 -> {
                    navController.navigate(Screens.ScreenInstagram.route)
                }

                12 -> {
                    navController.navigate(Screens.ScreenFacebook.route)
                }

                13 -> {
                    navController.navigate(Screens.ScreenYoutube.route)
                }

                14 -> {
                    navController.navigate(Screens.ScreenPersonalBio.route)
                }

                15 -> {
                    navController.navigate(Screens.ScreenCode.route)
                }

                16 -> {
                    navController.navigate(Screens.ScreenCustom.route)
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
    val easing = if (scrollingToBottom || isFirstLoad) LinearOutSlowInEasing else FastOutSlowInEasing
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
    val transitionState = remember { MutableTransitionState(State.PLACING).apply { targetState = State.PLACED } }
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