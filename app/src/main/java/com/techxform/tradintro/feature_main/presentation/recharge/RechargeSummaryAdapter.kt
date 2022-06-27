package com.techxform.tradintro.feature_main.presentation.recharge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.databinding.RechargeRowItemBinding

import com.techxform.tradintro.feature_main.data.remote.dto.Notifications
import com.techxform.tradintro.feature_main.data.remote.dto.WalletHistory

class RechargeSummaryAdapter(val list: ArrayList<WalletHistory>? = null) :
    RecyclerView.Adapter<RechargeSummaryAdapter.RechargeSummaryVH>() {

    private var selection: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RechargeSummaryVH {
        val binding = DataBindingUtil.inflate<RechargeRowItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.recharge_row_item,
            parent,
            false
        )
        return RechargeSummaryVH(binding)
    }

    override fun onBindViewHolder(holder: RechargeSummaryVH, position: Int) {
        holder.binding()
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }


    inner class RechargeSummaryVH(private val rechargeRowItemBinding: RechargeRowItemBinding) :
        RecyclerView.ViewHolder(rechargeRowItemBinding.root) {
        fun binding() {
            with(rechargeRowItemBinding)
            {
                val context = root.context

                subTitleTv.text = context.getString(
                    R.string.rs_format,
                    list?.get(absoluteAdapterPosition)?.netAmount
                )
                perTv.text = context.getString(
                    R.string.rs_format,
                    list?.get(absoluteAdapterPosition)?.walletTradeValue
                )
                if(selection == 0)
                amountTv.text = context.getString(R.string.trade_money)
                else amountTv.text = context.getString(R.string.voucher_code)

            }
        }

    }

    fun setSelectionType(selectionType: Int) {
        selection = selectionType
    }


    interface OnClickListener {
        fun onItemClick(position: Int, notifications: Notifications)
        fun onDeleteClick(position: Int, notifications: Notifications)
    }


}