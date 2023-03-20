package com.appsfourlife.draftogo.components

import androidx.compose.foundation.clickable
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.appsfourlife.draftogo.helpers.WindowInfo
import com.appsfourlife.draftogo.helpers.rememberWindowInfo
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Gray

@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    value: String,
    fontWeight: FontWeight = FontWeight.Normal,
    placeholder: String = "",
    trailingIcon: Int = 0,
    trailingIconTint: Color = Color.White,
    cursorColor: Color = Blue,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        cursorColor = cursorColor,
    ),
    onTrailingIconClick: () -> Unit = {},
    textColor: Color = Color.Black,
    onValueChanged: (String) -> Unit
) {

    val fontSize = when (rememberWindowInfo().screenWidthInfo) {
        is WindowInfo.WindowType.Compact -> 15.sp
        is WindowInfo.WindowType.Medium -> 20.sp
        else -> 24.sp
    }

    TextField(
        modifier = modifier,
        value = value,
        onValueChange = {
            onValueChanged(it)
        },
        trailingIcon = {
            if (trailingIcon != 0)
                MyIcon(modifier = Modifier.clickable {
                    onTrailingIconClick()
                }, iconID = trailingIcon, contentDesc = "trailing icon", tint = trailingIconTint)
        },
        colors = colors,
        textStyle = TextStyle(fontWeight = fontWeight, fontSize = fontSize, color = textColor),
        placeholder = { MyText(text = placeholder, color = Gray) }
    )

}