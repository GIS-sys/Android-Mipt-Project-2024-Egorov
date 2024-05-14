package com.giswarm.mipt_2024.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.giswarm.mipt_2024.IntentProcessor
import com.giswarm.mipt_2024.IntentTargets
import com.giswarm.mipt_2024.MainActivity
import com.giswarm.mipt_2024.R
import com.giswarm.mipt_2024.position.DevicePositionManager
import com.giswarm.mipt_2024.position.GpsPositionManager
import kotlin.concurrent.timer

const val PREFERENCES_NAME = "PREFERENCES_NAME"
const val PREFERENCES_ALREADY_SHOWN = "GREETINGS_ALREADY_SHOWN"

class GreetingsFragment : Fragment(R.layout.fragment_greetings) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (IntentProcessor.actionsTransitionsList.isNotEmpty() && IntentProcessor.actionsTransitionsList.first() == IntentTargets.MAIN) {
            IntentProcessor.actionsTransitionsList.removeFirst()
            moveToMain()
        }
        if (requireActivity().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getBoolean(PREFERENCES_ALREADY_SHOWN, false)
        ) {
            moveToMain()
        }
        view.findViewById<Button>(R.id.greetings_btn_go_to_main).setOnClickListener {
            moveToMain()
        }
    }

    private fun moveToMain() {
        requireActivity().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
            .putBoolean(PREFERENCES_ALREADY_SHOWN, true).apply()
        (activity as MainActivity).moveToMain()
    }
}