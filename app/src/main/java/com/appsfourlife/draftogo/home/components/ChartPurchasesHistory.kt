package com.appsfourlife.draftogo.home.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.MyLottieAnim
import com.appsfourlife.draftogo.components.MySpacer
import com.appsfourlife.draftogo.components.MyTextTitle
import com.appsfourlife.draftogo.helpers.Helpers
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Rose
import com.himanshoe.charty.bar.BarChart
import com.himanshoe.charty.bar.config.BarConfig
import com.himanshoe.charty.bar.model.BarData
import com.himanshoe.charty.common.axis.AxisConfig

@SuppressLint("UnrememberedMutableState")
@Composable
fun ChartPurchasesHistory(
    colors: List<Color> = listOf(Rose)
) {
//   val list = listOf(
//        // todo update history purchases
//        BarData("14/4/2023", 24f),
//        BarData("14/5/2023", 27f),
//        BarData("14/6/2023", 27f),
//        BarData("14/7/2023", 27f),
//        BarData("14/8/2023", 27f),
//    )

    val list = listOf<BarData>()

    Column(modifier = Modifier.fillMaxSize()) {
        MyTextTitle(
            text = stringResource(id = R.string.purchase_history),
            modifier = Modifier
                .fillMaxWidth(),
            color = Color.White
        )
        MySpacer(type = "medium")

        if (list.isNotEmpty()) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
            ) {

                item {
                    BarChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(32.dp),
                        onBarClick = { barData ->
                            Helpers.logD("${barData.yValue}")
                        },
                        barConfig = BarConfig(),
                        axisConfig = AxisConfig(
                            showAxes = true,
                            showUnitLabels = true,
                            xAxisColor = Blue,
                            yAxisColor = Color.Black
                        ),
                        color = colors.first(),
                        barData = list
                    )
                }
            }
        } else {
            MyLottieAnim(
                modifier = Modifier
                    .fillMaxSize(),
                lottieID = R.raw.empty,
                isLottieAnimationPlaying = mutableStateOf(true)
            )
        }
    }
}