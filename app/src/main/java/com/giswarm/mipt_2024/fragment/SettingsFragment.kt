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
import com.giswarm.mipt_2024.recycler.RecyclerItemTextImageDelegateAdapter
import com.giswarm.mipt_2024.recycler.RecyclerItemTextImage
import com.giswarm.mipt_2024.recycler.ViewType

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    /*data class ItemsViewModel(val image: Int, val text: String) {}

    class CustomAdapter(private val mList: List<ItemsViewModel>, private val onClickListener: ((Int) -> Unit), var selectedPos: Int = 0) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_element, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val itemsViewModel = mList[position]
            holder.imageView.setImageResource(itemsViewModel.image)
            holder.textView.text = itemsViewModel.text
            holder.itemView.setOnClickListener{
                val lastSelected = selectedPos
                selectedPos = position
                onClickListener.invoke(position)
                notifyItemChanged(lastSelected)
                notifyItemChanged(position)
            }
            holder.itemView.setBackgroundColor(if (selectedPos == position) (0xff000000).toInt() else (0xff99cc11u).toInt())
        }

        override fun getItemCount(): Int {
            return mList.size
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView.findViewById(R.id.imageview)
            val textView: TextView = itemView.findViewById(R.id.textView)
        }
    }*/

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
            object : RecyclerItemTextImageDelegateAdapter.OnViewSelectedListener {
                override fun onItemSelected(item: RecyclerItemTextImage) {
                    when (item) {
                        is RecyclerItemTextImage -> Log.d("DEBUG_1604", item.text)
                        else -> Log.d("DEBUG_1604", "item")
                    }
                }
            },
            imageShapeRecyclerView,
            requireActivity(),
            listOf(RecyclerItemCircle(requireContext()), RecyclerItemSquare(requireContext())))
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