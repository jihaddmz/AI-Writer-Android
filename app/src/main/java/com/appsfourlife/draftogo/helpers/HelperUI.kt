package com.appsfourlife.draftogo.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.DialogProperties
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import java.util.*

object HelperUI {

    @OptIn(ExperimentalComposeUiApi::class)
    fun requestFocus(
        focusRequester: FocusRequester,
        keyboardController: SoftwareKeyboardController
    ) {
        focusRequester.requestFocus()
        keyboardController.show()
    }

    @SuppressLint("WrongConstant")
    fun showToast(context: Context = App.context, msg: String) {
        Toast.makeText(context, msg, Constants.Toast_Lenght).show()
    }

    @Composable
    fun ShowAccessibilityPermissionRequester(showDontShowAgainOption: Boolean) {
        val context = LocalContext.current

        if (!HelperPermissions.isAccessibilityEnabled(context)) {
            val showDialog = remember {
                mutableStateOf(true)
            }
            if (showDialog.value)
                MyCustomConfirmationDialog(
                    showDialog = showDialog,
                    negativeBtnText = stringResource(id = R.string.cancel),
                    positiveBtnText = stringResource(id = R.string.ok),
                    properties = DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false
                    ),
                    onPositiveBtnClick = {
                        // if not construct intent to request permission
                        val intent =
                            Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        // request permission via start activity for result
                        context.startActivity(intent)
                    }) {
                    Column(modifier = Modifier.padding(SpacersSize.small)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            MyTextTitle(
                                text = stringResource(id = R.string.permission_write_anywhere),
                                modifier = Modifier,
                                fontWeight = FontWeight.Bold
                            )
                            MySpacer(type = "small", widthOrHeight = "width")
                            IconLink(url = "https://appsfourlife.com/blogpost_writeanywhere")
                        }
                        MySpacer(type = "small")
                        MyAnnotatedText(
                            text = AnnotatedString(text = stringResource(id = R.string.permission_accessibility_label_desc)).plus(
                                AnnotatedString(text = " ")
                            ).plus(
                                AnnotatedString(
                                    stringResource(id = R.string.draft),
                                    spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
                                ),
                            )
                        )
                        if (showDontShowAgainOption) {
                            MySpacer(type = "small")
                            val checked = remember {
                                mutableStateOf(HelperSharedPreference.getDontShowAnyWhereWritingPermission())
                            }
                            MyCheckbox(
                                label = stringResource(id = R.string.dont_show_again),
                                checked = checked,
                                onCheckedChange = {
                                    HelperSharedPreference.setDontShowAnyWhereWritingPermission(it)
                                }
                            )
                        }
                    }
                }
        }
    }
}