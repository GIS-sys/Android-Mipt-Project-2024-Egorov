package com.giswarm.mipt_2024.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giswarm.mipt_2024.R
import com.giswarm.mipt_2024.composable.OneTextComposable
import com.giswarm.mipt_2024.position.MoonPositionManager
import com.giswarm.mipt_2024.recycler.MoonShapeAdapter
import com.giswarm.mipt_2024.recycler.PaymentTypeAdapter
import com.giswarm.mipt_2024.recycler.RecyclerItemCircle
import com.giswarm.mipt_2024.recycler.RecyclerItemInputDelete
import com.giswarm.mipt_2024.recycler.RecyclerItemSquare
import com.giswarm.mipt_2024.recycler.RecyclerItemText
import com.giswarm.mipt_2024.recycler.RecyclerItemTextImage
import com.giswarm.mipt_2024.recycler.ViewType
import com.giswarm.mipt_2024.recycler.ViewTypeDelegateAdapter
import com.giswarm.mipt_2024.repository.MoonPositionRepository
import com.giswarm.mipt_2024.repository.MoonSavedRepository
import com.giswarm.mipt_2024.storage.Consts
import com.giswarm.mipt_2024.storage.DrawableManager

class CredentialsFragment : Fragment(R.layout.fragment_credentials) {
    private lateinit var paymentTypeRecyclerView: RecyclerView
    private lateinit var paymentTypeRecyclerViewAdapter: PaymentTypeAdapter
    private lateinit var buttonAdd: Button

    private val moonPositionRepository = MoonPositionRepository()
    private val moonSavedRepository = MoonSavedRepository()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paymentTypeRecyclerView = view.findViewById(R.id.cred_payment_recycler_view)
        paymentTypeRecyclerView.layoutManager = LinearLayoutManager(activity)
        paymentTypeRecyclerViewAdapter = PaymentTypeAdapter(
            moonSavedRepository.getSavedList().ifEmpty { currentElement(0) }
        )
        paymentTypeRecyclerView.adapter = paymentTypeRecyclerViewAdapter

        buttonAdd = view.findViewById(R.id.cred_payment_btn)
        buttonAdd.setOnClickListener {
            paymentTypeRecyclerViewAdapter.add(currentElement())
            moonSavedRepository.setSavedList(paymentTypeRecyclerViewAdapter.items)
        }
    }

    private fun currentElement(idArg: Int? = null): List<RecyclerItemInputDelete> {
        val id: Int = idArg ?: paymentTypeRecyclerViewAdapter.itemCount
        return listOf(
            RecyclerItemInputDelete(
                "position$id",
                moonPositionRepository.getPosition().toString()
            )
        )
    }
}