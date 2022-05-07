package com.techxform.tradintro.feature_main.presentation.landing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.databinding.DrawerRowBinding
import com.techxform.tradintro.feature_main.domain.model.DrawerItem

class DrawerAdapter(val list: ArrayList<DrawerItem>, val listener:ClickListener) :
    RecyclerView.Adapter<DrawerAdapter.DrawerVH>() {

    inner class DrawerVH(private val drawerRowBinding: DrawerRowBinding) :
        RecyclerView.ViewHolder(drawerRowBinding.root) {
        fun binding() {
            drawerRowBinding.item = list[adapterPosition]
            drawerRowBinding.root.setOnClickListener {
                listener.onClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerVH {
        val binding = DataBindingUtil.inflate<DrawerRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.drawer_row,
            parent,
            false
        )
        return DrawerVH(binding)
    }

    override fun onBindViewHolder(holder: DrawerVH, position: Int) {
        holder.binding()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ClickListener{
        fun onClick(position: Int)
    }
}