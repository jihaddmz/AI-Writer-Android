package com.appsfourlife.draftogo.components

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.ui.theme.SpacersSize


@SuppressLint("UnrememberedMutableState")
@Composable
fun BottomSheetRedeemCode() {
    val context = LocalContext.current
    val code = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SpacersSize.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MyTextTitle(text = stringResource(id = R.string.redeem_code), fontWeight = FontWeight.Bold)

        MySpacer(type = "small")

        MyText(text = stringResource(id = R.string.redeem_code_desc))

        MySpacer(type = "medium")

        MyOutlinedTextField(placeHolder = "code", value = code)

        MySpacer(type = "medium")

        MyOutlinedButton(isEnabled = mutableStateOf(code.value.isNotEmpty()), text = stringResource(id = R.string.submit)) {
            val url = "https://play.google.com/redeem?code=${code.value}"
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }
}