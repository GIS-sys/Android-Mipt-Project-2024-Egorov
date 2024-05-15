package com.giswarm.mipt_2024.fragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giswarm.mipt_2024.storage.DrawableManager
import com.giswarm.mipt_2024.R
import com.giswarm.mipt_2024.recycler.MoonShapeAdapter
import com.giswarm.mipt_2024.recycler.RecyclerItemCircle
import com.giswarm.mipt_2024.recycler.RecyclerItemSquare
import com.giswarm.mipt_2024.recycler.RecyclerItemText
import com.giswarm.mipt_2024.recycler.RecyclerItemTextImage
import com.giswarm.mipt_2024.recycler.ViewType
import com.giswarm.mipt_2024.recycler.ViewTypeDelegateAdapter
import com.giswarm.mipt_2024.repository.SettingsRepository
import com.giswarm.mipt_2024.storage.Consts.SHARERD_PRERFERENCES_MOON_SHOW_TEXT
import com.giswarm.mipt_2024.storage.Consts.SHARERD_PRERFERENCES_SETTINGS

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    companion object {
        private const val KEY_LABEL_SWITCH: String = "KEY_LABEL_SWITCH"
        private const val KEY_SHAPE_SPINNER: String = "KEY_SHAPE_SPINNER"
    }

    private lateinit var imageLabelSwitch: SwitchCompat
    private lateinit var imageShapeRecyclerView: RecyclerView
    private lateinit var imageShapeRecyclerViewAdapter: MoonShapeAdapter

    private var selectedImage: Drawable? = null
    private val settingsRepository = SettingsRepository(requireActivity())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageLabelSwitch = view.findViewById(R.id.image_label_switch)

        imageShapeRecyclerView = view.findViewById(R.id.image_shape_recycler_view)
        imageShapeRecyclerView.layoutManager = LinearLayoutManager(activity)
        imageShapeRecyclerViewAdapter = MoonShapeAdapter(
            object : ViewTypeDelegateAdapter.OnViewSelectedListener {
                override fun onItemSelected(item: ViewType, position: Int) {
                    when (item) {
                        is RecyclerItemTextImage -> selectedImage = item.image
                        is RecyclerItemText -> selectedImage = null
                        else -> Log.d("DEBUG_1604", "loading")
                    }
                }

                override fun onItemDeleted(item: ViewType) {
                    TODO("Not yet implemented")
                }
            },
            imageShapeRecyclerView,
            requireActivity(),
            listOf(
                RecyclerItemText(getString(R.string.only_text)),
                RecyclerItemCircle(requireContext()),
                RecyclerItemSquare(requireContext())
            )
        )
        imageShapeRecyclerView.adapter = imageShapeRecyclerViewAdapter

        if (savedInstanceState != null) {
            imageLabelSwitch.isChecked = savedInstanceState.getBoolean(KEY_LABEL_SWITCH)
            imageShapeRecyclerViewAdapter.selectedPosition =
                savedInstanceState.getInt(KEY_SHAPE_SPINNER)
        }

        view.findViewById<Button>(R.id.settings_btn_save).setOnClickListener {
            Toast.makeText(context, getString(R.string.saved), Toast.LENGTH_SHORT).show()
            settingsRepository.setMoonShape(selectedImage)
            settingsRepository.setShowText(imageLabelSwitch.isChecked)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_LABEL_SWITCH, imageLabelSwitch.isChecked)
        outState.putInt(KEY_SHAPE_SPINNER, imageShapeRecyclerViewAdapter.selectedPosition)
    }
}