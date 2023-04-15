package com.appsfourlife.draftogo.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.util.SettingsNotifier

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomSheetSavedOutputs() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .padding(horizontal = 32.dp, vertical = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth(0.2f)
                            .height(5.dp)
                            .background(shape = Shapes.large, color = Color.LightGray),
                        thickness = 3.dp,
                    )
                }
                MySpacer(type = "small")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MyIcon(iconID = R.drawable.icon_save, contentDesc = "", tint = Color.LightGray)
                    MySpacer(type = "small", widthOrHeight = "width")
                    Text(
                        text = stringResource(id = R.string.save_outputs),
                        style = MaterialTheme.typography.h6
                    )
                }
            }
        }

        if (SettingsNotifier.comparisonGenerationEntries.value.isEmpty()) {
            item {
                MySpacer(type = "medium")
                MyTipText(text = stringResource(id = R.string.label_no_saved_items_added))

                MySpacer(type = "medium")

                MyLottieAnim(
                    lottieID = R.raw.empty_box,
                )
            }
        }

        items(
            SettingsNotifier.comparisonGenerationEntries.value.size,
            key = { "${SettingsNotifier.comparisonGenerationEntries.value[it].output} ${SettingsNotifier.comparisonGenerationEntries.value[it].input}" }) { index ->
            MySpacer(type = "medium")
            val modelComparedGenerationItem =
                SettingsNotifier.comparisonGenerationEntries.value[index]
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItemPlacement(),
            ) {
                MyLabelText(
                    label = "${stringResource(id = R.string.input)}:",
                    text = "\n${modelComparedGenerationItem.input}"
                )
                MySpacer(type = "small")
                SelectionContainer {
                    MyLabelText(
                        label = "${stringResource(id = R.string.output)}:",
                        text = "\n${modelComparedGenerationItem.output}"
                    )
                }

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    MyOutlinedButton(text = stringResource(id = R.string.delete)) {
                        SettingsNotifier.deleteComparisonGenerationEntry(index)
                    }
                }
                Divider(color = Blue, thickness = 2.dp)
            }
        }
    }
}