package com.giswarm.mipt_2024.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.giswarm.mipt_2024.R

class MainFragment : Fragment(R.layout.fragment_main) {
    private var fragmentIndex: Long = -1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cycleInnerFragment()
        view.findViewById<Button>(R.id.main_btn_switch_inner_fragment).setOnClickListener {
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

    private fun cycleInnerFragment() {
        fragmentIndex = (fragmentIndex + 1) % 2
        childFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.main_fragment_container_view,
                when (fragmentIndex) {
                    0.toLong() -> TextViewFragment()
                    1.toLong() -> VisualViewFragment()
                    else -> TODO()
                })
        }
    }
}