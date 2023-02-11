package com.appsfourlife.draftogo.components

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
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.ui.theme.Blue
import kotlin.math.roundToInt

@Composable
fun length(
    modifier: Modifier = Modifier,
    label: String = stringResource(id = R.string.length),
    maxLength: Float = Constants.MAX_GENERATION_LENGTH
): Int {

    val context = LocalContext.current
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

        MyText(text = "$label:", color = Blue, fontWeight = FontWeight.Bold)

        value.value = mySlider(
            modifier.fillMaxWidth(0.6f),
            initialValue = HelperSharedPreference.getFloat(
                HelperSharedPreference.SP_SETTINGS,
                HelperSharedPreference.SP_SETTINGS_LENGTH,
                100f,
                context = context
            ),
            valueRange = 7f..maxLength,
            onValueChangeListener = {
                HelperSharedPreference.setFloat(
                    HelperSharedPreference.SP_SETTINGS,
                    HelperSharedPreference.SP_SETTINGS_LENGTH,
                    it,
                    context = context
                )
            }).roundToInt()

        MyText(text = value.value.toString())
    }

    return value.value
}