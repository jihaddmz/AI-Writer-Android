package com.appsfourlife.draftogo.components

import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.appsfourlife.draftogo.helpers.WindowInfo
import com.appsfourlife.draftogo.helpers.rememberWindowInfo
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Gray

@Composable
fun MyOutlinedTextField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    placeHolder: String,
    value: MutableState<String>,
) {

    val fontSize = when (rememberWindowInfo().screenWidthInfo){
        is WindowInfo.WindowType.Compact -> 15.sp
        is WindowInfo.WindowType.Medium -> 20.sp
        else -> 24.sp
    }

    MyCardView(modifier = modifier) {

        OutlinedTextField(
            modifier = Modifier,
            placeholder = { MyText(text = placeHolder, color = Gray) },
            value = value.value,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Blue,
            ),
            onValueChange = {
                value.value = it
                onValueChange(it)
            },
            textStyle = TextStyle(fontSize = fontSize)
        )
    }
}