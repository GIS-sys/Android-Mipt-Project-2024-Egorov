package com.giswarm.mipt_2024.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.giswarm.mipt_2024.R

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.settings_btn_save).setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}