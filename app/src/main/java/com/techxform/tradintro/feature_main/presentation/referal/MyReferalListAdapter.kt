package com.techxform.tradintro.feature_main.presentation.recharge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.databinding.InviteRowItemBinding
import com.techxform.tradintro.feature_main.data.remote.dto.InviteData
import com.techxform.tradintro.feature_main.data.remote.dto.Notifications

class MyReferalListAdapter(val list: MutableList<InviteData>) :
    RecyclerView.Adapter<MyReferalListAdapter.MyReferalListVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReferalListVH {
        val binding =
            DataBindingUtil.inflate<InviteRowItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.invite_row_item,
                parent,
                false
            )
        return MyReferalListVH(binding)
    }


    override fun getItemCount(): Int {
        return 10
    }


    inner class MyReferalListVH(private val rowBinding: InviteRowItemBinding) :
        RecyclerView.ViewHolder(rowBinding.root) {
        fun binding() {

            rowBinding.data = list[adapterPosition]
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


    interface OnClickListener {
        fun onItemClick(position: Int, notifications: Notifications)
        fun onDeleteClick(position: Int, notifications: Notifications)
    }

    override fun onBindViewHolder(holder: MyReferalListVH, position: Int) {
        holder.binding()
    }


}