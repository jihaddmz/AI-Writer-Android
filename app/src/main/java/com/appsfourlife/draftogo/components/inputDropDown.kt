package com.appsfourlife.draftogo.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize


@Composable
fun inputDropDown(
    modifier: Modifier = Modifier,
    label: String = "",
    list: List<String> = Constants.listOfProgrammingLanguages.toMutableList(),
    onItemSelect: () -> Unit = {}
): String {

    val expand = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    val chosenItem = remember {
        when (list) {
            App.listOfLetterTypes -> {
                mutableStateOf(
                    HelperSharedPreference.getString(
                        HelperSharedPreference.SP_SETTINGS,
                        HelperSharedPreference.SP_SETTINGS_LETTER_TYPE,
                        App.getTextFromString(R.string.normal_letter),
                        context = context
                    )
                )
            }
            App.listOfCVTypes -> {
                mutableStateOf(
                    HelperSharedPreference.getString(
                        HelperSharedPreference.SP_SETTINGS,
                        HelperSharedPreference.SP_SETTINGS_CV_TYPE,
                        App.getTextFromString(R.string.chronological),
                        context
                    )
                )
            }
            Constants.listOfProgrammingLanguages -> {
                mutableStateOf(
                    HelperSharedPreference.getString(
                        HelperSharedPreference.SP_SETTINGS,
                        HelperSharedPreference.SP_SETTINGS_PROGRAMMING_LANGUAGE,
                        "java",
                        context
                    )
                )
            }
            App.listOfEssays -> {
                mutableStateOf(
                    HelperSharedPreference.getString(
                        HelperSharedPreference.SP_SETTINGS,
                        HelperSharedPreference.SP_SETTINGS_ESSAY_TYPE,
                        App.getTextFromString(R.string.normal_essay),
                        context
                    )
                )
            }
            Constants.OUTPUT_LANGUAGES -> {
                mutableStateOf(
                    HelperSharedPreference.getString(
                        HelperSharedPreference.SP_SETTINGS,
                        HelperSharedPreference.SP_SETTINGS_OUTPUT_LANGUAGE,
                        App.getTextFromString(R.string.english),
                        context
                    )
                )
            }
            Constants.listOfPodcastTypes -> {
                mutableStateOf(
                    HelperSharedPreference.getString(
                        HelperSharedPreference.SP_SETTINGS,
                        HelperSharedPreference.SP_SETTINGS_PODCAST_TYPE,
                        App.getTextFromString(R.string.solo),
                        context
                    )
                )
            }
            Constants.listOfGameConsoleTypes -> {
                mutableStateOf(
                    HelperSharedPreference.getString(
                        HelperSharedPreference.SP_SETTINGS,
                        HelperSharedPreference.SP_SETTINGS_GAME_CONSOLE_TYPE,
                        App.getTextFromString(R.string.playStation_5),
                        context
                    )
                )
            }
            Constants.FEEDBACK_TYPES -> {
                mutableStateOf(Constants.FEEDBACK_TYPES[0])
            }
            else -> {
                mutableStateOf("")
            }
        }
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (label.isNotEmpty())
            MyText(text = "$label:", fontWeight = FontWeight.Bold)

        Row(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .background(color = Color.White, shape = Shapes.medium)
                .animateContentSize(animationSpec = tween(durationMillis = 1000)),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (chosenItem.value.isNotEmpty())
                MyText(
                    modifier = Modifier.padding(start = SpacersSize.small, end = SpacersSize.small),
                    text = chosenItem.value,
                )

            val rotateDegree = remember {
                mutableStateOf(0f)
            }
            val animatedRotateDegree by animateFloatAsState(
                targetValue = rotateDegree.value,
                animationSpec = tween(durationMillis = 1000)
            )
            IconButton(onClick = {
                expand.value = true
                rotateDegree.value = 180f
            }) {
                MyIcon(
                    modifier = Modifier.rotate(animatedRotateDegree),
                    tint = Blue,
                    iconID = R.drawable.__icon_drop_down,
                    contentDesc = stringResource(
                        id = androidx.compose.ui.R.string.dropdown_menu
                    )
                )
            }

            DropdownMenu(modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(start = SpacersSize.medium),
                expanded = expand.value,
                onDismissRequest = {
                    expand.value = false
                    rotateDegree.value = 0f
                }) {

                list.forEach {

                    MyText(text = it, modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            rotateDegree.value = 0f

                            when (list) {
                                App.listOfLetterTypes -> {
                                    HelperSharedPreference.setString(
                                        HelperSharedPreference.SP_SETTINGS,
                                        HelperSharedPreference.SP_SETTINGS_LETTER_TYPE,
                                        it,
                                        context = context
                                    )
                                }
                                App.listOfCVTypes -> {
                                    HelperSharedPreference.setString(
                                        HelperSharedPreference.SP_SETTINGS,
                                        HelperSharedPreference.SP_SETTINGS_CV_TYPE,
                                        it,
                                        context
                                    )
                                }
                                Constants.listOfProgrammingLanguages -> {
                                    HelperSharedPreference.setString(
                                        HelperSharedPreference.SP_SETTINGS,
                                        HelperSharedPreference.SP_SETTINGS_PROGRAMMING_LANGUAGE,
                                        it,
                                        context
                                    )
                                }
                                App.listOfEssays -> {
                                    HelperSharedPreference.setString(
                                        HelperSharedPreference.SP_SETTINGS,
                                        HelperSharedPreference.SP_SETTINGS_ESSAY_TYPE,
                                        it,
                                        context
                                    )
                                }
                                Constants.OUTPUT_LANGUAGES -> {
                                    HelperSharedPreference.setString(
                                        HelperSharedPreference.SP_SETTINGS,
                                        HelperSharedPreference.SP_SETTINGS_OUTPUT_LANGUAGE,
                                        it,
                                        context
                                    )
                                }
                                Constants.listOfPodcastTypes -> {
                                    HelperSharedPreference.setString(
                                        HelperSharedPreference.SP_SETTINGS,
                                        HelperSharedPreference.SP_SETTINGS_PODCAST_TYPE,
                                        it,
                                        context
                                    )
                                }
                                Constants.listOfGameConsoleTypes -> {
                                    HelperSharedPreference.setString(
                                        HelperSharedPreference.SP_SETTINGS,
                                        HelperSharedPreference.SP_SETTINGS_GAME_CONSOLE_TYPE,
                                        it,
                                        context
                                    )
                                }
                            }
                            expand.value = false
                            chosenItem.value = it
                            onItemSelect()
                        })

                    Spacer(modifier = Modifier.height(SpacersSize.medium))
                }
            }
        }
    }

    return chosenItem.value
}