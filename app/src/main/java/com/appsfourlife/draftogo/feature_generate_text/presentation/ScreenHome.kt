package com.appsfourlife.draftogo.feature_generate_text.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.SettingsNotifier
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.extensions.sectionsGridContent
import com.appsfourlife.draftogo.feature_generate_text.util.Screens
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import java.util.*
import kotlin.concurrent.timerTask


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScreenHome(
    modifier: Modifier = Modifier, navController: NavController
) {

    /**
     * checking if user is signed in, if not show dialog sign in
     **/
    if (HelperSharedPreference.getBool(
            HelperSharedPreference.SP_SETTINGS,
            HelperSharedPreference.SP_SETTINGS_OUTPUT_SHOW_DIALOG_SIGNIN, true
        )
    ) {
        LaunchedEffect(key1 = true, block = {
            Timer().schedule(timerTask {
                if (HelperSharedPreference.getUsername() == "") {
                    SettingsNotifier.showDialogSignIn.value = true
                }
            }, 1000)
        })
    }

    val context = LocalContext.current
    val state = rememberLazyListState()

    if (SettingsNotifier.showDialogSignIn.value) // if dialog should be showed
        DialogSignIn()

    Column(modifier = modifier.fillMaxSize()) {

        MainAppBar(navController = navController)

        MySpacer(type = "small")

        LazyVerticalGrid(modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp),
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
    navController: NavController,
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