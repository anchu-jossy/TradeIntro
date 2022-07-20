package com.techxform.tradintro.feature_main.presentation.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.databinding.OrderBookRowBinding

class OrderBookAdapter (val list: ArrayList<String>) : RecyclerView.Adapter<OrderBookAdapter.OrderBookVH>() {


    inner class OrderBookVH(private val orderBookRowBinding: OrderBookRowBinding): RecyclerView.ViewHolder(orderBookRowBinding.root)
    {
        fun bind()
        {
            val lm = LinearLayoutManager(orderBookRowBinding.root.context, LinearLayoutManager.HORIZONTAL, false)
            orderBookRowBinding.priceRv.layoutManager = lm
            orderBookRowBinding.priceRv.adapter = PriceAdapter(arrayListOf())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderBookVH {
        val binding = DataBindingUtil.inflate<OrderBookRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.order_book_row,
            parent,
            false
        )
        return OrderBookVH(binding)
    }

    override fun onBindViewHolder(holder: OrderBookVH, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 5
    }
}