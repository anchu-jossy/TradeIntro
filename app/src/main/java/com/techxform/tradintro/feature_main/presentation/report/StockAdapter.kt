package com.techxform.tradintro.feature_main.presentation.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.databinding.StockRowLayoutBinding

class StockAdapter(val list: ArrayList<String>) : RecyclerView.Adapter<StockAdapter.StockVH>() {


    inner class StockVH(private val stockRowLayoutBinding: StockRowLayoutBinding): RecyclerView.ViewHolder(stockRowLayoutBinding.root)
    {
        fun bind()
        {
            val lm = LinearLayoutManager(stockRowLayoutBinding.root.context, LinearLayoutManager.HORIZONTAL, false)
            stockRowLayoutBinding.priceRv.layoutManager = lm
            stockRowLayoutBinding.priceRv.adapter = PriceAdapter(arrayListOf())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockVH {
        val binding = DataBindingUtil.inflate<StockRowLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.stock_row_layout,
            parent,
            false
        )
        return StockVH(binding)
    }

    override fun onBindViewHolder(holder: StockVH, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
       return 5
    }
}