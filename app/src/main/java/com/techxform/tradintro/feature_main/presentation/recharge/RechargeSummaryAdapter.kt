package com.techxform.tradintro.feature_main.presentation.recharge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.databinding.RechargeRowItemBinding

import com.techxform.tradintro.feature_main.data.remote.dto.Notifications

class RechargeSummaryAdapter(val list: List<Any>?=null) : RecyclerView.Adapter<RechargeSummaryAdapter.RechargeSummaryVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RechargeSummaryVH {
        val binding = DataBindingUtil.inflate<com.techxform.tradintro.databinding.RechargeRowItemBinding>(
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
        return 10
        //return 10
    }


    inner class RechargeSummaryVH(private val notificationRowBinding: RechargeRowItemBinding): RecyclerView.ViewHolder(notificationRowBinding.root)
    {
        fun binding(){

//            notificationRowBinding.notification = list[adapterPosition]
//
//            notificationRowBinding.root.setOnClickListener {
//                listener.onItemClick(adapterPosition, list[adapterPosition])
//            }
//
//            notificationRowBinding.closeIv.setOnClickListener {
//                listener.onDeleteClick(adapterPosition, list[adapterPosition])
//            }
        }

    }


    interface OnClickListener{
        fun onItemClick(position:Int, notifications: Notifications)
        fun onDeleteClick(position: Int, notifications: Notifications)
    }


}