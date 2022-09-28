package com.techxform.tradintro.feature_main.presentation.portfolio_view

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.databinding.PriceRowBinding
import com.techxform.tradintro.feature_main.domain.model.PriceType
import com.techxform.tradintro.feature_main.domain.util.Utils
import java.text.DecimalFormat

class PriceAdapter(val list: ArrayList<PriceType>) :
    RecyclerView.Adapter<PriceAdapter.PriceViewHolder>() {


    inner class PriceViewHolder(private val itemViewBinding: PriceRowBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {
        fun binding() {
            itemViewBinding.price = list[absoluteAdapterPosition]
            if( list[absoluteAdapterPosition].type==(itemViewBinding.root.resources.getString(R.string.quantity)) )
                itemViewBinding.titleTv.text =  DecimalFormat("0.#").format( list[absoluteAdapterPosition].amount)
                   .toString()
            else if( list[absoluteAdapterPosition].text.isNullOrEmpty()) {
                itemViewBinding.titleTv.text = itemViewBinding.root.context.getString(
                    R.string.rs_format,
                    list[absoluteAdapterPosition].amount)

            }else{
                val text=list[absoluteAdapterPosition].text
                itemViewBinding.titleTv.text=text
                if(text.equals("BUY",true)){
                    itemViewBinding.titleTv.setTextColor(Color.GREEN)
                }else if(text.equals("SELL",true)){
                    itemViewBinding.titleTv.setTextColor(Color.RED)
                }
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