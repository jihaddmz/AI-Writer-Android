package com.appsfourlife.draftogo.extensions

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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