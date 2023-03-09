package com.appsfourlife.draftogo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.helpers.WindowInfo
import com.appsfourlife.draftogo.helpers.rememberWindowInfo
import com.appsfourlife.draftogo.ui.theme.Blue

@Composable
fun headlines(
    modifier: Modifier = Modifier
): Boolean {

    val context = LocalContext.current
    val checked = remember {
        mutableStateOf(HelperSharedPreference.getBool(HelperSharedPreference.SP_SETTINGS, HelperSharedPreference.SP_SETTINGS_ENABLE_HEADLINES, false, context = context) as Boolean)
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        MyText(text = stringResource(id = R.string.enable_headlines) + ":", color = Blue, fontWeight = FontWeight.Bold)

        val switchSize = when (rememberWindowInfo().screenWidthInfo){
            is WindowInfo.WindowType.Compact -> 25.dp
            is WindowInfo.WindowType.Medium -> 50.dp
            else -> 70.dp
        }

        Switch(
            modifier = Modifier.requiredSize(switchSize),
            checked = checked.value,
            onCheckedChange = {
                HelperSharedPreference.setBool(HelperSharedPreference.SP_SETTINGS, HelperSharedPreference.SP_SETTINGS_ENABLE_HEADLINES, it, context = context)
                checked.value = it
            },
            colors = SwitchDefaults.colors(checkedThumbColor = Blue)
        )
    }

    return checked.value
}