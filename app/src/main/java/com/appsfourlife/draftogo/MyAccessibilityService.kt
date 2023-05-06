package com.appsfourlife.draftogo

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.os.Bundle
import android.text.Html
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.appsfourlife.draftogo.helpers.Constants
import com.appsfourlife.draftogo.helpers.HelperAuth
import com.appsfourlife.draftogo.helpers.HelperChatGPT
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.util.SettingsNotifier
import java.util.*
import kotlin.concurrent.timerTask


class MyAccessibilityService : AccessibilityService() {

    private val secondsToStartGenerating = 3
    private val textInProgress = App.getTextFromString(R.string.generating)

    private var nodeInfo: AccessibilityNodeInfo? = null
    private var seconds: Int = 0
    var startRecording = false

    init {
        Timer().scheduleAtFixedRate(timerTask {
            if (seconds > secondsToStartGenerating) { // if user has stopped typing more than the number of defined seconds, start generating
                if (HelperAuth.isSubscribed()) {
                    if (HelperSharedPreference.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_BASE) {
                        if (HelperSharedPreference.getNbOfWordsGenerated() < Constants.BASE_PLAN_MAX_NB_OF_WORDS) {
                            generate()
                        } else {
                            startRecording = false
                            seconds = 0

                            val arguments = Bundle()
                            arguments.putString(
                                AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                                Html.fromHtml(App.getTextFromString(R.string.base_plan_nb_of_words_exceeded), 0)
                                    .toString()

                            )
                            nodeInfo!!.performAction(
                                AccessibilityNodeInfoCompat.ACTION_SET_TEXT,
                                arguments
                            )
                        }
                    } else {
                        generate()
                    }
                } else {
                    if (HelperSharedPreference.getNbOfGenerationsConsumed() <= Constants.NB_OF_MAX_ALLOWED_GENERATIONS) {
                        generate()
                    } else {
                        startRecording = false
                        seconds = 0

                        val arguments = Bundle()
                        arguments.putString(
                            AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                            App.getTextFromString(R.string.nb_of_text_generations_exceeded_anywhere)

                        )
                        nodeInfo!!.performAction(
                            AccessibilityNodeInfoCompat.ACTION_SET_TEXT,
                            arguments
                        )
                    }
                }
            }

            if (startRecording)
                seconds += 1
        }, 0, 1000)
    }

    private fun generate() {
        nodeInfo?.let { node ->

            startRecording = false
            seconds = 0

            val arguments = Bundle()
            arguments.putString(
                AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                textInProgress
            )
            node.performAction(
                AccessibilityNodeInfoCompat.ACTION_SET_TEXT,
                arguments
            )

            SettingsNotifier.input.value = TextFieldValue(node.text.split("/draft")[1].trim())
            SettingsNotifier.templateType = App.getTextFromString(R.string.written_anywhere)
            HelperChatGPT.getResponse1(
                node.text.split("/draft")[1].trim(),
                onErrorAction = {},
                onDoneAction = {
                    arguments.putString(
                        AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                        it
                    )
                    node.performAction(
                        AccessibilityNodeInfoCompat.ACTION_SET_TEXT,
                        arguments
                    )
                })
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            if (event.eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
                if (startRecording) // if user is still typing don't start the timer
                    seconds = 0

                event.beforeText?.let { it1 ->
                    if (it1.contains("/draft")) {
                        startRecording = true
                        event.source?.let { nodeInfo ->
                            this.nodeInfo = nodeInfo
                        }
                    } else {
                        nodeInfo = null
                    }
                }
            }
        }
    }

    override fun onInterrupt() {
        TODO("Not yet implemented")
    }

    override fun onServiceConnected() {
        serviceInfo.apply {
            // Set the type of events that this service wants to listen to. Others
            // won't be passed to this service.
            eventTypes =
                AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED

            // If you only want this service to work with specific applications, set their
            // package names here. Otherwise, when the service is activated, it will listen
            // to events from all applications.
//            packageNames = arrayOf("com.example.android.myFirstApp", "com.example.android.mySecondApp")

            // Set the type of feedback your service will provide.
            feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL

            // Default services are invoked only if no package-specific ones are present
            // for the type of AccessibilityEvent generated. This service *is*
            // application-specific, so the flag isn't necessary. If this was a
            // general-purpose service, it would be worth considering setting the
            // DEFAULT flag.

//             flags = AccessibilityServiceInfo.DEFAULT;

//            notificationTimeout = 100
        }
    }
}