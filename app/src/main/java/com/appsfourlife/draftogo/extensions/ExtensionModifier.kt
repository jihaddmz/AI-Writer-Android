package com.appsfourlife.draftogo.extensions

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.util.Screens
import com.appsfourlife.draftogo.util.SettingsNotifier
import java.util.*
import kotlin.concurrent.timerTask

fun Modifier.animateScaling(): Modifier = composed {

    val scale = remember {
        mutableStateOf(false)
    }
    val scaleState = animateFloatAsState(
        targetValue = if (!scale.value) 0.1f else 1f,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(key1 = true, block = {
        scale.value = true
    })

    scale(scale = scaleState.value)
}

fun Modifier.animateScalingDecreasing(): Modifier = composed {

    val scale = remember {
        mutableStateOf(false)
    }
    val scaleState = animateFloatAsState(
        targetValue = if (!scale.value) 1f else 0.1f,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(key1 = true, block = {
        scale.value = true
    })

    scale(scale = scaleState.value)
}

fun Modifier.animateOffsetY(initialOffsetY: Dp, delay: Long = 0): Modifier = composed {

    val animate = remember {
        mutableStateOf(false)
    }

    val offsetYState = animateDpAsState(
        targetValue = if (!animate.value) initialOffsetY else 0.dp,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(key1 = true, block = {
        Timer().schedule(timerTask {
            animate.value = true
        }, delay)
    })

    offset(y = offsetYState.value)
}

fun Modifier.animateOffsetX(initialOffsetX: Dp, delay: Long = 0): Modifier = composed {

    val animate = remember {
        mutableStateOf(false)
    }

    val offsetXState = animateDpAsState(
        targetValue = if (!animate.value) initialOffsetX else 0.dp,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(key1 = true, block = {
        Timer().schedule(timerTask {
            animate.value = true
        }, delay)
    })

    offset(x = offsetXState.value)
}

fun Modifier.animateVisibility(delay: Long = 0): Modifier = composed {
    val animate = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true, block = {
        Timer().schedule(timerTask {
            animate.value = true
        }, delay)
    })

    val alpha1 = animateFloatAsState(
        targetValue = if (!animate.value) 0f else 1f,
        animationSpec = tween(durationMillis = 1000)
    )

    alpha(alpha = alpha1.value)
}

fun Modifier.determineTemplateRoute(text: String, route: MutableState<String>): Modifier {
    when (text) {
        App.getTextFromString(R.string.write_an_email) -> {
            route.value = Screens.ScreenEmail.route
        }

        App.getTextFromString(R.string.write_a_blog) -> {
            route.value = Screens.ScreenBlog.route
        }

        App.getTextFromString(R.string.write_an_essay) -> {
            route.value = Screens.ScreenEssay.route
        }

        App.getTextFromString(R.string.write_an_article) -> {
            route.value = Screens.ScreenArticle.route
        }

        App.getTextFromString(R.string.write_a_letter) -> {
            route.value = Screens.ScreenLetter.route
        }

        App.getTextFromString(R.string.write_a_cv) -> {
            route.value = Screens.ScreenCV.route
        }

        App.getTextFromString(R.string.write_a_resume) -> {
            route.value = Screens.ScreenResume.route
        }

        App.getTextFromString(R.string.write_a_personal_bio) -> {
            route.value = Screens.ScreenPersonalBio.route
        }

        App.getTextFromString(R.string.write_a_tweet) -> {
            route.value = Screens.ScreenTwitter.route
        }

        App.getTextFromString(R.string.write_a_viral_tiktok_captions) -> {
            route.value = Screens.ScreenTiktok.route
        }

        App.getTextFromString(R.string.write_an_instagram_caption) -> {
            route.value = Screens.ScreenInstagram.route
        }

        App.getTextFromString(R.string.write_a_facebook_post) -> {
            route.value = Screens.ScreenFacebook.route
        }

        App.getTextFromString(R.string.write_a_linkedin_post) -> {
            route.value = Screens.ScreenLinkedIn.route
        }

        App.getTextFromString(R.string.write_a_youtube_caption) -> {
            route.value = Screens.ScreenYoutube.route
        }

        App.getTextFromString(R.string.write_a_podcast_top_bar) -> {
            route.value = Screens.ScreenPodcast.route
        }

        App.getTextFromString(R.string.write_a_game_script_top_label) -> {
            route.value = Screens.ScreenGame.route
        }

        App.getTextFromString(R.string.write_a_poem) -> {
            route.value = Screens.ScreenPoem.route
        }

        App.getTextFromString(R.string.write_a_song) -> {
            route.value = Screens.ScreenSong.route
        }

        App.getTextFromString(R.string.write_a_code) -> {
            route.value = Screens.ScreenCode.route
        }

        App.getTextFromString(R.string.custom) -> {
            route.value = Screens.ScreenCustom.route
        }

        App.getTextFromString(R.string.summarize_the_following_text) -> {
            route.value = Screens.ScreenSummarize.route
        }

        App.getTextFromString(R.string.correct_the_following_text) -> {
            route.value = Screens.ScreenGrammar.route
        }

        App.getTextFromString(R.string.translate_the_following_text) -> {
            route.value = Screens.ScreenTranslate.route
        }

        else -> {
            SettingsNotifier.currentUserQuerySection = text
            route.value = Screens.ScreenUserAddedTemplate.route
        }
    }

    return this
}

