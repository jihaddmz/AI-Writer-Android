package com.appsfourlife.draftogo.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TypeWriterEffect(
    baseText: String,
    outputText: MutableState<String>,
    delay: Long = 0,
    finished: MutableState<Boolean>? = null
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true, block = {

        coroutineScope.launch {
            kotlinx.coroutines.delay(delay)
            baseText.forEachIndexed { index, c ->
                kotlinx.coroutines.delay(
                    100L
                )
                outputText.value = baseText.substring(startIndex = 0, endIndex = index + 1)
            }
            finished?.value = true
        }
    })
}