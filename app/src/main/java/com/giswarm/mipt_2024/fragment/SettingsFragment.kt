package com.giswarm.mipt_2024.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.giswarm.mipt_2024.R

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    companion object {
        private const val KEY_LABEL_SWITCH: String = "KEY_LABEL_SWITCH"
        private const val KEY_SHAPE_SPINNER: String = "KEY_SHAPE_SPINNER"
    }

    private lateinit var imageLabelSwitch: SwitchCompat
    private lateinit var imageShapeSpinner: Spinner

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageLabelSwitch = view.findViewById(R.id.image_label_switch)
        imageShapeSpinner = view.findViewById(R.id.image_shape_spinner)

        Log.d("DEBUG_SAVEINSTANCE", "onViewCreated")
        if (savedInstanceState != null) {
            Log.d("DEBUG_SAVEINSTANCE", savedInstanceState.getBoolean(KEY_LABEL_SWITCH).toString())
            Log.d("DEBUG_SAVEINSTANCE", savedInstanceState.getInt(KEY_SHAPE_SPINNER).toString())
            imageLabelSwitch.isChecked = savedInstanceState.getBoolean(KEY_LABEL_SWITCH)
            imageShapeSpinner.setSelection(savedInstanceState.getInt(KEY_SHAPE_SPINNER))
        }

        view.findViewById<Button>(R.id.settings_btn_save).setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("DEBUG_SAVEINSTANCE2", imageLabelSwitch.isChecked.toString())
        Log.d("DEBUG_SAVEINSTANCE2", imageShapeSpinner.selectedItemPosition.toString())
        outState.putBoolean(KEY_LABEL_SWITCH, imageLabelSwitch.isChecked)
        outState.putInt(KEY_SHAPE_SPINNER, imageShapeSpinner.selectedItemPosition)
    }
}