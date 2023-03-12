package com.appsfourlife.draftogo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.R


@Composable
fun DialogConfirmation(
    showDialog: MutableState<Boolean>,
    title: String,
    onCheckClick: () -> Unit
) {
    Dialog(onDismissRequest = {
        showDialog.value = false
    }) {
        Column(
            modifier = Modifier
                .background(color = Color.White, shape = Shapes.medium)
                .padding(SpacersSize.medium)
        ) {

            MyText(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            MySpacer(type = "medium")

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                IconButton(onClick = {
                    showDialog.value = false
                }) {
                    MyIcon(iconID = R.drawable.icon_wrong, contentDesc = stringResource(
                        id = R.string.wrong
                    ), tint = Color.Red)
                }

                IconButton(onClick = {
                    onCheckClick()
                    showDialog.value = false
                }) {
                    MyIcon(iconID = R.drawable.icon_check, contentDesc = stringResource(
                        id = R.string.check
                    ))
                }
            }
        }
    }

}