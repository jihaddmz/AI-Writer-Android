package com.appsfourlife.draftogo.components

import android.content.Context
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
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
import com.appsfourlife.draftogo.SettingsNotifier
import com.appsfourlife.draftogo.helpers.*
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.timerTask

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun input(
    modifier: Modifier = Modifier,
    label: String,
    inputPrefix: String = "",
    nbOfGenerations: Int = 1,
    listOfGeneratedTexts: MutableList<String> = mutableListOf(),
    length: Int = Constants.MAX_GENERATION_LENGTH.toInt(),
    showDialog: MutableState<Boolean> = mutableStateOf(false),
): String {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    var input by remember {
        mutableStateOf(TextFieldValue(text = ""))
    }
    val output = remember {
        mutableStateOf("")
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
                        input = TextFieldValue(text = it)
                    },
                    placeholder = label,
                    value = input.text
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
                        input = TextFieldValue(text = "")
                    }) {
                        MyIcon(
                            iconID = R.drawable.clear,
                            tint = Blue,
                            contentDesc = stringResource(
                                id = R.string.clear
                            )
                        )
                    }

                    when (rememberWindowInfo().screenWidthInfo) {
                        is WindowInfo.WindowType.Compact -> Spacer(modifier = Modifier.width(0.dp))
                        is WindowInfo.WindowType.Medium -> Spacer(
                            modifier = Modifier.width(
                                SpacersSize.small
                            )
                        )
                        else -> Spacer(modifier = Modifier.width(SpacersSize.medium))
                    }

                    IconButton(onClick = {
                        input = Helpers.pasteFromClipBoard(mutableStateOf(input), context)
                    }) {
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
        MyButton(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            text = generateText.value,
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
            }
        ) {

            if (HelperSharedPreference.getInt(
                    HelperSharedPreference.SP_SETTINGS,
                    HelperSharedPreference.SP_SETTINGS_NB_OF_GENERATIONS_LEFT,
                    Constants.MAX_NB_OF_TRIES_ALLOWED,
                    context = context
                ) == 0 && !HelperAuth.getUserSubscriptionState()
            ) { // if the nb of generations left is 0, make the user to subscribe
                SettingsNotifier.showDialogNbOfGenerationsLeftExceeded.value = true
            } else {
                keyboardController?.hide()
                Helpers.checkForConnection(ifConnected = {
                    showDialog.value = true
                    generateText.value = App.getTextFromString(R.string.generating)

                    getResponse(
                        inputPrefix + " " + input.text.trim(),
                        context,
                        length,
                        nbOfGenerations,
                        listOfGeneratedTexts,
                        output
                    ) { // on fetching response action done
                        showDialog.value = false
                        generateText.value = App.getTextFromString(R.string.generated)

                        if (!HelperAuth.getUserSubscriptionState()) { // if the user is not yet subscribed, decrement the nb of generations left
                            SettingsNotifier.nbOfGenerationsLeft.value -= 1
                            HelperSharedPreference.setInt(
                                HelperSharedPreference.SP_SETTINGS,
                                HelperSharedPreference.SP_SETTINGS_NB_OF_GENERATIONS_LEFT,
                                SettingsNotifier.nbOfGenerationsLeft.value,
                                context
                            )
                        }

                        Timer().schedule(timerTask {
                            generateText.value = App.getTextFromString(R.string.generate)
                        }, 1500)
                    }
                }, notConnected = {
                    runBlocking(Dispatchers.Main) {
                        HelperUI.showToast(
                            context,
                            connectionError,
                        )
                    }
                }, coroutineScope = coroutineScope)
            }
        }
    }

    return output.value
}

private fun getResponse(
    query: String,
    context: Context,
    length: Int,
    nbOfGenerations: Int = 1,
    listOfGeneratedTexts: MutableList<String> = mutableListOf(),
    output: MutableState<String>,
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

    // on below line making json object request.
    val postRequest: JsonObjectRequest =
        // on below line making json object request.
        object : JsonObjectRequest(Method.POST, url, jsonObject,
            Response.Listener { response ->
                // on below line getting response message and setting it to text view.
                if (nbOfGenerations > 1) {
                    listOfGeneratedTexts.clear() // this to not make the list append entries each time
                    for (i in 0 until response.getJSONArray("choices").length()) {
                        listOfGeneratedTexts.add(
                            response.getJSONArray("choices").getJSONObject(i).getString("text")
                        )
                    }
                } else {
                    val responseMsg: String =
                        response.getJSONArray("choices").getJSONObject(0).getString("text")
                    output.value = responseMsg
                }
                onDoneAction()
            },
            // adding on error listener
            Response.ErrorListener { error ->
                Log.e("TAGAPI", "Error is : " + error.message + "\n" + error)
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