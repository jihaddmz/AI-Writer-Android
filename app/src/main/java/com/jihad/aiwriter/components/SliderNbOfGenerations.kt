package com.jihad.aiwriter.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.jihad.aiwriter.R
import com.jihad.aiwriter.helpers.Constants
import com.jihad.aiwriter.helpers.HelperSharedPreference
import com.jihad.aiwriter.ui.theme.Blue
import kotlin.math.roundToInt

@Composable
fun sliderNbOfGenerations(
    modifier: Modifier = Modifier,
): Int {

    val context = LocalContext.current
    val initialValue = remember {
        mutableStateOf(HelperSharedPreference.getFloat(HelperSharedPreference.SP_SETTINGS, HelperSharedPreference.SP_SETTINGS_NB_OF_GENERATIONS, 1f, context = context))
    }

    val value = remember {
        mutableStateOf(7)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(durationMillis = 1000)),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        MyText(text = "${stringResource(id = R.string.nb_of_generations)}:", color = Blue, fontWeight = FontWeight.Bold)

        value.value = mySlider(modifier.fillMaxWidth(0.6f), value = initialValue, valueRange = 1f..Constants.MAX_NB_OF_GENERATIONS, onValueChangeListener = {
            HelperSharedPreference.setFloat(HelperSharedPreference.SP_SETTINGS, HelperSharedPreference.SP_SETTINGS_NB_OF_GENERATIONS, it, context = context)
        }).roundToInt()

        MyText(text = value.value.toString())

    }

    return value.value
}