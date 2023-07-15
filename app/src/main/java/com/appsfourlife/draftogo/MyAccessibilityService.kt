package com.appsfourlife.draftogo

import android.accessibilityservice.AccessibilityService
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.appsfourlife.draftogo.helpers.HelperChatGPT
import com.appsfourlife.draftogo.util.SettingsNotifier
import java.util.Timer
import kotlin.concurrent.timerTask


class MyAccessibilityService : AccessibilityService() {

    private val secondsToStartGenerating = 3
    private val textInProgress = App.getTextFromString(R.string.generating)

    private var nodeInfo: AccessibilityNodeInfo? = null
    private var seconds: Int = 0
    private var startRecording = false
    private var prefixWord = App.getTextFromString(R.string.draft)

    init {
        Timer().scheduleAtFixedRate(timerTask {
            if (seconds > secondsToStartGenerating) { // if user has stopped typing more than the number of defined seconds, start generating
//                if (HelperAuth.isSubscribed()) {
//                    if (HelperSharedPreference.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_BASE) {
//                        if (HelperSharedPreference.getNbOfWordsGenerated() < Constants.BASE_PLAN_MAX_NB_OF_WORDS) {
//                            generate()
//                        } else {
//                            startRecording = false
//                            seconds = 0
//
//                            val arguments = Bundle()
//                            arguments.putString(
//                                AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
//                                App.getTextFromString(R.string.base_plan_nb_of_words_exceeded)
//                            )
//                            nodeInfo!!.performAction(
//                                AccessibilityNodeInfoCompat.ACTION_SET_TEXT,
//                                arguments
//                            )
//                        }
//                    } else {
                        generate()
//                    }
//                } else {
//                    if (HelperSharedPreference.getNbOfGenerationsConsumed() < Constants.NB_OF_MAX_ALLOWED_GENERATIONS) {
//                        generate()
//                    } else {
//                        startRecording = false
//                        seconds = 0
//
//                        val arguments = Bundle()
//                        arguments.putString(
//                            AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
//                            App.getTextFromString(R.string.nb_of_text_generations_exceeded_anywhere)
//
//                        )
//                        nodeInfo!!.performAction(
//                            AccessibilityNodeInfoCompat.ACTION_SET_TEXT,
//                            arguments
//                        )
//                    }
//                }
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
            if (SettingsNotifier.isConnected.value) {
                arguments.putString(
                    AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                    textInProgress
                )
                node.performAction(
                    AccessibilityNodeInfoCompat.ACTION_SET_TEXT,
                    arguments
                )

                SettingsNotifier.input.value = TextFieldValue(node.text.split(prefixWord)[1].trim())
                SettingsNotifier.templateType = App.getTextFromString(R.string.written_anywhere)
                HelperChatGPT.getResponse1(
                    node.text.split(prefixWord)[1].trim(),
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
            } else {
                arguments.putString(
                    AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                    App.getTextFromString(R.string.no_connection)
                )
                node.performAction(
                    AccessibilityNodeInfoCompat.ACTION_SET_TEXT,
                    arguments
                )
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (startRecording) { // if user is still typing don't start the timer
            seconds = 0
        }

        if (event!!.packageName == "com.google.android.gm") {
            event.source?.let { it1 ->
                nodeInfo = it1
                it1.text?.let {
                    if (it.contains(prefixWord)) {
                        startRecording = true
                    } else {
                        nodeInfo = null
                    }
                }
            }
        } else {
            event.beforeText?.let { it1 ->
                if (it1.contains(prefixWord)) {
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

    override fun onInterrupt() {
    }
}