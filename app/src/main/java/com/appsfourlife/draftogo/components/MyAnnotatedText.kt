package com.appsfourlife.draftogo.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.appsfourlife.draftogo.helpers.WindowInfo
import com.appsfourlife.draftogo.helpers.rememberWindowInfo

@Composable
fun MyAnnotatedText(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    textAlign: TextAlign = TextAlign.Start
) {

    val fontSize = when (rememberWindowInfo().screenWidthInfo){
        is WindowInfo.WindowType.Compact -> 14.sp
        is WindowInfo.WindowType.Medium -> 20.sp
        else -> 24.sp
    }

    Text(modifier = modifier, text = text, fontSize = fontSize, textAlign = textAlign)

}