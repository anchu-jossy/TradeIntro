package com.techxform.tradintro.feature_main.presentation.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.databinding.ProfitLossReportLayoutBinding

class ProfitLossAdapter(val list: ArrayList<String>) : RecyclerView.Adapter<ProfitLossAdapter.ProfitLossVH>() {


    inner class ProfitLossVH(profitLossReportLayoutBinding: ProfitLossReportLayoutBinding) :
        RecyclerView.ViewHolder(profitLossReportLayoutBinding.root) {

            fun bind()
            {

            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfitLossVH {
        val binding = DataBindingUtil.inflate<ProfitLossReportLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.profit_loss_report_layout,
            parent,
            false
        )
        return ProfitLossVH(binding)
    }

    override fun onBindViewHolder(holder: ProfitLossVH, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 4
    }


}