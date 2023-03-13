package com.appsfourlife.draftogo.feature_generate_text.presentation

import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.BuildConfig
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.extensions.sectionsGridContent
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.helpers.HelperFirebaseDatabase
import com.appsfourlife.draftogo.util.Screens
import com.appsfourlife.draftogo.helpers.HelperUI
import com.appsfourlife.draftogo.helpers.Helpers
import com.appsfourlife.draftogo.util.SettingsNotifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScreenHome(
    modifier: Modifier = Modifier, navController: NavController
) {

    val context = LocalContext.current
    val state = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val isAppOutDated = remember {
        mutableStateOf(false)
    }
    val listOfPredefinedTemplates = remember {
        Constants.PREDEFINED_TEMPLATES
    }

    LaunchedEffect(key1 = true, block = {
        coroutineScope.launch(Dispatchers.IO) {
            HelperFirebaseDatabase.fetchAppVersion {
                isAppOutDated.value = it != BuildConfig.VERSION_NAME
            }
        }
    })

    HelperFirebaseDatabase.fetchNbOfGenerationsConsumed()

    Column(modifier = modifier.fillMaxSize()) {

        if (isAppOutDated.value) // if the app is outdated show the alert dialog to update
            MyDialog(
                modifier = Modifier,
                showDialog = isAppOutDated,
                text = stringResource(id = R.string.app_is_outdated),
                title = stringResource(id = R.string.attention),
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            )

        MainAppBar(
            navController = navController,
            coroutineScope = coroutineScope,
            listOfPredefinedTemplates = listOfPredefinedTemplates
        )

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
                    list = listOfPredefinedTemplates.value,
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
    coroutineScope: CoroutineScope,
    listOfPredefinedTemplates: MutableState<List<String>>
) {

    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        MyIconOutlinedButton(
            modifier = Modifier.weight(0.5f),
            imageID = R.drawable.icon_history, contentDesc = stringResource(
                id = R.string.history
            )
        ) {
            // if there is network access, navigate to history
            Helpers.checkForConnection(coroutineScope, ifConnected = {
                coroutineScope.launch(Dispatchers.Main) {
                    navController.navigate(Screens.ScreenHistory.route)
                }
            }, notConnected = {
                coroutineScope.launch(Dispatchers.Main) {
                    HelperUI.showToast(msg = App.getTextFromString(R.string.no_connection))
                }
            })
        }

        val searchText = remember {
            mutableStateOf("")
        }
        MyTextField(
            modifier = Modifier.weight(1f),
            value = searchText.value,
            placeholder = stringResource(id = R.string.search),
            onValueChanged = { itr ->
                searchText.value = itr
                listOfPredefinedTemplates.value = if (searchText.value.isEmpty()) {
                    Constants.PREDEFINED_TEMPLATES.value
                } else {
                    val resultList = mutableListOf<String>()
                    Constants.PREDEFINED_TEMPLATES.value.forEach {
                        if (itr.contains(it))
                            resultList.add(it)
                    }
                    resultList
                }
//                if (searchText.value == "")
//                    listOfPredefinedTemplates.value = Constants.PREDEFINED_TEMPLATES.value
//
//                listOfPredefinedTemplates.value =
//                    Constants.PREDEFINED_TEMPLATES.value.forEach {
//
//                    }

            }
        )

        MyIconOutlinedButton(
            modifier = Modifier.weight(0.5f),
            imageID = R.drawable.icon_settings, contentDesc = stringResource(
                id = R.string.settings
            )
        ) {
            navController.navigate(Screens.ScreenSettings.route)
        }
    }
}