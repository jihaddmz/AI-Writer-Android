package com.appsfourlife.draftogo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun MyCustomConfirmationDialog(
    modifier: Modifier = Modifier,
    showDialog: MutableState<Boolean>,
    properties: DialogProperties = DialogProperties(),
    negativeBtnText: String,
    positiveBtnText: String,
    onNegativeBtnClick: () -> Unit = {},
    onPositiveBtnClick: () -> Unit,
    content: @Composable () -> Unit
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

            content()

            MySpacer(type = "medium")

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                MyOutlinedButton(text = negativeBtnText, borderColor = Color.Red) {
                    showDialog.value = false
                    onNegativeBtnClick()
                }
                MyOutlinedButton(text = positiveBtnText, borderColor = Blue) {
                    showDialog.value = false
                    onPositiveBtnClick()
                }
            }
        }
    }
}