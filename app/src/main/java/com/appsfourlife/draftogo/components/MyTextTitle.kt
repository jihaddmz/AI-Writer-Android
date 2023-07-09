package com.appsfourlife.draftogo.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.helpers.WindowInfo
import com.appsfourlife.draftogo.helpers.rememberWindowInfo

@Composable
fun MyTextTitle(
    modifier: Modifier = Modifier,
    text: String,
    textAlign: TextAlign = TextAlign.Start,
    textStyle: TextStyle = TextStyle(),
    color: Color = Color.Black,
    fontWeight: FontWeight = FontWeight.Normal,
    baseFontSize: Int = 17
) {

    val fontSize = when (rememberWindowInfo().screenWidthInfo){
        is WindowInfo.WindowType.Compact -> baseFontSize.sp
        is WindowInfo.WindowType.Medium -> (baseFontSize + 13).sp
        else -> (baseFontSize + 18).sp
    }

    Text(
        modifier = modifier.animateContentSize(animationSpec = tween(durationMillis = Constants.ANIMATION_LENGTH)),
        color = color,
        textAlign = textAlign,
        fontSize = fontSize,
        text = text,
        fontWeight = fontWeight,
        style = textStyle
    )

}