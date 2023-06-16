package com.appsfourlife.draftogo.feature_generate_text.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.extensions.animateScaling
import com.appsfourlife.draftogo.feature_generate_text.models.ModelHistory
import com.appsfourlife.draftogo.helpers.HelperFirebaseDatabase
import com.appsfourlife.draftogo.helpers.Helpers
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.BottomNavScreens

@Composable
fun ScreenHistory(
    modifier: Modifier = Modifier,
    navController: NavController
) {

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

    LaunchedEffect(key1 = true, block = {
        HelperFirebaseDatabase.fetchHistory(result, noHistory, showCircularIndicator)
    })

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
                MyLottieAnim(R.raw.loading)
            }
        } else if (noHistory.value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                MyLottieAnim(
                    modifier = Modifier.fillMaxSize(0.5f),
                    lottieID = R.raw.empty_box,
                )
            }
        }

        LazyVerticalGrid(modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp, vertical = SpacersSize.medium),
            columns = GridCells.Fixed(count = 2),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            content = {
                items(count = result.value.size) { index ->
                    val history = result.value[index]
                    MyCardView(
                        modifier = Modifier
                            .fillMaxHeight(0.2f)
                            .animateScaling(),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .padding(SpacersSize.small)
                                .clickable {
                                    showDialog.value = true
                                    modelHistory.value = history
                                }, horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            MyText(text = history.type)

                            MySpacer(type = "small")

                            MyText(text = history.input, textAlign = TextAlign.Center)

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

    val verticalScroll = rememberScrollState()

    Dialog(onDismissRequest = {
        showDialog.value = false
    }) {
        Column(
            modifier = Modifier
                .background(color = Color.White, shape = Shapes.medium)
                .padding(SpacersSize.medium)
                .verticalScroll(verticalScroll)
                .animateScaling(),
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

            Divider(color = Blue)

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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = SpacersSize.small),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AppBarTransparent(title = text, modifier = Modifier
                .fillMaxWidth(0.5f)
                .weight(1f)) {
                navController.navigate(BottomNavScreens.Content.route)
            }

            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.CenterEnd) {
                IconButton(onClick = {
                    showConfirmationDialog.value = true
                }) {
                    MyIcon(
                        iconID = R.drawable.icon_delete,
                        contentDesc = stringResource(id = R.string.delete),
                        tint = Color.Black
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
                list.value = mutableListOf()
                noHistory.value = true
            }

        content()
    }
}