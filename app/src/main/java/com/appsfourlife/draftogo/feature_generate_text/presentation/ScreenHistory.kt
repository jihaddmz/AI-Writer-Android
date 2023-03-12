package com.appsfourlife.draftogo.feature_generate_text.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.feature_generate_text.models.ModelHistory
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.Screens
import com.appsfourlife.draftogo.util.SettingsNotifier

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScreenHistory(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val state = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val showDialog = remember {
        mutableStateOf(false)
    }
    val modelHistory = remember {
        mutableStateOf(ModelHistory(type = "", input = "", output = "", date = ""))
    }
    val noHistory = remember {
        mutableStateOf(false)
    }
    val showCircularIndicator = remember {
        mutableStateOf(true)
    }

    val result = remember {
        mutableStateOf(mutableListOf<ModelHistory>())
    }

    HelperFirebaseDatabase.fetchHistory(result, noHistory, showCircularIndicator)

    TopBarHistory(
        text = stringResource(id = R.string.history),
        navController = navController,
        list = result,
        noHistory = noHistory
    ) {

        if (showDialog.value)
            DialogHistoryEntry(
                showDialog = showDialog,
                modelHistory = modelHistory.value
            )

        if (showCircularIndicator.value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (noHistory.value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                MyLottieAnim(
                    modifier = Modifier.fillMaxSize(0.5f),
                    lottieID = R.raw.empty_box,
                    isLottieAnimationPlaying = noHistory
                )
            }
        }

        LazyVerticalGrid(modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp, vertical = SpacersSize.medium),
            state = state,
            cells = GridCells.Fixed(count = 2),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            content = {
                items(count = result.value.size) { index ->
                    val history = result.value[index]
                    Card(
                        modifier = Modifier.fillMaxHeight(0.2f),
                        backgroundColor = Blue,
                        shape = Shapes.medium
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(SpacersSize.small)
                                .clickable {
                                    showDialog.value = true
                                    modelHistory.value = history
                                }, horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            MyText(text = history.type, color = Color.White)

                            MySpacer(type = "small")

                            MyText(text = history.input, color = Color.White)

                        }
                    }
                }
            })
    }
}

@Composable
fun DialogHistoryEntry(
    showDialog: MutableState<Boolean>,
    modelHistory: ModelHistory
) {

    Dialog(onDismissRequest = {
        showDialog.value = false
    }) {
        Column(
            modifier = Modifier
                .background(color = Color.White, shape = Shapes.medium)
                .padding(SpacersSize.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MyText(text = modelHistory.type)
                MyText(text = modelHistory.date)
            }

            MySpacer(type = "medium")

            MyText(modifier = Modifier.clickable {
                Helpers.copyToClipBoard(modelHistory.input, msgID = R.string.text_copied)
            }, text = modelHistory.input)

            MySpacer(type = "medium")

            MyText(modifier = Modifier.clickable {
                Helpers.copyToClipBoard(modelHistory.output, msgID = R.string.text_copied)
            }, text = modelHistory.output)

        }
    }

}

@Composable
fun TopBarHistory(
    modifier: Modifier = Modifier,
    text: String,
    navController: NavController,
    list: MutableState<MutableList<ModelHistory>>,
    noHistory: MutableState<Boolean>,
    content: @Composable () -> Unit,
) {

    val showConfirmationDialog = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        val padding = when (rememberWindowInfo().screenWidthInfo) {
            is WindowInfo.WindowType.Compact -> 0.dp
            is WindowInfo.WindowType.Medium -> 10.dp
            else -> 20.dp
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Blue)
                .padding(padding),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = {
                navController.navigate(Screens.ScreenHome.route)
                SettingsNotifier.resetValues()
            }) {

                MyIcon(
                    iconID = R.drawable.icon_arrow_back,
                    contentDesc = stringResource(
                        id = R.string.navigate_back
                    ),
                    tint = Color.White
                )
            }

            when (rememberWindowInfo().screenWidthInfo) {
                is WindowInfo.WindowType.Compact -> Spacer(modifier = Modifier.width(SpacersSize.small))
                is WindowInfo.WindowType.Medium -> Spacer(modifier = Modifier.width(SpacersSize.small))
                else -> Spacer(modifier = Modifier.width(SpacersSize.medium))
            }

            MyText(text = text, color = Color.White, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(SpacersSize.small))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                IconButton(onClick = {
                    showConfirmationDialog.value = true
                }) {
                    MyIcon(
                        iconID = R.drawable.icon_delete,
                        contentDesc = stringResource(id = R.string.delete),
                        tint = Color.White
                    )
                }

            }
        }

        if (showConfirmationDialog.value)
            DialogConfirmation(
                showDialog = showConfirmationDialog,
                title = stringResource(id = R.string.deletion_confirmation)
            ) {
                HelperFirebaseDatabase.deleteAllHistory()
                list.value.clear()
                noHistory.value = true
            }

        content()
    }
}