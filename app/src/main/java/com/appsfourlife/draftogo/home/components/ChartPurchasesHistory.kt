package com.appsfourlife.draftogo.home.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.MyLottieAnim
import com.appsfourlife.draftogo.components.MySpacer
import com.appsfourlife.draftogo.components.MyTextTitle
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelPurchaseHistory
import com.appsfourlife.draftogo.helpers.HelperDate
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Rose
import com.himanshoe.charty.bar.BarChart
import com.himanshoe.charty.bar.config.BarConfig
import com.himanshoe.charty.bar.model.BarData
import com.himanshoe.charty.common.axis.AxisConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@Composable
fun ChartPurchasesHistory(
    colors: List<Color> = listOf(Rose)
) {
    val coroutineScope = rememberCoroutineScope()

//   val listOf = listOf(
//        // todo update history purchases
//        BarData("14/4/2023", 24f),
//        BarData("14/5/2023", 27f),
//        BarData("14/6/2023", 27f),
//        BarData("14/7/2023", 27f),
//        BarData("14/8/2023", 27f),
//    )

    val list = remember {
        mutableStateOf(listOf<BarData>())
    }

    val listOfPurchases = remember {
        mutableStateOf(listOf<ModelPurchaseHistory>())
    }

    LaunchedEffect(key1 = true, block = {
        coroutineScope.launch(Dispatchers.IO) {
            listOfPurchases.value = App.databaseApp.daoApp.getAllPurchaseHistory()
                .sortedBy { HelperDate.parseStringToDate(it.date, "dd/MM/yyyy") }
            listOfPurchases.value = listOfPurchases.value.reversed()

            if (listOfPurchases.value.isNotEmpty()) {
                val result = mutableListOf<BarData>()
                val lastIndex = if (listOfPurchases.value.size >= 5) {
                    5
                } else {
                    listOfPurchases.value.size
                }
                for (i in 0 until lastIndex) {
                    val current = listOfPurchases.value[i]
                    result.add(BarData(current.date, current.price))
                }
                if (result.size == 1)
                    result.add(BarData("", 0f))
                list.value = result
            }
        }
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MyTextTitle(
            text = stringResource(id = R.string.purchase_history),
            modifier = Modifier
                .fillMaxWidth(),
            color = Color.White
        )
        MySpacer(type = "medium")

        if (list.value.isNotEmpty()) {
            BarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .padding(32.dp),
                onBarClick = { barData ->
                },
                barConfig = BarConfig(hasRoundedCorner = false),
                axisConfig = AxisConfig(
                    showAxes = true,
                    showUnitLabels = true,
                    xAxisColor = Blue,
                    yAxisColor = Color.Black
                ),
                color = colors.first(),
                barData = list.value
            )
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