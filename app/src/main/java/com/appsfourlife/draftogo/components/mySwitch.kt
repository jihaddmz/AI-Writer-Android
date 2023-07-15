package com.appsfourlife.draftogo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.helpers.WindowInfo
import com.appsfourlife.draftogo.helpers.rememberWindowInfo
import com.appsfourlife.draftogo.ui.theme.Blue

@Composable
fun mySwitch(
    modifier: Modifier = Modifier,
    label: String = stringResource(id = R.string.enable_headlines),
    initialValue: Boolean = HelperSharedPreference.getBool(HelperSharedPreference.SP_SETTINGS, HelperSharedPreference.SP_SETTINGS_ENABLE_HEADLINES, false),
    onCheckedChange: (Boolean) -> Unit
): Boolean {

    val context = LocalContext.current
    val checked = remember {
        mutableStateOf(initialValue)
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(text = "$label:", fontWeight = FontWeight.Bold, fontSize = 16.sp)

        val switchSize = when (rememberWindowInfo().screenWidthInfo){
            is WindowInfo.WindowType.Compact -> 25.dp
            is WindowInfo.WindowType.Medium -> 50.dp
            else -> 70.dp
        }

        Switch(
            modifier = Modifier.requiredSize(switchSize),
            checked = checked.value,
            onCheckedChange = {
                checked.value = it
                onCheckedChange(it)
            },
            colors = SwitchDefaults.colors(checkedThumbColor = Blue)
        )
    }

    return checked.value
}