package com.appsfourlife.draftogo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun MyDialog(
    modifier: Modifier = Modifier,
    showDialog: MutableState<Boolean>,
    text: String,
    title: String,
    showOkBtn: Boolean = false,
    onOkBtnClick: () -> Unit = {},
    properties: DialogProperties = DialogProperties()
) {

    Dialog(onDismissRequest = {
        showDialog.value = false
    }, properties = properties) {
        Column(
            modifier = modifier
                .background(color = Color.White, shape = Shapes.medium)
                .padding(SpacersSize.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)

            MySpacer(type = "medium")

            MyText(text = text)

            MySpacer(type = "medium")

            if (showOkBtn){
                MyButton(modifier = Modifier.fillMaxWidth().padding(horizontal = SpacersSize.medium), text = stringResource(id = R.string.ok)) {
                    showDialog.value = false
                    onOkBtnClick()
                }
            }
        }
    }
}