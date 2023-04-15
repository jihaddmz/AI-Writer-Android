package com.appsfourlife.draftogo.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.*
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun TypeModelChooser(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        MyText(
            text = "${stringResource(id = R.string.model_chooser_label)}:",
            fontWeight = FontWeight.Bold
        )

        val list = mutableListOf<MutableState<Int>>()
        Row {
            Constants.listOfWritingModels.forEach {
                val current = it
                val iconID = remember {
                    mutableStateOf(0)
                }

                if (HelperSharedPreference.getTypeOfWritingModel() == current) {
                    iconID.value = R.drawable.icon_checkcircle
                    list.add(iconID)
                }

                MyCardView(modifier = Modifier
                    .padding(SpacersSize.small)
                    .weight(1f)
                    .clickable {
                        list.forEach {
                            it.value = 0
                        }
                        list.add(iconID)
                        HelperSharedPreference.setTypeOfWritingModel(current)
                        iconID.value = R.drawable.icon_checkcircle
                    }) {
                    Column(
                        modifier = Modifier.padding(SpacersSize.small),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MyText(text = current)
                            if (iconID.value != 0) {
                                MyIcon(
                                    iconID = R.drawable.icon_checkcircle,
                                    contentDesc = "",
                                    tint = Color.Green
                                )
                            }
                        }
                        if (current.contains("turbo")) {
                            MyTipText(text = stringResource(id = R.string.turbo_model_explanation))
                        } else {
                            MyTipText(text = stringResource(id = R.string.davinci_model_explanation))
                        }
                    }
                }

                MySpacer(type = "small", widthOrHeight = "width")
            }
        }
    }
}