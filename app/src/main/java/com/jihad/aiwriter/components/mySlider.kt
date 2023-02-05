package com.jihad.aiwriter.components

import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.jihad.aiwriter.ui.theme.Blue

@Composable
fun mySlider(
    modifier: Modifier = Modifier,
    value: MutableState<Float>,
    valueRange: ClosedFloatingPointRange<Float> = 7f..500f,
    onValueChangeListener: (Float) -> Unit
): Float {


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