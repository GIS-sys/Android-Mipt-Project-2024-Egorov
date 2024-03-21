package com.giswarm.mipt_2024.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.giswarm.mipt_2024.R

class MainFragment : Fragment(R.layout.fragment_main) {
    companion object {
        private const val KEY_FRAGMENT_INDEX: String = "KEY_FRAGMENT_INDEX"
    }

    private var fragmentIndex: Long = -1
    private lateinit var buttonSwitchInnerFragment: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DEBUGMAIN", "onViewCreated")
        if (savedInstanceState != null) {
            Log.d("DEBUGMAIN", savedInstanceState.getLong(KEY_FRAGMENT_INDEX).toString())
        }
        buttonSwitchInnerFragment = view.findViewById<Button>(R.id.main_btn_switch_inner_fragment)
        cycleInnerFragment()
        buttonSwitchInnerFragment.setOnClickListener {
            cycleInnerFragment()
        }
        view.findViewById<Button>(R.id.main_btn_go_to_settings).setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.root_fragment_container_view, SettingsFragment())
                addToBackStack(null)
            }
        }
        view.findViewById<Button>(R.id.main_btn_go_to_credentials).setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.root_fragment_container_view, CredentialsFragment())
                addToBackStack(null)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("DEBUGMAIN", "onSaveInstanceState")
        outState.putLong(KEY_FRAGMENT_INDEX, fragmentIndex)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("DEBUGMAIN", "onViewStateRestored")
        if (savedInstanceState != null) {
            Log.d("DEBUGMAIN", savedInstanceState.getLong(KEY_FRAGMENT_INDEX).toString())
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