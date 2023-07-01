package com.appsfourlife.draftogo.feature_generate_video.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.MyOutlinedButton
import com.appsfourlife.draftogo.components.MySpacer
import com.appsfourlife.draftogo.components.VideoView
import com.appsfourlife.draftogo.feature_generate_video.models.ModelVideoTemplate
import com.appsfourlife.draftogo.ui.theme.SpacersSize

@Composable
fun ItemVideoTemplate(
    modifier: Modifier = Modifier,
    modelVideoTemplate: ModelVideoTemplate,
    onUseBtnClick: (ModelVideoTemplate) -> Unit
) {
    Column(modifier = modifier) {
        VideoView(uri = modelVideoTemplate.exampleUrl, autoPlay = true)

        MySpacer(type = "small")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacersSize.medium),
            horizontalArrangement = Arrangement.Center
        ) {

            MyOutlinedButton(text = stringResource(id = R.string.use_template)) {
                onUseBtnClick(modelVideoTemplate)
            }
        }
    }
}