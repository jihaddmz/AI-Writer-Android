package com.appsfourlife.draftogo.feature_generate_text.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
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
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.util.Screens
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
        mutableStateOf(Constants.PREDEFINED_TEMPLATES)
    }

    LaunchedEffect(key1 = true, block = {
        coroutineScope.launch(Dispatchers.IO) {
            HelperFirebaseDatabase.fetchAppVersion {
                isAppOutDated.value = it != BuildConfig.VERSION_NAME
            }

            HelperFirebaseDatabase.fetchNbOfGenerationsConsumed()

            // if the user is on base plan subscription, check if we are currently on the same renewal date, if so
            // reset all values on firebase and set the new renewal date, if no, check if currently we are after the
            // renewal date on firebase, if so reset the value on firebase and set the new renewal date
                HelperFirebaseDatabase.getRenewalDate {
                    if (it != "null" && it != "")
                        if (it == HelperDate.getCurrentDateInString()) {
                            HelperFirebaseDatabase.resetNbOfGenerationsConsumedAndNbOfWordsGenerated()
                            HelperFirebaseDatabase.setRenewalDate()
                        } else {
                            val dateInFirebase =
                                HelperDate.parseStringToDate(it, Constants.DAY_MONTH_YEAR_FORMAT)
                            val dateNow = HelperDate.parseStringToDate(
                                HelperDate.parseDateToString(
                                    Date(),
                                    Constants.DAY_MONTH_YEAR_FORMAT
                                ), Constants.DAY_MONTH_YEAR_FORMAT
                            )
                            dateNow?.let { dateNow ->
                                if (dateNow.after(dateInFirebase)) {
                                    HelperFirebaseDatabase.resetNbOfGenerationsConsumedAndNbOfWordsGenerated()
                                    HelperFirebaseDatabase.setRenewalDate()
                                }
                            }
                        }
                }
        }
    })

    Column(modifier = modifier.fillMaxSize()) {

        if (isAppOutDated.value) // if the app is outdated show the alert dialog to update
            MyDialog(
                modifier = Modifier,
                showDialog = isAppOutDated,
                text = stringResource(id = R.string.app_is_outdated),
                title = stringResource(id = R.string.attention),
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false,
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
            imageID = R.drawable.icon_history,
            contentDesc = stringResource(
                id = R.string.history
            )
        ) {
            // if there is network access, navigate to history
            if (SettingsNotifier.isConnected.value) {
                navController.navigate(Screens.ScreenHistory.route)
            } else {
                HelperUI.showToast(msg = App.getTextFromString(R.string.no_connection))
            }
        }

        val showSearch = remember {
            mutableStateOf(false)
        }
        val searchText = remember {
            mutableStateOf("")
        }

        MyAnimatedVisibility(visible = !showSearch.value) {
            IconButton(onClick = {
                showSearch.value = true
            }) {
                MyIcon(
                    iconID = R.drawable.icon_search,
                    contentDesc = stringResource(id = R.string.search)
                )
            }

        }

        MyAnimatedVisibility(visible = showSearch.value) {

            val focusRequester = FocusRequester()

            LaunchedEffect(key1 = true, block = {
                focusRequester.requestFocus()
            })

            MyTextField(modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
                .background(color = Blue, shape = Shapes.medium),
                value = searchText.value,
                textColor = Color.White,
                cursorColor = Color.White,
                placeholder = stringResource(id = R.string.search),
                trailingIcon = R.drawable.icon_wrong,
                onTrailingIconClick = {
                    searchText.value = ""
                    showSearch.value = false
                    listOfPredefinedTemplates.value = Constants.PREDEFINED_TEMPLATES
                },
                onValueChanged = { itr ->
                    searchText.value = itr
                    if (searchText.value.isEmpty()) {
                        listOfPredefinedTemplates.value = Constants.PREDEFINED_TEMPLATES
                    } else listOfPredefinedTemplates.value =
                        Constants.PREDEFINED_TEMPLATES.filter { it.lowercase().contains(searchText.value) }
                })
        }

        MyIconOutlinedButton(
            modifier = Modifier.weight(0.5f),
            imageID = R.drawable.icon_settings,
            contentDesc = stringResource(
                id = R.string.settings
            )
        ) {
            navController.navigate(Screens.ScreenSettings.route)
        }
    }
}