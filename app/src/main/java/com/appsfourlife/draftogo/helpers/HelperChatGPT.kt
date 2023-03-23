package com.appsfourlife.draftogo.helpers

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.MutableState
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.util.SettingsNotifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.net.ssl.SSLException
import kotlin.math.roundToInt

object HelperChatGPT {

    fun getResponse(
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
            object : JsonObjectRequest(
                Method.POST, url, jsonObject, Response.Listener { response ->
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
}