package com.appsfourlife.draftogo.components

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.helpers.HelperUI
import java.util.*
import kotlin.concurrent.timerTask

@Composable
fun HomeBackHandler(context: Activity) {
    var counter = 0
    Timer().scheduleAtFixedRate(timerTask {
        counter = 0
    }, 0, Constants.EXIT_TIME_BETWEEN)
    BackHandler(true) {
        counter++
        if (counter == 2)
            context.finish()
        else if (counter == 1)
            HelperUI.showToast(
                context,
                context.getString(R.string.back_button_exit_text)
            )
    }
}