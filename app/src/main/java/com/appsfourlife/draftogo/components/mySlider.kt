package com.appsfourlife.draftogo.components

import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.ui.theme.Blue

@Composable
fun mySlider(
    modifier: Modifier = Modifier,
    initialValue: Float,
    valueRange: ClosedFloatingPointRange<Float> = 7f..Constants.MAX_GENERATION_LENGTH,
    onValueChangeListener: (Float) -> Unit
): Float {

    val value = remember {
        mutableStateOf(initialValue)
    }

    Slider(
        modifier = modifier,
        value = value.value,
        onValueChange = {
            value.value = it
            onValueChangeListener(it)
        },
        colors = SliderDefaults.colors(thumbColor = Blue, activeTrackColor = Blue),
        valueRange = valueRange
    )

    return value.value
}