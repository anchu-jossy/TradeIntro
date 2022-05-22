package com.techxform.tradintro.feature_main.presentation.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.databinding.NotificationRowBinding
import com.techxform.tradintro.databinding.RowItemBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Notifications

class NotificationAdapter(val list : ArrayList<Notifications>,val listener: OnClickListener) : RecyclerView.Adapter<NotificationAdapter.NotificationVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationVH {
        val binding = DataBindingUtil.inflate<NotificationRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.notification_row,
            parent,
            false
        )
        return NotificationVH(binding)
    }

    override fun onBindViewHolder(holder: NotificationVH, position: Int) {
        holder.binding()
    }

    override fun getItemCount(): Int {
        return list.size
        //return 10
    }


    inner class NotificationVH(private val notificationRowBinding: NotificationRowBinding): RecyclerView.ViewHolder(notificationRowBinding.root)
    {
        fun binding(){

            notificationRowBinding.root.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

            notificationRowBinding.closeIv.setOnClickListener {
                listener.onDeleteClick(adapterPosition)
            }
        }

    }


    interface OnClickListener{
        fun onItemClick(position:Int)
        fun onDeleteClick(position: Int)
    }


}