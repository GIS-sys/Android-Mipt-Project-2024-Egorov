package com.giswarm.mipt_2024.recycler

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.giswarm.mipt_2024.R


class RecyclerItemSquare(context: Context) : RecyclerItemTextImage("Square", getDrawable(context, R.drawable.square)!!) {
}