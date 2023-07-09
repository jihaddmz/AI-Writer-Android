package com.appsfourlife.draftogo.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppBarTransparent(
    modifier: Modifier = Modifier,
    title: String,
    showSidebar: Boolean = false,
    showHelpIcon: Boolean = false,
    scaffoldState: ScaffoldState? = null,
    onBackBtnClick: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = SpacersSize.small,
                bottom = SpacersSize.medium,
                start = SpacersSize.small
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showSidebar) {
                IconButton(onClick = {
                    coroutineScope.launch {
                        scaffoldState?.drawerState?.animateTo(
                            DrawerValue.Open,
                            tween(durationMillis = 1000)
                        )
                    }
                }) {
                    MyIcon(
                        iconID = R.drawable.icon_sidebar,
                        contentDesc = "sidebar",
                        tint = Color.Black
                    )
                }
                MySpacer(type = "medium", widthOrHeight = "width")
            }
            Text(
                modifier = Modifier,
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (showHelpIcon) {
            Row {

                val expand = remember {
                    mutableStateOf(false)
                }
                val showDialog = remember {
                    mutableStateOf(false)
                }
                val questionedText = remember {
                    mutableStateOf("")
                }
                val mapOfHelpCommands = mapOf(
                    stringResource(id = R.string.how_to_regenerate_response) to stringResource(
                        id = R.string.just_tap_the_input_you_want
                    ),
                    stringResource(id = R.string.how_to_generate_output_in_specific_language) to stringResource(
                        id = R.string.append_the_language_you_want_at_the_end
                    ),
                    stringResource(id = R.string.how_to_copy_generated_text) to stringResource(
                        id = R.string.long_click_the_generated_output
                    ),
                )

                IconButton(onClick = {
                    expand.value = true
                }) {
                    MyIcon(iconID = R.drawable.icon_help, contentDesc = "help", tint = Color.Black)
                }

                MyDropDownMenu(
                    expand = expand,
                    list = mapOfHelpCommands.keys.toList(),
                    onItemClickListener = {
                        questionedText.value = it
                        expand.value = false
                        showDialog.value = true
                    })

                if (showDialog.value)
                    MyDialog(
                        showDialog = showDialog,
                        text = mapOfHelpCommands[questionedText.value]!!,
                        title = questionedText.value,
                        showOkBtn = true
                    )
            }
        }
    }
}