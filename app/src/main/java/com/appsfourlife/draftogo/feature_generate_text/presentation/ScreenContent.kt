package com.appsfourlife.draftogo.feature_generate_text.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.IconButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.extensions.animateOffsetY
import com.appsfourlife.draftogo.extensions.animateVisibility
import com.appsfourlife.draftogo.extensions.sectionsGridContent
import com.appsfourlife.draftogo.helpers.HelperAnalytics
import com.appsfourlife.draftogo.helpers.HelperUI
import com.appsfourlife.draftogo.ui.theme.Amber
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.Screens
import com.appsfourlife.draftogo.util.SettingsNotifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ScreenContent(
    modifier: Modifier = Modifier, navController: NavController
) {

    val context = LocalContext.current
    val state = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()


    SettingsNotifier.disableDrawerContent.value = false
    SettingsNotifier.enableSheetContent.value = false

    LaunchedEffect(key1 = true, block = {
        coroutineScope.launch(Dispatchers.IO) {
            SettingsNotifier.predefinedTemplates.value =
                App.databaseApp.daoApp.getAllTemplates()
            SettingsNotifier.predefinedTemplates.value =
                SettingsNotifier.predefinedTemplates.value.sortedBy { it.userAdded }
        }
    })

    Column(modifier = modifier.fillMaxSize()) {

        if (SettingsNotifier.showAddTemplateDialog.value)
            DialogAddTemplate(
                showDialog = SettingsNotifier.showAddTemplateDialog,
                onTemplateAdded = {
                    coroutineScope.launch(Dispatchers.IO) {
                        SettingsNotifier.predefinedTemplates.value =
                            App.databaseApp.daoApp.getAllTemplates()
                                .sortedBy { it.userAdded }
                    }
                })

        if (SettingsNotifier.showDeleteTemplateDialog.value)
            DialogConfirmation(
                showDialog = SettingsNotifier.showDeleteTemplateDialog, title = stringResource(
                    id = R.string.delete_template_confirmation
                )
            ) {
                coroutineScope.launch(Dispatchers.IO) {
                    SettingsNotifier.templateToDelete?.let { modelTemplate ->
                        App.databaseApp.daoApp.deleteTemplate(modelTemplate)
                        delay(1000)
                        SettingsNotifier.predefinedTemplates.value =
                            App.databaseApp.daoApp.getAllTemplates()
                                .sortedBy { it.userAdded }
                    }
                }
            }

        MainAppBar(
            navController = navController,
            coroutineScope = coroutineScope,
        )

        MySpacer(type = "small")

        Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            MyTextLink(
                text = stringResource(id = R.string.view_history),
                modifier = Modifier
                    .padding(end = SpacersSize.medium)
                    .clickable {
                        HelperAnalytics.sendEvent("history")
                        // if there is network access, navigate to history
                        if (SettingsNotifier.isConnected.value) {
                            navController.navigate(Screens.ScreenHistory.route)
                        } else {
                            HelperUI.showToast(
                                msg = App.getTextFromString(
                                    R.string.no_connection
                                )
                            )
                        }
                    },
                textAlign = TextAlign.End
            )
        }

        MySpacer(type = "small")

        LazyVerticalGrid(modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp),
            state = state,
            columns = GridCells.Fixed(count = 2),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            content = {
                sectionsGridContent(
                    list = SettingsNotifier.predefinedTemplates.value,
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
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        IconButton(
            onClick = {
                SettingsNotifier.showAddTemplateDialog.value = true
            }, modifier = Modifier
                .animateOffsetY(initialOffsetY = (-100).dp, delay = 0)
        ) {
            MyIcon(
                iconID = R.drawable.icon_add_new,
                contentDesc = stringResource(id = R.string.add_your_template),
                tint = Color.Red
            )
        }

        val showSearch = remember {
            mutableStateOf(true)
        }
        val showSearchExpanded = remember {
            mutableStateOf(false)
        }
        val searchText = remember {
            mutableStateOf("")
        }
        if (showSearch.value) {
            IconButton(onClick = {
                HelperAnalytics.sendEvent("search_templates")

                showSearch.value = false
                showSearchExpanded.value = true
            }, modifier = Modifier.animateOffsetY(initialOffsetY = (-100).dp, delay = 200)) {
                MyIcon(
                    iconID = R.drawable.icon_search,
                    contentDesc = stringResource(id = R.string.search),
                    tint = Color.Black
                )
            }

        }

        if (showSearchExpanded.value) {
            val focusRequester = FocusRequester()

            LaunchedEffect(key1 = true, block = {
                focusRequester.requestFocus()
            })

            MyTextField(modifier = Modifier
                .padding(SpacersSize.small)
                .animateVisibility()
                .weight(1f)
                .focusRequester(focusRequester)
                .background(color = Blue, shape = Shapes.medium),
                value = searchText.value,
                textColor = Color.Black,
                trailingIconTint = Amber,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                cursorColor = Color.White,
                placeholder = stringResource(id = R.string.search),
                trailingIcon = R.drawable.icon_wrong,
                onTrailingIconClick = {
                    searchText.value = ""
                    showSearchExpanded.value = false
                    showSearch.value = true
                    coroutineScope.launch(Dispatchers.IO) {
                        SettingsNotifier.predefinedTemplates.value =
                            App.databaseApp.daoApp.getAllTemplates()
                                .sortedBy { it.userAdded }
                    }
                },
                onValueChanged = { itr ->
                    searchText.value = itr
                    if (searchText.value.isEmpty()) {
                        coroutineScope.launch(Dispatchers.IO) {
                            SettingsNotifier.predefinedTemplates.value =
                                App.databaseApp.daoApp.getAllTemplates()
                        }
                    } else {
                        coroutineScope.launch(Dispatchers.IO) {
                            SettingsNotifier.predefinedTemplates.value =
                                App.databaseApp.daoApp.getAllTemplates().filter {
                                    it.query.lowercase().contains(searchText.value.lowercase())
                                }
                        }
                    }
                })
        }
    }
}