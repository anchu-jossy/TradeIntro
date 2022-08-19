package com.techxform.tradintro.feature_main.presentation.report

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.techxform.tradintro.databinding.SpinnerItemBinding

class SpinnerAdapter(val context: Context, val list: Array<String>) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        //var binding: SpinnerItemBinding
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


            val binding = SpinnerItemBinding.inflate(
                inflater,
                p2,
                false
            )
             //= inflater.inflate(R.layout.spinner_item, p2, false)

        binding.titleTv.text = list[p0]
        return binding.root
    }


}