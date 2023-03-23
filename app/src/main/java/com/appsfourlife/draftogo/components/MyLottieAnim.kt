package com.appsfourlife.draftogo.components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.*

@Composable
fun MyLottieAnim(
    lottieID: Int,
    isLottieAnimationPlaying: MutableState<Boolean>,
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
        isPlaying = isLottieAnimationPlaying.value,
        // Increasing the speed of change Lottie
        speed = animationSpeed,
        restartOnPlay = false,
    )

    LottieAnimation(composition = composition, lottieAnimation, modifier = modifier)


}