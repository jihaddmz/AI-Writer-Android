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
import com.appsfourlife.draftogo.extensions.animateOffsetY
import com.appsfourlife.draftogo.extensions.sectionsGridContent
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelTemplate
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.util.Screens
import com.appsfourlife.draftogo.util.SettingsNotifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
        mutableStateOf(SettingsNotifier.predefinedTemplates)
    }

    SettingsNotifier.disableDrawerContent.value = false

    LaunchedEffect(key1 = true, block = {
        coroutineScope.launch(Dispatchers.IO) {

            SettingsNotifier.predefinedTemplates = App.dbGenerateText.daoTemplates.getAllTemplates()
            listOfPredefinedTemplates.value = SettingsNotifier.predefinedTemplates.sortedBy { it.userAdded }

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

        if (SettingsNotifier.showAddTemplateDialog.value)
            DialogAddTemplate(
                showDialog = SettingsNotifier.showAddTemplateDialog,
                onTemplateAdded = {
                    coroutineScope.launch(Dispatchers.IO) {
                        listOfPredefinedTemplates.value =
                            App.dbGenerateText.daoTemplates.getAllTemplates().sortedBy { it.userAdded }
                        SettingsNotifier.predefinedTemplates = listOfPredefinedTemplates.value
                    }
                })

        if (SettingsNotifier.showDeleteTemplateDialog.value)
            DialogConfirmation(
                showDialog = SettingsNotifier.showDeleteTemplateDialog, title = stringResource(
                    id = R.string.delete_template_confirmation
                )
            ) {
                coroutineScope.launch(Dispatchers.IO) {
                    SettingsNotifier.templateToDelete?.let {
                        App.dbGenerateText.daoTemplates.deleteTemplate(it)
                        delay(1000)
                        listOfPredefinedTemplates.value =
                            App.dbGenerateText.daoTemplates.getAllTemplates()
                        SettingsNotifier.predefinedTemplates = listOfPredefinedTemplates.value
                    }
                }
            }

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
    listOfPredefinedTemplates: MutableState<List<ModelTemplate>>
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        IconButton(
            onClick = {
                // if there is network access, navigate to history
                if (SettingsNotifier.isConnected.value) {
                    navController.navigate(Screens.ScreenHistory.route)
                } else {
                    HelperUI.showToast(msg = App.getTextFromString(R.string.no_connection))
                }
            }, modifier = Modifier
                .animateOffsetY(initialOffsetY = (-100).dp)
        ) {
            MyIcon(
                iconID = R.drawable.icon_history,
                contentDesc = stringResource(id = R.string.history)
            )
        }

        IconButton(
            onClick = {
                SettingsNotifier.showAddTemplateDialog.value = true
            }, modifier = Modifier
                .animateOffsetY(initialOffsetY = (-100).dp, delay = 100)
        ) {
            MyIcon(
                iconID = R.drawable.icon_add_new,
                contentDesc = stringResource(id = R.string.add_your_template)
            )
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
            }, modifier = Modifier.animateOffsetY(initialOffsetY = (-100).dp, delay = 200)) {
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
                    listOfPredefinedTemplates.value = SettingsNotifier.predefinedTemplates
                },
                onValueChanged = { itr ->
                    searchText.value = itr
                    if (searchText.value.isEmpty()) {
                        listOfPredefinedTemplates.value = SettingsNotifier.predefinedTemplates
                    } else listOfPredefinedTemplates.value =
                        SettingsNotifier.predefinedTemplates.filter {
                            it.query.lowercase().contains(searchText.value.lowercase())
                        }
                })
        }

        IconButton(
            onClick = {
                navController.navigate(Screens.ScreenSettings.route)
            }, modifier = Modifier
                .animateOffsetY(initialOffsetY = (-100).dp, delay = 300)
        ) {
            MyIcon(
                iconID = R.drawable.icon_settings,
                contentDesc = stringResource(id = R.string.settings)
            )
        }
    }
}