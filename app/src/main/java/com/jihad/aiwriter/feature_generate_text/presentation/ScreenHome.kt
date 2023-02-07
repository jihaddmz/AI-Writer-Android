package com.jihad.aiwriter.feature_generate_text.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jihad.aiwriter.App
import com.jihad.aiwriter.R
import com.jihad.aiwriter.components.*
import com.jihad.aiwriter.extensions.sectionsGridContent
import com.jihad.aiwriter.feature_generate_text.util.Screens
import java.util.*


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScreenHome(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val context = LocalContext.current
    val state = rememberLazyListState()

    Column(modifier = modifier.fillMaxSize()) {

        MainAppBar(navController = navController)

        MySpacer(type = "small")

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
                )
            })
    }
}

@Composable
fun MainAppBar(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        MyIconOutlinedButton(
            imageID = R.drawable.icon_settings, contentDesc = stringResource(
                id = R.string.settings
            )
        ) {
            navController.navigate(Screens.ScreenSettings.route)
        }
    }
}