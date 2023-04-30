package com.appsfourlife.draftogo.helpers

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.DialogProperties
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import java.util.*
import kotlin.concurrent.timerTask

object HelperUI {

    @OptIn(ExperimentalComposeUiApi::class)
    fun requestFocus(
        focusRequester: FocusRequester,
        keyboardController: SoftwareKeyboardController
    ) {
        focusRequester.requestFocus()
        keyboardController.show()
    }

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
                    Column {
                        MyTextTitle(
                            text = stringResource(id = R.string.permission_write_anywhere),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        MySpacer(type = "small")
                        MyAnnotatedText(
                            text = AnnotatedString(text = stringResource(id = R.string.permission_accessibility_label_desc)).plus(
                                AnnotatedString(text = " ")
                            ).plus(
                                AnnotatedString(
                                    "/draft",
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