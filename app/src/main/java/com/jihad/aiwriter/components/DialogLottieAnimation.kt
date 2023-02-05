package com.jihad.aiwriter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jihad.aiwriter.ui.theme.Shapes
import com.jihad.aiwriter.ui.theme.SpacersSize

@Composable
fun DialogLottieAnimation(
    modifier: Modifier = Modifier,
    text: String,
    lottieID: Int,
    onDismissDialog: () -> Unit
) {

    val isLottieAnimationPlaying = remember {
        mutableStateOf(true)
    }

    if (!isLottieAnimationPlaying.value) {
        onDismissDialog()
    }

    Dialog(onDismissRequest = {
        onDismissDialog()
    }) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = Shapes.medium)
                .padding(SpacersSize.medium)
        ) {

            MyText(
                text = text,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            MySpacer(type = "large")

            MyLottieAnim(
                lottieID = lottieID, modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp), isLottieAnimationPlaying = isLottieAnimationPlaying
            )
        }

    }

}