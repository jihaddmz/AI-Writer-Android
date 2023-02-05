package com.jihad.aiwriter.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.jihad.aiwriter.R
import com.jihad.aiwriter.ui.theme.Blue
import java.util.*

@Composable
fun myEditTextLabel(
    modifier: Modifier = Modifier,
    placeHolder: String = stringResource(id = R.string.Name),
    label: String = stringResource(id = R.string.to),
): String {

    var result = ""

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        MyText(text = "$label:", color = Blue, fontWeight = FontWeight.Bold)

      result = myOutlinedTextField(placeHolder = placeHolder)
    }

    return result.capitalize(Locale.getDefault())
}