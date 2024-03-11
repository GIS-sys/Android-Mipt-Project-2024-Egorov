package com.giswarm.mipt_2024

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

class MainFragment : Fragment(R.layout.fragment_main) {
    private var fragmentIndex: Long = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container_view_main, TextViewFragment())
        }

        view.findViewById<Button>(R.id.btn_switch_inner_fragment).setOnClickListener {
            fragmentIndex = (fragmentIndex + 1) % 2
            if (fragmentIndex == 0.toLong()) {
                childFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.fragment_container_view_main, TextViewFragment())
                }
            }
            if (fragmentIndex == 1.toLong()) {
                childFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.fragment_container_view_main, VisualViewFragment())
                }
            }
        }
    }
}