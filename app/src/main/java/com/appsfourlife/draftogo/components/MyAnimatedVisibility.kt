package com.appsfourlife.draftogo.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.appsfourlife.draftogo.helpers.Constants

@Composable
fun MyAnimatedVisibility(
    modifier: Modifier = Modifier,
    visible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(durationMillis = Constants.ANIMATION_LENGTH)
        ) + expandIn(animationSpec = tween(durationMillis = Constants.ANIMATION_LENGTH)),
        exit = fadeOut(animationSpec = tween(durationMillis = Constants.ANIMATION_LENGTH))
     + shrinkOut(animationSpec = tween(durationMillis = Constants.ANIMATION_LENGTH))
    ) {
        content()
    }
}