package com.giswarm.mipt_2024.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.giswarm.mipt_2024.IntentProcessor
import com.giswarm.mipt_2024.R
import com.giswarm.mipt_2024.position.DevicePositionManager
import com.giswarm.mipt_2024.position.GpsPositionManager
import kotlin.concurrent.timer

class GreetingsFragment : Fragment(R.layout.fragment_greetings) {
    companion object {
        private const val PREFERENCES_ALREADY_SHOWN = "GREETINGS_ALREADY_SHOWN"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (IntentProcessor.actionsTransitionsList.isNotEmpty() && IntentProcessor.actionsTransitionsList[0] == "main") {
            IntentProcessor.actionsTransitionsList.removeAt(0)
            moveToMain()
        }
        if (requireActivity().getSharedPreferences("PREFERENCE_NAME",  Context.MODE_PRIVATE).getBoolean(PREFERENCES_ALREADY_SHOWN, false)) {
            moveToMain()
        }
        view.findViewById<Button>(R.id.greetings_btn_go_to_main).setOnClickListener {
            moveToMain()
        }
    }

    private fun moveToMain() {
        requireActivity().getSharedPreferences("PREFERENCE_NAME",  Context.MODE_PRIVATE).edit().putBoolean(PREFERENCES_ALREADY_SHOWN, true).commit()
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.root_fragment_container_view, MainFragment())
        }
    }
}