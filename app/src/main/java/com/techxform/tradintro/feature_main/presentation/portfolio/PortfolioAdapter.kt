package com.techxform.tradintro.feature_main.presentation.portfolio

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.techxform.tradintro.R
import com.techxform.tradintro.databinding.RowItemBinding
import com.techxform.tradintro.feature_main.data.remote.dto.PortfolioItem
import com.techxform.tradintro.feature_main.data.remote.dto.StockHistory

class PortfolioAdapter(var list: ArrayList<PortfolioItem>, val listener: ClickListener) :
    RecyclerView.Adapter<PortfolioAdapter.PortfolioVH>() {



    inner class PortfolioVH(private val rowItemBinding: RowItemBinding) :
        RecyclerView.ViewHolder(rowItemBinding.root) {
        fun binding() {
            rowItemBinding.rowType = 1
            rowItemBinding.portfolio = list[adapterPosition]
            val portfolio = list[adapterPosition]
            var currentValue = 0.0f
            val size = portfolio.market?.history?.size ?: 0
            if (size > 0) {
                currentValue = (portfolio.market.history.first().stockHistoryOpen +
                        portfolio.market.history.first().stockHistoryClose) / 2;
            }
            rowItemBinding.amountTv.text = currentValue.toString()
            val diff = (((currentValue - portfolio.orderPrice) /
                    ((currentValue + portfolio.orderPrice) / 2)) * 100);
            rowItemBinding.perTv.text = "% " + diff.toString();
            if (diff < 0) {
                rowItemBinding.perTv.setTextColor(Color.RED);
            } else if (diff > 0) {
                rowItemBinding.perTv.setTextColor(Color.GREEN);
            }

            if (adapterPosition % 2 == 0) {
                drawChart(
                    ContextCompat.getColor(itemView.context, R.color.dark_pink), createData(
                        list[adapterPosition].market.history,
                    ), rowItemBinding
                )
            } else drawChart(
                ContextCompat.getColor(itemView.context, R.color.light_blue_900), createData(
                    list[adapterPosition].market.history
                ), rowItemBinding
            )
            rowItemBinding.root.setOnClickListener {
                listener.onItemClick(list[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioVH {
        val binding = DataBindingUtil.inflate<RowItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.row_item,
            parent,
            false
        )
        return PortfolioVH(binding)
    }

    override fun onBindViewHolder(holder: PortfolioVH, position: Int) {
        holder.binding()
    }

    override fun getItemCount(): Int {
        return list.size
        //return 10
    }

    private fun createData(list: MutableList<StockHistory>): ArrayList<Entry> {
        val arrayList = arrayListOf<Entry>()

        /*list.clear()
        //TODO: remove it
        if (list.isNullOrEmpty()) {
            *//* arrayList.add(Entry(1F, 20.45F))
             arrayList.add(Entry(2F, 40.45F))
             arrayList.add(Entry(3F, 10.45F))
             arrayList.add(Entry(4F, 60.45F))
             arrayList.add(Entry(5F, 20.45F))
             arrayList.add(Entry(6F, 100.45F))*//*
            return arrayList
        }*/
        list.forEachIndexed { index, stockHistory ->
            arrayList.add(Entry(index.toFloat(), (stockHistory.stockHistoryClose + stockHistory.stockHistoryOpen)/2))
        }
        return arrayList
    }

    private fun drawChart(@ColorInt color: Int, values: ArrayList<Entry>, binding: RowItemBinding) {
        val set1 = LineDataSet(values, "Sample Data")
        set1.color = color
        set1.setDrawIcons(false)
        set1.setCircleColor(color)
        set1.lineWidth = 2f
        set1.circleRadius = 1f
        set1.setDrawCircleHole(false)
        set1.setDrawValues(false)

        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(set1)

        binding.lineChart.axisRight.setDrawLabels(false)
        binding.lineChart.axisRight.setDrawAxisLine(false)
        binding.lineChart.axisRight.setDrawGridLines(false)
        binding.lineChart.axisLeft.setDrawAxisLine(false)
        binding.lineChart.axisLeft.setDrawLabels(false)
        binding.lineChart.axisLeft.setDrawGridLines(false)
        binding.lineChart.xAxis.setDrawLabels(false)
        binding.lineChart.xAxis.setDrawAxisLine(false)
        binding.lineChart.xAxis.setDrawGridLines(false)
        binding.lineChart.xAxis.isEnabled = false
        binding.lineChart.description.isEnabled = false
        binding.lineChart.legend.isEnabled = false
        binding.lineChart.setTouchEnabled(false)
        binding.lineChart.setPinchZoom(false)

        val data = LineData(dataSets)
        binding.lineChart.data = data

    }

    interface ClickListener {
        fun onItemClick(portfolioItem: PortfolioItem, position: Int)
    }


}