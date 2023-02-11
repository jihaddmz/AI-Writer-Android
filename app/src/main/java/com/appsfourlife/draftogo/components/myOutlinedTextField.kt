package com.appsfourlife.draftogo.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appsfourlife.draftogo.helpers.WindowInfo
import com.appsfourlife.draftogo.helpers.rememberWindowInfo
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Gray
import com.appsfourlife.draftogo.ui.theme.Shapes

@Composable
fun myOutlinedTextField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    placeHolder: String,
    borderStroke: BorderStroke = BorderStroke(2.dp, color = Blue)
): String {

    val value = remember {
        mutableStateOf("")
    }

    val fontSize = when (rememberWindowInfo().screenWidthInfo){
        is WindowInfo.WindowType.Compact -> 15.sp
        is WindowInfo.WindowType.Medium -> 20.sp
        else -> 24.sp
    }

    Card(modifier = modifier, shape = Shapes.medium, border = borderStroke) {

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
    return value.value
}