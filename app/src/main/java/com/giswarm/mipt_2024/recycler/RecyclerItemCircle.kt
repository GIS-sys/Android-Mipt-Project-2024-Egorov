package com.giswarm.mipt_2024.recycler

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.giswarm.mipt_2024.R


class RecyclerItemCircle(context: Context) : RecyclerItemTextImage("Circle", getDrawable(context, R.drawable.circle)!!) {
}