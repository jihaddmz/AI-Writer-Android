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
    label: String = "",
    steps: Int = 0,
    onValueChangeListener: (Float) -> Unit
): Float {

    val value = remember {
        mutableStateOf(initialValue)
    }

    Slider(
        modifier = modifier,
        valueRange = valueRange,
        value = value.value,
        onValueChange = {
            value.value = it
            onValueChangeListener(it)
        },
        steps = steps,
        colors = SliderDefaults.colors(thumbColor = Blue, activeTrackColor = Blue),
    )

    return value.value
}