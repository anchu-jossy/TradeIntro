package com.techxform.tradintro.feature_main.presentation.portfolio_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.databinding.PriceRowBinding
import com.techxform.tradintro.feature_main.domain.model.PriceType
import kotlin.math.roundToInt

class PriceAdapter(val list: ArrayList<PriceType>) :
    RecyclerView.Adapter<PriceAdapter.PriceViewHolder>() {


    inner class PriceViewHolder(private val itemViewBinding: PriceRowBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {
        fun binding() {
            itemViewBinding.price = list[adapterPosition]
            itemViewBinding.titleTv.text = "${list[adapterPosition].amount}"
            if (adapterPosition == 6 || adapterPosition == 7) {
                itemViewBinding.titleTv.text = "${list[adapterPosition].amount.roundToInt()}"
                if (!isPositive(itemViewBinding.titleTv.text as String))
                    itemViewBinding.titleTv.setTextColor(
                        ContextCompat.getColor(
                            itemViewBinding.root.context,
                            R.color.red
                        )
                    )
                else itemViewBinding.titleTv.setTextColor(
                    ContextCompat.getColor(
                        itemViewBinding.root.context,
                        R.color.green
                    )
                )

            }


        }

    }
private  fun isPositive(num :String): Boolean {
    try {
        val value: Double = num.toDouble()
        return value >= 0

    } catch (e: NumberFormatException) {
        System.out.println("String " + num.toString() + "is not a number")
    }
    return false
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