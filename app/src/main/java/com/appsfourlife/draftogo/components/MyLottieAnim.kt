package com.appsfourlife.draftogo.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.*

@Composable
fun MyLottieAnim(
    lottieID: Int,
    modifier: Modifier = Modifier
) {

    val animationSpeed by remember {
        mutableStateOf(1f)
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(
                lottieID
            )
    )
    val progress by animateLottieCompositionAsState(composition)
    if (progress == 1.0f){ // if the lottie animation is done animating, set the playing state of the anim to false
//        isLottieAnimationPlaying.value = false
    }

    // to control the lottie animation
    val lottieAnimation by animateLottieCompositionAsState(
        // pass the composition created above
        composition,
        // Iterates Forever
        iterations = LottieConstants.IterateForever,
        // Lottie and pause/play
        isPlaying = true,
        // Increasing the speed of change Lottie
        speed = animationSpeed,
        restartOnPlay = false,
    )

    LottieAnimation(composition = composition, lottieAnimation, modifier = modifier)


}