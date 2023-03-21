package com.appsfourlife.draftogo.components

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.extensions.animateOffsetX
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.SettingsNotifier
import kotlinx.coroutines.*
import org.json.JSONObject
import java.util.*
import javax.net.ssl.SSLException
import kotlin.concurrent.timerTask
import kotlin.math.roundToInt

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun input(
    modifier: Modifier = Modifier,
    label: String,
    inputPrefix: String = "",
    nbOfGenerations: Int = 1,
    verticalScrollState: ScrollState = rememberScrollState(),
    length: Int = Constants.MAX_GENERATION_LENGTH.toInt(),
    showDialog: MutableState<Boolean> = mutableStateOf(false),
    checkIfInputIsEmpty: Boolean = false
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val isGenerateBtnEnabled = remember {
        mutableStateOf(false)
    }

    val connectionError = stringResource(id = R.string.no_connection)

    Column(modifier = modifier) {

        Card(shape = Shapes.medium, border = BorderStroke(3.dp, color = Blue)) {

            Column {

                MyTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 100.dp),
                    onValueChanged = {
                        SettingsNotifier.input.value = TextFieldValue(text = it)
                        isGenerateBtnEnabled.value = it.isNotEmpty()
                    },
                    placeholder = label,
                    value = SettingsNotifier.input.value.text
                )

                /**
                 * input actions
                 **/
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = SpacersSize.small, bottom = SpacersSize.small),
                    horizontalArrangement = Arrangement.End
                ) {

                    IconButton(onClick = {
                        SettingsNotifier.input.value = TextFieldValue(text = "")
                        isGenerateBtnEnabled.value = false
                    }, modifier = Modifier.animateOffsetX(initialOffsetX = 100.dp)) {
                        MyIcon(
                            iconID = R.drawable.clear, tint = Blue, contentDesc = stringResource(
                                id = R.string.clear
                            )
                        )
                    }

                    MySpacer(type = "small", widthOrHeight = "width")

                    IconButton(onClick = {
                        SettingsNotifier.input.value = Helpers.pasteFromClipBoard(
                            mutableStateOf(SettingsNotifier.input.value), context
                        )
                    }, modifier = Modifier.animateOffsetX(initialOffsetX = 100.dp)) {
                        MyIcon(
                            iconID = R.drawable.icon_paste,
                            tint = Blue,
                            contentDesc = stringResource(
                                id = R.string.paste
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(SpacersSize.small))

        /**
         * generate button
         **/
        val generateText = remember {
            mutableStateOf(App.getTextFromString(R.string.generate))
        }
        MyButton(modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
            text = generateText.value,
            isEnabled = isGenerateBtnEnabled.value,
            content = {
                MyAnimatedVisibility(visible = generateText.value == App.getTextFromString(R.string.generated)) {
                    Row {
                        MySpacer(type = "small", widthOrHeight = "width")
                        MyIcon(
                            iconID = R.drawable.icon_checkcircle,
                            tint = Color.White,
                            contentDesc = stringResource(id = R.string.done)
                        )
                    }
                }
            }) {

            if (!SettingsNotifier.isConnected.value) {
                HelperUI.showToast(
                    context,
                    connectionError,
                )
                return@MyButton
            }

            if (checkIfInputIsEmpty) // so if the user coming from custom screen, so there is no input prefix
            // then the model will generate gibberish content
                if (SettingsNotifier.input.value.text.trim().isEmpty()) {
                    HelperUI.showToast(msg = App.getTextFromString(R.string.no_input_entered))
                    return@MyButton
                }

            /**
             * if user is on base plan subscription and nb of words generated is at the max, prevent him from
             * generating extra prompts
             **/
            if (HelperAuth.isSubscribed())
                if (HelperSharedPreference.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_BASE && HelperSharedPreference.getNbOfWordsGenerated() >= Constants.BASE_PLAN_MAX_NB_OF_WORDS) {
                    SettingsNotifier.basePlanMaxNbOfWordsExceeded.value = true
                    return@MyButton
                }

            if (HelperSharedPreference.getNbOfGenerationsConsumed() >= 2 && !HelperAuth.isSubscribed()) { // if nbOfGenerationsConsumed is >= 2
                // and the user is not subscribed, force the user to subscribe
                SettingsNotifier.showDialogNbOfGenerationsLeftExceeded.value = true
            } else {
                keyboardController?.hide()
                isGenerateBtnEnabled.value = false
                showDialog.value = true
                generateText.value = App.getTextFromString(R.string.generating)

                getResponse(
                    inputPrefix + " " + SettingsNotifier.input.value.text.trim(),
                    context,
                    length,
                    nbOfGenerations,
                    isGenerateBtnEnabled,
                    coroutineScope = coroutineScope,
                    onErrorAction = {
                        showDialog.value = false
                        isGenerateBtnEnabled.value = true
                        generateText.value = App.getTextFromString(R.string.generate)

                    }, verticalScrollState = verticalScrollState
                ) { // on fetching response action done
                    showDialog.value = false
                    generateText.value = App.getTextFromString(R.string.generated)

                    if (!HelperAuth.isSubscribed()) { // if the user is not yet subscribed, decrement the nb of generations left
                        SettingsNotifier.nbOfGenerationsConsumed.value += 1
                    }

                    Timer().schedule(timerTask {
                        generateText.value = App.getTextFromString(R.string.generate)
                    }, 1500)
                }
            }
        }
    }
}

private fun getResponse(
    query: String,
    context: Context,
    length: Int,
    nbOfGenerations: Int = 1,
    isGenerateBtnEnabled: MutableState<Boolean>,
    coroutineScope: CoroutineScope,
    verticalScrollState: ScrollState,
    onErrorAction: () -> Unit,
    onDoneAction: () -> Unit
) {
    val url = "https://api.openai.com/v1/completions"
    // setting text on for question on below line.
    // creating a queue for request queue.
    val queue: RequestQueue = Volley.newRequestQueue(context)
    // creating a json object on below line.
    val jsonObject: JSONObject = JSONObject()
    // adding params to json object.
    jsonObject.put("model", "text-davinci-003")
    jsonObject.put("prompt", query)
    jsonObject.put("temperature", 0.9)
    jsonObject.put("max_tokens", length)
    jsonObject.put("top_p", 0.5)
    jsonObject.put("frequency_penalty", 0.0)
    jsonObject.put("presence_penalty", 0.0)
    jsonObject.put("n", nbOfGenerations)
    jsonObject.put("stream", false)
    jsonObject.put("logprobs", null)

    // on below line making json object request.
    val postRequest: JsonObjectRequest =
        // on below line making json object request.
        object : JsonObjectRequest(Method.POST, url, jsonObject, Response.Listener { response ->
            val totalNbOfToken: Int =
                response.getJSONObject("usage").getInt("total_tokens")
            HelperSharedPreference.incrementNbOfGenerationsConsumed()
            HelperSharedPreference.addToNbOfWordsGenerated((totalNbOfToken * 0.75).roundToInt())
            // on below line getting response message and setting it to text view.
            if (nbOfGenerations > 1) { // many output to generate
                SettingsNotifier.outputList.clear() // this to not make the list append entries each time
                for (i in 0 until response.getJSONArray("choices").length()) {
                    val text =
                        response.getJSONArray("choices").getJSONObject(i).getString("text").trim()

                    SettingsNotifier.outputList.add(
                        text
                    )

                    HelperFirebaseDatabase.writeHistoryEntry(
                        type = SettingsNotifier.templateType,
                        input = SettingsNotifier.input.value.text.trim(),
                        text
                    )
                }
                isGenerateBtnEnabled.value = true
            } else { // 1 output to generate
                val responseMsg: String =
                    response.getJSONArray("choices").getJSONObject(0).getString("text").trim()
                coroutineScope.launch(Dispatchers.IO)
                {
                    HelperFirebaseDatabase.writeHistoryEntry(
                        type = SettingsNotifier.templateType,
                        input = SettingsNotifier.input.value.text.trim(),
                        responseMsg
                    )
                    SettingsNotifier.stopTyping.value = false
                    SettingsNotifier.outputList.clear() // this to not make the list append entries each time
                    responseMsg.forEachIndexed { index, c ->
                        if (SettingsNotifier.stopTyping.value) {
                            return@forEachIndexed
                        }
                        SettingsNotifier.output.value =
                            responseMsg.substring(startIndex = 0, endIndex = index + 1)

                        delay(
                            HelperSharedPreference.getFloat(
                                HelperSharedPreference.SP_SETTINGS,
                                HelperSharedPreference.SP_SETTINGS_OUTPUT_TYPEWRITER_LENGTH,
                                50f
                            ).toLong()
                        )

                        // scrolling the textfield output so the user don't need to scroll it manually
                        coroutineScope.launch(Dispatchers.IO) {
                            verticalScrollState.scrollTo(
                                SettingsNotifier.output.value.length + 100,
                            )
                        }
                    }
                    isGenerateBtnEnabled.value = true
                }
            }
            onDoneAction()
        },
            // adding on error listener
            Response.ErrorListener { error ->
                onErrorAction()
                if (error.cause is SSLException) {
                    HelperUI.showToast(msg = App.getTextFromString(textID = R.string.no_connection))
                } else HelperUI.showToast(msg = App.getTextFromString(textID = R.string.something_went_wrong))

            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                // adding headers on below line.
                params["Content-Type"] = "application/json"
                params["Authorization"] =
                    "Bearer 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"
                return params;
            }
        }

    // on below line adding retry policy for our request.
    postRequest.retryPolicy = object : RetryPolicy {
        override fun getCurrentTimeout(): Int {
            return 50000
        }

        override fun getCurrentRetryCount(): Int {
            return 50000
        }

        @Throws(VolleyError::class)
        override fun retry(error: VolleyError) {
        }
    }
    // on below line adding our request to queue.
    queue.add(postRequest)
}