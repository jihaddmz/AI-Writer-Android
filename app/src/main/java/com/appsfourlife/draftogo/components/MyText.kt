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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.helpers.WindowInfo
import com.appsfourlife.draftogo.helpers.rememberWindowInfo

@Composable
fun MyText(
    modifier: Modifier = Modifier,
    text: String,
    textAlign: TextAlign = TextAlign.Start,
    textStyle: TextStyle = TextStyle(),
    color: Color = Color.Black,
    textDecoration: TextDecoration = TextDecoration.None,
    fontWeight: FontWeight = FontWeight.Normal,
    enableSingleLine: Boolean = false
) {

    val fontSize = when (rememberWindowInfo().screenWidthInfo) {
        is WindowInfo.WindowType.Compact -> 14.sp
        is WindowInfo.WindowType.Medium -> 20.sp
        else -> 24.sp
    }

    if (!enableSingleLine)
        Text(
            modifier = modifier.animateContentSize(animationSpec = tween(durationMillis = Constants.ANIMATION_LENGTH)),
            color = color,
            textAlign = textAlign,
            fontSize = fontSize,
            text = text,
            fontWeight = fontWeight,
            style = textStyle,
            textDecoration = textDecoration,
            overflow = TextOverflow.Ellipsis
        )
     else {
        Text(
            modifier = modifier.animateContentSize(animationSpec = tween(durationMillis = Constants.ANIMATION_LENGTH)),
            color = color,
            textAlign = textAlign,
            fontSize = fontSize,
            text = text,
            fontWeight = fontWeight,
            style = textStyle,
            textDecoration = textDecoration,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

}