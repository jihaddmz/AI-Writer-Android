
package com.jihad.aiwriter.components

import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jihad.aiwriter.helpers.WindowInfo
import com.jihad.aiwriter.helpers.rememberWindowInfo
import com.jihad.aiwriter.ui.theme.Blue
import com.jihad.aiwriter.ui.theme.Gray

@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    value: String,
    fontWeight: FontWeight = FontWeight.Normal,
    placeholder: String = "",
    onValueChanged: (String) -> Unit
) {

    val fontSize = when (rememberWindowInfo().screenWidthInfo){
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
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Blue,
        ),
        textStyle = TextStyle(fontWeight = fontWeight, fontSize = fontSize),
        placeholder = { MyText(text = placeholder, color = Gray) }
    )

}