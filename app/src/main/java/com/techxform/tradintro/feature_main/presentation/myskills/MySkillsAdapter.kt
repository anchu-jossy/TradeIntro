package com.techxform.tradintro.feature_main.presentation.myskills

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.databinding.MyskillsItemBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Levels

class MySkillsAdapter(var list: ArrayList<Levels>, val listener: ClickListener, val myLevel: Int?) :
    RecyclerView.Adapter<MySkillsAdapter.MySkillsVH>() {


    inner class MySkillsVH(private val rowItemBinding: MyskillsItemBinding) :
        RecyclerView.ViewHolder(rowItemBinding.root) {
        fun binding() {
            with(rowItemBinding){
                level = list[absoluteAdapterPosition]
                userLevel=myLevel
                root.setOnClickListener {
                    listener.onItemClick(absoluteAdapterPosition,  list[absoluteAdapterPosition],myLevel)
                    textViewHeader
                }

            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySkillsVH {
        val binding = DataBindingUtil.inflate<MyskillsItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.myskills_item,
            parent,
            false
        )

        return MySkillsVH(binding)
    }

    override fun onBindViewHolder(holder: MySkillsVH, position: Int) {
        holder.binding()
    }

    override fun getItemCount(): Int {
         return list.size;
        //return 10
    }


    interface ClickListener {
        fun onItemClick(position: Int, levels: Levels, myLevel: Int?)
    }


}