package com.giswarm.mipt_2024.recycler

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.giswarm.mipt_2024.R


class RecyclerItemCircle(context: Context) : RecyclerItemTextImage(context.getString(R.string.circle), getDrawable(context, R.drawable.circle)!!) {
}