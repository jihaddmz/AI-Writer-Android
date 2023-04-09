package com.appsfourlife.draftogo.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
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
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun ScreenFeedback(
    navController: NavController
) {
    val feedback = remember {
        mutableStateOf("")
    }

    TopBar(
        text = stringResource(id = R.string.Feedback),
        navController = navController,
        hideNbOfGenerationsLeft = true
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpacersSize.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            MyText(text = stringResource(id = R.string.feedback_title))

           val type = myDropDown(label = stringResource(id = R.string.type), list = Constants.FEEDBACK_TYPES)

            Card(shape = Shapes.medium, border = BorderStroke(3.dp, color = Blue)) {
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
                text = stringResource(id = R.string.submit)
            ) {
                HelperIntent.sendEmail("developer@appsfourlife.com", subject = type, message = feedback.value)
            }
        }
    }
}