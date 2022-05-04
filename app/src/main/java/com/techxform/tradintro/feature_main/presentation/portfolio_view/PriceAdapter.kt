package com.techxform.tradintro.feature_main.presentation.portfolio_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.databinding.PriceRowBinding
import com.techxform.tradintro.feature_main.domain.model.PriceType

class PriceAdapter(val list: ArrayList<PriceType>) :
    RecyclerView.Adapter<PriceAdapter.PriceViewHolder>() {


    inner class PriceViewHolder(private val itemViewBinding: PriceRowBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {
        fun binding() {
            itemViewBinding.price = list[adapterPosition]
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        val binding = DataBindingUtil.inflate<PriceRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.price_row,
            parent,
            false
        )
        return PriceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        holder.binding()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}