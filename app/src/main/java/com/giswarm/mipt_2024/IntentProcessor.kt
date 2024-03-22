package com.giswarm.mipt_2024

import android.content.Intent
import android.util.Log

object IntentProcessor {
    val actionsTransitionsList = ArrayList<String>()

    fun process(intent: Intent) {
        if (intent.data?.scheme.isNullOrBlank() && intent.data?.host.isNullOrBlank()) {
            return
        }
        if (intent.data?.scheme == "com.giswarm.mipt_2024") {
            when (intent.data?.host) {
                "main.text" -> {
                    actionsTransitionsList.add("main")
                    actionsTransitionsList.add("text")
                }
                "main.visual" -> {
                    actionsTransitionsList.add("main")
                    actionsTransitionsList.add("visual")
                }
                "credentials" -> actionsTransitionsList.add("credentials")
                "configuration" -> actionsTransitionsList.add("configuration")
            }
        }
    }
}