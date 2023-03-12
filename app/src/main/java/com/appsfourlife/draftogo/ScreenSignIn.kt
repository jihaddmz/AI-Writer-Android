package com.appsfourlife.draftogo

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.helpers.HelperAuth
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun ScreenSignIn(
) {
    val currentActivity = LocalContext.current as Activity

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
            MyTextTitle(text = stringResource(id = R.string.login), fontWeight = FontWeight.Bold, color = Color.White)

            MyVideo(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f), videoUri = "https://user-images.githubusercontent.com/124468932/224495423-a39f624f-836f-4526-8e0a-2e51c814e960.mp4")
            
            MyButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.sign_in_with_google)
            ) {
                HelperAuth.signIn(currentActivity)
            }
        }
    }
}