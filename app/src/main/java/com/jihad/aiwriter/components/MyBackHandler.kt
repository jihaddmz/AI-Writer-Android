package com.jihad.aiwriter.components

import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jihad.aiwriter.R
import com.jihad.aiwriter.feature_generate_text.presentation.ScreenHome
import com.jihad.aiwriter.helpers.Constants
import com.jihad.aiwriter.helpers.HelperUI
import com.jihad.aiwriter.ui.theme.SpacersSize
import java.util.*
import kotlin.concurrent.timerTask

@Composable
fun MyBackHandler(context: Activity) {
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