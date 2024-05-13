package com.giswarm.mipt_2024.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giswarm.mipt_2024.R
import com.giswarm.mipt_2024.recycler.MoonShapeAdapter
import com.giswarm.mipt_2024.recycler.PaymentTypeAdapter
import com.giswarm.mipt_2024.recycler.RecyclerItemCircle
import com.giswarm.mipt_2024.recycler.RecyclerItemInputDelete
import com.giswarm.mipt_2024.recycler.RecyclerItemSquare
import com.giswarm.mipt_2024.recycler.RecyclerItemText
import com.giswarm.mipt_2024.recycler.RecyclerItemTextImage
import com.giswarm.mipt_2024.recycler.ViewType
import com.giswarm.mipt_2024.recycler.ViewTypeDelegateAdapter
import com.giswarm.mipt_2024.storage.Consts
import com.giswarm.mipt_2024.storage.DrawableManager

class CredentialsFragment : Fragment(R.layout.fragment_credentials) {
    private lateinit var paymentTypeRecyclerView: RecyclerView
    private lateinit var paymentTypeRecyclerViewAdapter: PaymentTypeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paymentTypeRecyclerView = view.findViewById(R.id.cred_payment_recycler_view)
        paymentTypeRecyclerView.layoutManager = LinearLayoutManager(activity)
        paymentTypeRecyclerViewAdapter = PaymentTypeAdapter(
            object : ViewTypeDelegateAdapter.OnViewSelectedListener {
                override fun onItemSelected(item: ViewType, position: Int) {
                    when (item) {
                        is RecyclerItemText -> Toast.makeText(
                            requireContext(),
                            getString(R.string.chosen_payment_type) + item.text,
                            Toast.LENGTH_SHORT
                        ).show()

                        else -> Log.d("DEBUG_1604", "loading")
                    }
                }
            },
            paymentTypeRecyclerView,
            requireActivity(),
            listOf(
                RecyclerItemInputDelete("xxx", getString(R.string.chosen_payment_type_buy)),
                RecyclerItemInputDelete("yyy", getString(R.string.chosen_payment_type_subscribe)),
                RecyclerItemInputDelete("yyy", getString(R.string.chosen_payment_type_subscribe)),
                RecyclerItemInputDelete("yyy", getString(R.string.chosen_payment_type_subscribe))
            )
        )
        paymentTypeRecyclerView.adapter = paymentTypeRecyclerViewAdapter
    }
}