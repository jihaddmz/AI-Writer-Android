package com.appsfourlife.draftogo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import com.appsfourlife.draftogo.MainActivity
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.SettingsNotifier
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.helpers.HelperAuth
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.launch

@Composable
fun DialogSignIn(
    modifier: Modifier = Modifier,
) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current as MainActivity


    Dialog(onDismissRequest = {
        coroutineScope.launch {
            SettingsNotifier.showDialogNbOfGenerationsLeftExceeded.value = false
        }
    }) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = Shapes.medium)
                .padding(SpacersSize.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            MyText(text = stringResource(id = R.string.sign_in_title), fontWeight = FontWeight.Bold)

            MySpacer(type = "small")

            MyButton(text = stringResource(id = R.string.learn_more)) {
                // todo navigate to website to learn more about new features
            }

            MySpacer(type = "large")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                MyText(modifier = Modifier.clickable {
                    SettingsNotifier.showDialogSignIn.value = false
                    HelperSharedPreference.setBool(HelperSharedPreference.SP_SETTINGS,
                    HelperSharedPreference.SP_SETTINGS_OUTPUT_SHOW_DIALOG_SIGNIN, false)

                }, text = stringResource(id = R.string.no), color = Blue)

                MyText(modifier = Modifier.clickable {
                    HelperAuth.signIn(context)
                }, text = stringResource(id = R.string.yes), color = Blue)
            }

//            MyButton(text = stringResource(id = R.string.sign_in)) {
//
//
//            }
        }
    }
}