package com.giswarm.mipt_2024.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.giswarm.mipt_2024.IntentProcessor
import com.giswarm.mipt_2024.R

class MainFragment : Fragment(R.layout.fragment_main) {
    companion object {
        private const val KEY_FRAGMENT_INDEX: String = "KEY_FRAGMENT_INDEX"
    }

    private var fragmentIndex: Long = -1
    private lateinit var buttonSwitchInnerFragment: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (IntentProcessor.actionsTransitionsList.isNotEmpty()) {
            when (IntentProcessor.actionsTransitionsList[0]) {
                "text" -> {
                    IntentProcessor.actionsTransitionsList.removeAt(0)
                    fragmentIndex = 0
                }
                "visual" -> {
                    IntentProcessor.actionsTransitionsList.removeAt(0)
                    fragmentIndex = 1
                }
                "settings" -> {
                    IntentProcessor.actionsTransitionsList.removeAt(0)
                    moveToSettings()
                }
                "credentials" -> {
                    IntentProcessor.actionsTransitionsList.removeAt(0)
                    moveToCredentials()
                }
            }
        }
        // load state
        if (savedInstanceState != null) {
            fragmentIndex = savedInstanceState.getLong(KEY_FRAGMENT_INDEX)
        }
        if (fragmentIndex != (-1).toLong()) {
            fragmentIndex -= 1
        }
        // show fragment and register next changes
        buttonSwitchInnerFragment = view.findViewById<Button>(R.id.main_btn_switch_inner_fragment)
        cycleInnerFragment()
        buttonSwitchInnerFragment.setOnClickListener {
            cycleInnerFragment()
        }
        // add onclicks
        view.findViewById<Button>(R.id.main_btn_go_to_settings).setOnClickListener {
            moveToSettings()
        }
        view.findViewById<Button>(R.id.main_btn_go_to_credentials).setOnClickListener {
            moveToCredentials()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(KEY_FRAGMENT_INDEX, fragmentIndex)
    }

    private fun moveToSettings() {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.root_fragment_container_view, SettingsFragment())
            addToBackStack(null)
        }
    }

    private fun moveToCredentials() {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.root_fragment_container_view, CredentialsFragment())
            addToBackStack(null)
        }
    }

    private fun cycleInnerFragment() {
        fragmentIndex = (fragmentIndex + 1) % 2
        buttonSwitchInnerFragment.text = when(fragmentIndex) {
            0.toLong() -> getString(R.string.change_view_image)
            1.toLong() -> getString(R.string.change_view_text)
            else -> TODO()
        }
        childFragmentManager.commit {
            setReorderingAllowed(true)
            replace(
                R.id.main_fragment_container_view,
                when (fragmentIndex) {
                    0.toLong() -> TextViewFragment()
                    1.toLong() -> VisualViewFragment()
                    else -> TODO()
                }
            )
        }
    }
}