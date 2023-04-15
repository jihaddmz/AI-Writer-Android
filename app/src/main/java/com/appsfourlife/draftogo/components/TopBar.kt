package com.appsfourlife.draftogo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.helpers.HelperAuth
import com.appsfourlife.draftogo.helpers.WindowInfo
import com.appsfourlife.draftogo.helpers.rememberWindowInfo
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.SettingsNotifier

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    text: String,
    navController: NavController,
    hideNbOfGenerationsLeft: Boolean = false,
    content: @Composable () -> Unit,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Blue)
                .padding(SpacersSize.small),
            verticalAlignment = Alignment.CenterVertically
        ) {

            when (rememberWindowInfo().screenWidthInfo) {
                is WindowInfo.WindowType.Compact -> Spacer(modifier = Modifier.width(SpacersSize.small))
                is WindowInfo.WindowType.Medium -> Spacer(modifier = Modifier.width(SpacersSize.small))
                else -> Spacer(modifier = Modifier.width(SpacersSize.medium))
            }

            MyTextTitle(text = text, color = Color.White)

            if (!HelperAuth.isSubscribed() && !hideNbOfGenerationsLeft) { // if user is not subscribed
                val nbOfGenerationsLeft = 2 - SettingsNotifier.nbOfGenerationsConsumed.value;
                if (nbOfGenerationsLeft <= 0) {
                    MyText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = SpacersSize.small),
                        color = Color.White,
                        text = stringResource(
                            id = R.string.left,
                            0
                        ),
                        textAlign = TextAlign.End
                    )
                } else
                    MyText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = SpacersSize.small),
                        color = Color.White,
                        text = stringResource(
                            id = R.string.left,
                            2 - SettingsNotifier.nbOfGenerationsConsumed.value
                        ),
                        textAlign = TextAlign.End
                    )
            }
        }

        Spacer(modifier = Modifier.height(SpacersSize.small))

        content()
    }
}