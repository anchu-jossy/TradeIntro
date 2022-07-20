package com.techxform.tradintro.feature_main.presentation.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.databinding.RowPriceBinding

class PriceAdapter(val list: ArrayList<Pair<String, String>>) :
    RecyclerView.Adapter<PriceAdapter.PriceVH>() {


    inner class PriceVH(private val rowPriceBinding: RowPriceBinding) :
        RecyclerView.ViewHolder(rowPriceBinding.root) {
        fun bind() {
            rowPriceBinding.p = list[absoluteAdapterPosition]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceVH {
        val binding = DataBindingUtil.inflate<RowPriceBinding>(
            LayoutInflater.from(parent.context),
            R.layout.row_price,
            parent,
            false
        )
        return PriceVH(binding)
    }

    override fun onBindViewHolder(holder: PriceVH, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}