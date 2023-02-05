package com.jihad.aiwriter.feature_generate_text.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.jihad.aiwriter.App
import com.jihad.aiwriter.R
import com.jihad.aiwriter.components.MyButton
import com.jihad.aiwriter.components.MySpacer
import com.jihad.aiwriter.components.MyText
import com.jihad.aiwriter.components.myOutlinedTextField
import com.jihad.aiwriter.extensions.sectionsGridContent
import com.jihad.aiwriter.ui.theme.Glass
import com.jihad.aiwriter.ui.theme.Shapes
import com.jihad.aiwriter.ui.theme.SpacersSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.concurrent.timerTask


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScreenHome(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val context = LocalContext.current
    val state = rememberLazyListState()
    val showDialog = remember {
        mutableStateOf(false)
    }
    val focusRequester = remember {
        FocusRequester()
    }
    val verticalScroll = rememberScrollState()

    if (showDialog.value) {
        Timer().schedule(timerTask {
            runBlocking(Dispatchers.Main){
                focusRequester.requestFocus()
            }
        }, 1000)
        Dialog(onDismissRequest = {
            showDialog.value = false
        }) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(verticalScroll)
                    .background(color = Glass, shape = Shapes.medium)
                    .padding(SpacersSize.medium)
            ) {

                MyText(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.add_your_option_label),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )

                MySpacer(type = "medium")

                val action = myOutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    placeHolder = stringResource(
                        id = R.string.write_a_facebook_post
                    )
                )

                MySpacer(type = "medium")

                MyButton(
                    text = stringResource(id = R.string.add),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    // todo add custom section

                }
            }
        }
    }

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        state = state,
        cells = GridCells.Fixed(count = 2),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        content = {
            sectionsGridContent(
                map = App.mapOfScreens,
                2,
                state,
                navController,
                showDialog = showDialog
            )
        })
}