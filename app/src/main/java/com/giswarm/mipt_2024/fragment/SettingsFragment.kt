package com.giswarm.mipt_2024.fragment

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.giswarm.mipt_2024.R
import com.giswarm.mipt_2024.recycler.MoonShapeAdapter
import com.giswarm.mipt_2024.recycler.MoonShapeDelegateAdapter
import com.giswarm.mipt_2024.recycler.MoonShapeItem

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

        val data = ArrayList<MoonShapeItem>()
        for (i in 1..20) {
            /*data.add(MoonShapeItem(
                "Item " + i,
                Glide.with(requireActivity()).asDrawable()
                    .placeholder(R.drawable.mipt_android_icon)
                    .error(R.drawable.mipt_android_icon)
                    .load("https://goo.gl/gEgYUd")
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: Target<Drawable>?, p3: Boolean): Boolean {
                            Log.e("DEBUG_1704", "onLoadFailed")
                            //do something if error loading
                            return false
                        }
                        override fun onResourceReady(p0: Drawable?, p1: Any?, p2: Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                            Log.d("DEBUG_1704", "OnResourceReady")
                            //do something when picture already loaded
                            return false
                        }
                    }).
            ))*/
        }

        imageShapeRecyclerView = view.findViewById(R.id.image_shape_recycler_view)
        imageShapeRecyclerView.layoutManager = LinearLayoutManager(activity)
        imageShapeRecyclerViewAdapter = MoonShapeAdapter(object : MoonShapeDelegateAdapter.OnViewSelectedListener {
            override fun onItemSelected(item: MoonShapeItem) {
                Log.d("DEBUG_1604", item.text)
            }
        }, imageShapeRecyclerView, requireActivity())
        imageShapeRecyclerViewAdapter.add(data)
        imageShapeRecyclerView.adapter = imageShapeRecyclerViewAdapter

        for (i in 1..20) {
            Glide.with(requireActivity()).asDrawable()
                .placeholder(R.drawable.mipt_android_icon)
                .error(R.drawable.mipt_android_icon)
                .load("https://goo.gl/gEgYUd")
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: Boolean): Boolean {
                        Log.e("DEBUG_1704", "onLoadFailed")
                        //do something if error loading
                        return true
                    }
                    override fun onResourceReady(p0: Drawable?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                        Log.d("DEBUG_1704", "OnResourceReady")
                        //do something when picture already loaded
                        imageShapeRecyclerViewAdapter.add(listOf(MoonShapeItem("Item " + i, p0!!)))
                        return true
                    }
                }).submit()
        }

        Log.d("DEBUG_SAVEINSTANCE", "onViewCreated")
        if (savedInstanceState != null) {
            Log.d("DEBUG_SAVEINSTANCE", savedInstanceState.getBoolean(KEY_LABEL_SWITCH).toString())
            Log.d("DEBUG_SAVEINSTANCE", savedInstanceState.getInt(KEY_SHAPE_SPINNER).toString())
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
        //Log.d("DEBUG_SAVEINSTANCE2", imageShapeRecyclerViewAdapter.selectedPos.toString())
        outState.putBoolean(KEY_LABEL_SWITCH, imageLabelSwitch.isChecked)
        //outState.putInt(KEY_SHAPE_SPINNER, imageShapeRecyclerViewAdapter.selectedPos)
    }
}