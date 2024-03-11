package com.giswarm.mipt_2024.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.giswarm.mipt_2024.R

class GreetingsFragment : Fragment(R.layout.fragment_greetings) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.greetings_btn_go_to_main).setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.root_fragment_container_view, MainFragment())
            }
        }
    }
}