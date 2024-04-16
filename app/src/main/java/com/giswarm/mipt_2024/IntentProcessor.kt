package com.giswarm.mipt_2024

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.getString

enum class IntentTargets {
    MAIN, TEXT, VISUAL, CREDENTIALS, SETTINGS
}

object IntentProcessor {
    val actionsTransitionsList = ArrayDeque<IntentTargets>()

    fun process(context: Context, intent: Intent) {
        if (intent.data?.scheme.isNullOrBlank() && intent.data?.host.isNullOrBlank()) {
            return
        }
        if (intent.data?.scheme ==  getString(context, R.string.scheme)) {
            when (intent.data?.host) {
                getString(context, R.string.intent_main_text) -> {
                    actionsTransitionsList.addLast(IntentTargets.MAIN)
                    actionsTransitionsList.addLast(IntentTargets.TEXT)
                }
                getString(context, R.string.intent_main_visual) -> {
                    actionsTransitionsList.addLast(IntentTargets.MAIN)
                    actionsTransitionsList.addLast(IntentTargets.VISUAL)
                }
                getString(context, R.string.intent_credentials) -> {
                    actionsTransitionsList.addLast(IntentTargets.MAIN)
                    actionsTransitionsList.addLast(IntentTargets.CREDENTIALS)
                }
                getString(context, R.string.intent_settings)-> {
                    actionsTransitionsList.addLast(IntentTargets.MAIN)
                    actionsTransitionsList.addLast(IntentTargets.SETTINGS)
                }
            }
        }
    }
}