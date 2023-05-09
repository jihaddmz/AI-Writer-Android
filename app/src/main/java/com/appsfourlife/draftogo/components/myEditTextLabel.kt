package com.appsfourlife.draftogo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.util.SettingsNotifier

@Composable
fun myEditTextLabel(
    modifier: Modifier = Modifier,
    placeHolder: String = stringResource(id = R.string.Name),
    label: String = stringResource(id = R.string.to),
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        MyText(text = "$label:", fontWeight = FontWeight.Bold)

        if (label == stringResource(id = R.string.to))
            MyOutlinedTextField(placeHolder = placeHolder, value = SettingsNotifier.name)
        else if (label == stringResource(id = R.string.job_title))
            MyOutlinedTextField(placeHolder = placeHolder, value = SettingsNotifier.jobTitle)
    }

}