package com.appsfourlife.draftogo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.SettingsNotifier

@Composable
fun DialogLoading(
    modifier: Modifier = Modifier,
    title: String
) {

    Dialog(
        onDismissRequest = { SettingsNotifier.showLoadingDialog.value = false },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Column(modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = Shapes.medium)
            .padding(SpacersSize.medium), horizontalAlignment = Alignment.CenterHorizontally) {

            MyText(text = title, fontWeight = FontWeight.Bold)

            MySpacer(type = "medium")

            CircularProgressIndicator()
        }
    }

}