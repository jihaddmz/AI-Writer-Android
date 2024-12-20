package com.appsfourlife.draftogo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun MyCustomConfirmationDialog(
    modifier: Modifier = Modifier,
    showDialog: MutableState<Boolean>,
    properties: DialogProperties = DialogProperties(),
    negativeBtnText: String,
    positiveBtnText: String,
    closeOnNegativeBtnClick: Boolean = true,
    showCloseBtn: Boolean = false,
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
                .padding(SpacersSize.small),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (showCloseBtn) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    IconButton(onClick = {
                        showDialog.value = false
                    }) {
                        MyIcon(iconID = R.drawable.clear, contentDesc = "close")
                    }
                }

                MySpacer(type = "small")
            }

            content()

            MySpacer(type = "medium")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                MyOutlinedButton(text = negativeBtnText) {
                    if (closeOnNegativeBtnClick)
                        showDialog.value = false

                    onNegativeBtnClick()
                }
                MyOutlinedButton(text = positiveBtnText) {
                    showDialog.value = false
                    onPositiveBtnClick()
                }
            }
        }
    }
}