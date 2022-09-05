package com.techxform.tradintro.feature_main.presentation.myskills

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.databinding.LearnmoreItemBindingImpl
import com.techxform.tradintro.feature_main.data.remote.dto.Level

class LearnMoreAdapter(var list: ArrayList<Level>) :
    RecyclerView.Adapter<LearnMoreAdapter.LearnMoreVH>() {


    inner class LearnMoreVH(private val rowItemBinding: LearnmoreItemBindingImpl) :
        RecyclerView.ViewHolder(rowItemBinding.root) {
        fun binding() {
            with(rowItemBinding) {
                level = list[absoluteAdapterPosition]
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearnMoreVH {
        val binding = DataBindingUtil.inflate<LearnmoreItemBindingImpl>(
            LayoutInflater.from(parent.context),
            R.layout.learnmore_item,
            parent,
            false
        )

        return LearnMoreVH(binding)
    }

    override fun onBindViewHolder(holder: LearnMoreVH, position: Int) {
        if (position % 2 == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.itemView.setBackgroundColor(holder.itemView.rootView.context.getColor(R.color.light_ash_trans))
            };
        }
        holder.binding()
    }

    override fun getItemCount(): Int {
        return list.size

    }


    interface ClickListener {
        //  fun onItemClick(position: Int, levels: Levels, myLevel: Int?)
    }


}