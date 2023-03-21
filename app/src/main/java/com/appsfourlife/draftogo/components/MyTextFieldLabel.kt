package com.appsfourlife.draftogo.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.appsfourlife.draftogo.ui.theme.Blue

@Composable
fun myTextFieldLabel(
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String,
    singleLine: Boolean = false,
    onValueChange: (String) -> Unit = {}
): String {

    val input = remember {
        mutableStateOf("")
    }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {

        MyText(text = "$label:")

        MySpacer(type = "medium", widthOrHeight = "width")

        MyTextField(
            value = input.value,
            onValueChanged = {
                input.value = it
                onValueChange(it)
            },
            singleLine = singleLine,
            placeholder = placeholder,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Blue,
                cursorColor = Blue,
            ),
        )
    }

    return input.value
}