package com.giswarm.mipt_2024.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giswarm.mipt_2024.R
import com.giswarm.mipt_2024.recycler.MoonShapeAdapter
import com.giswarm.mipt_2024.recycler.RecyclerItemCircle
import com.giswarm.mipt_2024.recycler.RecyclerItemSquare
import com.giswarm.mipt_2024.recycler.RecyclerItemText
import com.giswarm.mipt_2024.recycler.RecyclerItemTextImageDelegateAdapter
import com.giswarm.mipt_2024.recycler.RecyclerItemTextImage
import com.giswarm.mipt_2024.recycler.ViewType
import com.giswarm.mipt_2024.recycler.ViewTypeDelegateAdapter

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    companion object {
        private const val KEY_LABEL_SWITCH: String = "KEY_LABEL_SWITCH"
        private const val KEY_SHAPE_SPINNER: String = "KEY_SHAPE_SPINNER"
    }

    private lateinit var imageLabelSwitch: SwitchCompat
    private lateinit var imageShapeRecyclerView: RecyclerView
    private lateinit var imageShapeRecyclerViewAdapter: MoonShapeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageLabelSwitch = view.findViewById(R.id.image_label_switch)

        imageShapeRecyclerView = view.findViewById(R.id.image_shape_recycler_view)
        imageShapeRecyclerView.layoutManager = LinearLayoutManager(activity)
        imageShapeRecyclerViewAdapter = MoonShapeAdapter(
            object : ViewTypeDelegateAdapter.OnViewSelectedListener {
                override fun onItemSelected(item: ViewType) {
                    when (item) {
                        is RecyclerItemTextImage -> Log.d("DEBUG_1604textimage", item.text)
                        is RecyclerItemText -> Log.d("DEBUG_1604text", item.text)
                        else -> Log.d("DEBUG_1604", "item")
                    }
                }
            },
            imageShapeRecyclerView,
            requireActivity(),
            listOf(RecyclerItemText(getString(R.string.only_text)), RecyclerItemCircle(requireContext()), RecyclerItemSquare(requireContext())))
        imageShapeRecyclerView.adapter = imageShapeRecyclerViewAdapter

        Log.d("DEBUG_SAVEINSTANCE", "onViewCreated")
        if (savedInstanceState != null) {
            imageLabelSwitch.isChecked = savedInstanceState.getBoolean(KEY_LABEL_SWITCH)
            //imageShapeRecyclerViewAdapter.selectedPos = savedInstanceState.getInt(KEY_SHAPE_SPINNER)
        }

        view.findViewById<Button>(R.id.settings_btn_save).setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("DEBUG_SAVEINSTANCE2", imageLabelSwitch.isChecked.toString())
        outState.putBoolean(KEY_LABEL_SWITCH, imageLabelSwitch.isChecked)
        //outState.putInt(KEY_SHAPE_SPINNER, imageShapeRecyclerViewAdapter.selectedPos)
    }
}