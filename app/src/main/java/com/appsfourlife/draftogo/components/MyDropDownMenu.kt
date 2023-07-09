package com.appsfourlife.draftogo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun MyDropDownMenu(expand: MutableState<Boolean>, list: List<String>, onItemClickListener: (String) -> Unit) {

    DropdownMenu(modifier = Modifier
        .fillMaxWidth(0.9f)
        .padding(start = SpacersSize.medium),
        expanded = expand.value,
        onDismissRequest = {
            expand.value = false
        }) {

        list.forEach {
            MyText(text = it, modifier = Modifier.clickable {
                onItemClickListener(it)
            })

            MySpacer(type = "small")
        }

    }
}