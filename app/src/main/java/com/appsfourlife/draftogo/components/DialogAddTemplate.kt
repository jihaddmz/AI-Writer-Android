package com.appsfourlife.draftogo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelTemplate
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timerTask

@Composable
fun DialogAddTemplate(
    modifier: Modifier = Modifier,
    showDialog: MutableState<Boolean>,
    onTemplateAdded: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Dialog(onDismissRequest = {
        showDialog.value = false
    }) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = Shapes.medium)
                .padding(SpacersSize.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val input = myTextFieldLabel(
                label = stringResource(id = R.string.input), placeholder = stringResource(
                    id = R.string.add_template_example
                )
            )

            MySpacer(type = "small")

            val imageUrl = myTextFieldLabel(
                label = stringResource(id = R.string.image_url),
                placeholder = "image"
            )

            MySpacer(type = "small")

            MyButton(text = stringResource(id = R.string.add), modifier = Modifier.fillMaxWidth()) {
                coroutineScope.launch(Dispatchers.IO) {
                    App.dbGenerateText.daoTemplates.insertTemplate(ModelTemplate(query = input, imageUrl = ""))
                    showDialog.value = false

                    Timer().schedule(timerTask {
                        onTemplateAdded()
                    }, 1000)
                }
            }
        }
    }
}