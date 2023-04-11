package com.appsfourlife.draftogo.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelTemplate
import com.appsfourlife.draftogo.feature_generate_text.models.ModelTemplateIcon
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.SettingsNotifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timerTask

@Composable
fun DialogAddTemplate(
    modifier: Modifier = Modifier,
    showDialog: MutableState<Boolean>,
    onTemplateAdded: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val verticalScroll = rememberScrollState()
    val showIconChooserDialog = remember {
        mutableStateOf(false)
    }
    val clickedImageUrl = remember {
        mutableStateOf("https://user-images.githubusercontent.com/124468932/230606491-b8ec8c39-406d-478d-937a-cd4c1714e93a.svg")
    }

    Dialog(onDismissRequest = {
        showDialog.value = false
    }) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = Shapes.medium)
                .padding(SpacersSize.medium)
                .verticalScroll(verticalScroll),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val input = myTextFieldLabel(
                label = stringResource(id = R.string.query),
                placeholder = stringResource(
                    id = R.string.add_template_example
                ),
                singleLine = true
            )

            MySpacer(type = "small")

            if (showIconChooserDialog.value) {
                DialogIconChooser(
                    showDialog = showIconChooserDialog,
                    clickedImageUrl = clickedImageUrl
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                MyText(text = "${stringResource(id = R.string.choose_image)}:")

                MySpacer(type = "medium", widthOrHeight = "width")
                MyUrlSvg(
                    modifier = Modifier.clickable {
                        if (!SettingsNotifier.isConnected.value) {
                            HelperUI.showToast(msg = App.getTextFromString(R.string.no_connection))
                            return@clickable
                        }

                        showIconChooserDialog.value = true
                    },
                    imageUrl = clickedImageUrl.value,
                    contentDesc = ""
                )
            }

            MySpacer(type = "small")

            MyButton(text = stringResource(id = R.string.add), modifier = Modifier.fillMaxWidth()) {

                HelperAnalytics.sendEvent("add_template")

                if (input.isEmpty()) {
                    HelperUI.showToast(msg = App.getTextFromString(R.string.no_query_defined))
                    return@MyButton
                }

                coroutineScope.launch(Dispatchers.IO) {
                    App.databaseApp.daoApp.insertTemplate(
                        ModelTemplate(
                            query = input.trim(),
                            imageUrl = clickedImageUrl.value.trim(),
                            0
                        )
                    )
                    showDialog.value = false

                    Timer().schedule(timerTask {
                        onTemplateAdded()
                    }, 1000)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialogIconChooser(
    showDialog: MutableState<Boolean>,
    clickedImageUrl: MutableState<String>,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val icons = remember {
        mutableStateOf(listOf<ModelTemplateIcon>())
    }

    LaunchedEffect(key1 = true, block = {
        coroutineScope.launch(Dispatchers.IO) {
            HelperFirebaseDatabase.getAllTemplateIcons(icons)
        }
    })

    Dialog(onDismissRequest = {
        showDialog.value = false
    }) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(color = Color.White, shape = Shapes.medium)
                .padding(SpacersSize.medium)
        ) {

            LazyVerticalGrid(
                cells = GridCells.Fixed(count = 4),
                content = {
                    items(icons.value.size) { index ->
                        val name = icons.value[index].name
                        val url = icons.value[index].url

                        ListItemTemplateIcon(
                            modifier = Modifier.padding(bottom = SpacersSize.medium),
                            name = name,
                            url = url,
                            onIconClicked = {
                                clickedImageUrl.value = it
                                showDialog.value = false
                            }
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun ListItemTemplateIcon(
    modifier: Modifier = Modifier,
    name: String,
    url: String,
    onIconClicked: (String) -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        MyUrlSvg(modifier = Modifier.clickable {
            onIconClicked(url)
        }, imageUrl = url, contentDesc = name)
        MyText(text = name)
    }
}