package com.techxform.tradintro.feature_main.presentation.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class SwipeAdapter<T : ViewBinding,I>(private val itemVB: BaseItem<T, I>, private val items:ArrayList<I>) :
    RecyclerView.Adapter<SimpleViewHolder<T, I>>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder<T, I> {
        return SimpleViewHolder(itemVB.onCreate(parent), itemVB.bindFun)
    }

    override fun onBindViewHolder(holder: SimpleViewHolder<T, I>, position: Int) {
        holder.bindFun(holder.binding, position, items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class SimpleViewHolder<T : ViewBinding,I>(
    val binding: T,
    val bindFun: (binding: T, position: Int,item:I) -> Unit
) : RecyclerView.ViewHolder(binding.root)

abstract class BaseItem<T : ViewBinding,I> {

    fun onCreate(parent: ViewGroup): T {
        return inflate(LayoutInflater.from(parent.context), parent)
    }

    abstract fun inflate(inflater: LayoutInflater, parent: ViewGroup): T

    abstract val bindFun: (T, Int,I) -> Unit
}
