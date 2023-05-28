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
import kotlin.math.roundToInt

@Composable
fun TypeWriterLength(
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val value = remember {
        mutableStateOf(7)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(durationMillis = 1000)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        MyText(text = "${stringResource(id = R.string.typewriter_length)}:(ms)", fontWeight = FontWeight.Bold)

        value.value = mySlider(
            modifier.fillMaxWidth(0.6f),
            initialValue = HelperSharedPreference.getFloat(
                HelperSharedPreference.SP_SETTINGS,
                HelperSharedPreference.SP_SETTINGS_OUTPUT_TYPEWRITER_LENGTH,
                50f,
                context = context
            ),
            valueRange = 0f..Constants.MAX_TYPEWRITER_LENGTH,
            steps = 10,
            onValueChangeListener = {
                HelperSharedPreference.setFloat(
                    HelperSharedPreference.SP_SETTINGS,
                    HelperSharedPreference.SP_SETTINGS_OUTPUT_TYPEWRITER_LENGTH,
                    it,
                    context = context
                )
            }).roundToInt()

        MyText(text = value.value.toString())
    }
}