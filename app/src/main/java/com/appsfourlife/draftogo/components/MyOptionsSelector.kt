package com.appsfourlife.draftogo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun myOptionsSelector(
    modifier: Modifier = Modifier,
    list: List<String>,
    defaultSelectedText: String
): String {

    val selectedOption = remember {
        mutableStateOf("")
    }
    val listOfSelectedColors = remember {
        mutableListOf<MutableState<Color>>()
    }

    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        list.forEach {
            val color = remember {
                mutableStateOf(Color.Transparent)
            }
            LaunchedEffect(key1 = true, block = {
                if (it == defaultSelectedText) {
                    listOfSelectedColors.add(color)
                    color.value = Color.LightGray
                    selectedOption.value = it
                }
            })
            OptionItemSelector(text = it, modifier = Modifier.clickable {
                selectedOption.value = it
                listOfSelectedColors.add(color)
                listOfSelectedColors.forEach { color ->
                    color.value = Color.Transparent
                }
                color.value = Color.LightGray
            }, color.value)
        }
    }

    return selectedOption.value
}

@Composable
fun OptionItemSelector(
    text: String,
    modifier: Modifier,
    bgC: Color
) {
    MyCardView(
        modifier = modifier.background(bgC, shape = Shapes.medium),
    ) {
        MyText(modifier = Modifier.padding(SpacersSize.medium), text = text)
    }
}