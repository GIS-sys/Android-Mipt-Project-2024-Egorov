package com.giswarm.mipt_2024

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

class GreetingsFragment : Fragment(R.layout.fragment_greetings) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<Button>(R.id.btn_go_to_main)

        button.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_root_container_view, MainFragment())
                addToBackStack(null)
            }
        }
    }
}