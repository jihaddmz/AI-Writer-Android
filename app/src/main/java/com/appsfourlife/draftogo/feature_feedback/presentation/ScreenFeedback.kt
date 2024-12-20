package com.appsfourlife.draftogo.feature_feedback.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.helpers.HelperIntent
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.BottomNavScreens
import com.appsfourlife.draftogo.util.SettingsNotifier

@Composable
fun ScreenFeedback(
    navController: NavController
) {
    val feedback = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {

        AppBarTransparent(title = stringResource(id = R.string.Feedback)) {
            SettingsNotifier.navHostController?.navigate(BottomNavScreens.Settings.route)
        }

        MySpacer(type = "large")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = SpacersSize.medium,
                    end = SpacersSize.medium,
                    bottom = SpacersSize.medium
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            MyTipText(text = stringResource(id = R.string.feedback_title))

            val type =
                myDropDown(
                    label = stringResource(id = R.string.type),
                    list = Constants.FEEDBACK_TYPES
                )

            MyCardView {
                MyTextField(
                    value = feedback.value,
                    onValueChanged = {
                        feedback.value = it
                    },
                    placeholder = stringResource(id = R.string.your_feedback),
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 300.dp)
                )
            }

            MyButton(
                modifier = Modifier.fillMaxWidth(),
                isEnabled = feedback.value.isNotEmpty(),
                text = stringResource(id = R.string.submit)
            ) {
                HelperIntent.sendEmail(
                    "developer@appsfourlife.com",
                    subject = type,
                    message = feedback.value
                )
            }
        }
    }
}