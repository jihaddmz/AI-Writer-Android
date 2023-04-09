package com.appsfourlife.draftogo

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.appsfourlife.draftogo.components.MyButton
import com.appsfourlife.draftogo.components.MyTextTitle
import com.appsfourlife.draftogo.components.MyVideo
import com.appsfourlife.draftogo.helpers.HelperAuth
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.SettingsNotifier

@Composable
fun ScreenSignIn(
) {
    val currentActivity = LocalContext.current as Activity
    val coroutineScope = rememberCoroutineScope()

    SettingsNotifier.disableDrawerContent.value = true

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = R.drawable.login_bg),
            contentDescription = "login bg"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpacersSize.medium)
                .padding(top = SpacersSize.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            MyTextTitle(
                text = stringResource(id = R.string.login),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

//            MyVideo(modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(0.5f), videoUri = "https://user-images.githubusercontent.com/124468932/224495423-a39f624f-836f-4526-8e0a-2e51c814e960.mp4")

            MyVideo(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                videoUri = "https://firebasestorage.googleapis.com/v0/b/ai-writer-9832b.appspot.com/o/Draftogo%20promo%20video%20android_720p%20(1).mp4?alt=media&token=b7afc317-e712-48a1-980d-e8d7e858ab32"
            )

            MyButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.sign_in_with_google)
            ) {
                HelperAuth.signIn(currentActivity)
            }
        }
    }
}