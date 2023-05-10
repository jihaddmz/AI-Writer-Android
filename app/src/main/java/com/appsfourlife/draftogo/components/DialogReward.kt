package com.appsfourlife.draftogo.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.appsfourlife.draftogo.R

@Composable
fun DialogReward(showDialog: MutableState<Boolean>, text: String) {

    MyCustomDialog(showDialog = showDialog, dialogProperties = DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = true)) {

        MyTextTitle(text = text, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        
        MySpacer(type = "small")

        MyLottieAnim(lottieID = R.raw.lottie_congratulate, modifier = Modifier.fillMaxWidth().height(200.dp))

    }
}