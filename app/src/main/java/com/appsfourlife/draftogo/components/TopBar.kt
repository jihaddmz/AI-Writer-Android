package com.appsfourlife.draftogo.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.helpers.HelperAuth
import com.appsfourlife.draftogo.helpers.WindowInfo
import com.appsfourlife.draftogo.helpers.rememberWindowInfo
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.BottomNavScreens
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
                .padding(top = SpacersSize.medium, start = SpacersSize.small, end = SpacersSize.small),
            verticalAlignment = Alignment.CenterVertically
        ) {

            when (rememberWindowInfo().screenWidthInfo) {
                is WindowInfo.WindowType.Compact -> Spacer(modifier = Modifier.width(SpacersSize.small))
                is WindowInfo.WindowType.Medium -> Spacer(modifier = Modifier.width(SpacersSize.small))
                else -> Spacer(modifier = Modifier.width(SpacersSize.medium))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    navController.navigate(BottomNavScreens.Content.route)
                }) {
                    MyIcon(
                        iconID = R.drawable.icon_arrow_back,
                        contentDesc = "back",
                        tint = Color.Black
                    )
                }
                MySpacer(type = "small", widthOrHeight = "width")
                Text(text = text, color = Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            }

            if (!HelperAuth.isSubscribed() && !hideNbOfGenerationsLeft) { // if user is not subscribed
                val nbOfGenerationsLeft = 2 - SettingsNotifier.nbOfGenerationsConsumed.value;
                if (nbOfGenerationsLeft <= 0) {
                    MyText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = SpacersSize.small),
                        color = Color.Black,
                        text = stringResource(
                            id = R.string.left,
                            0
                        ),
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.ExtraBold
                    )
                } else
                    MyText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = SpacersSize.small),
                        color = Color.Black,
                        text = stringResource(
                            id = R.string.left,
                            2 - SettingsNotifier.nbOfGenerationsConsumed.value
                        ),
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.ExtraBold
                    )
            }
        }

        MySpacer(type = "medium")

        content()
    }
}