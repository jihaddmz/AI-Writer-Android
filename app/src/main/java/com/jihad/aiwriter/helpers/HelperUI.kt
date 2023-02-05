package com.jihad.aiwriter.helpers

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.SoftwareKeyboardController

object HelperUI {

    @OptIn(ExperimentalComposeUiApi::class)
    fun requestFocus(focusRequester: FocusRequester, keyboardController: SoftwareKeyboardController){
        focusRequester.requestFocus()
        keyboardController.show()
    }

    fun showToast(context: Context, msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}