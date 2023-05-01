package com.appsfourlife.draftogo.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Gray

@Composable
fun MyCheckbox(label: String, checked: MutableState<Boolean>, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        MyText(text = label, color = Blue)
        MySpacer(type = "medium", widthOrHeight = "width")
        Checkbox(checked = checked.value, onCheckedChange = {
            checked.value = it
            onCheckedChange(it)
        }, colors = CheckboxDefaults.colors(checkedColor = Blue, uncheckedColor = Gray))
    }
}