package com.techxform.tradintro.feature_main.presentation.portfolio

import android.view.LayoutInflater
import android.view.View
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

class PortfolioAdapter(var list: ArrayList<PortfolioItem>,val isSelectionView:Boolean=false, val listener: ClickListener) :
    RecyclerView.Adapter<PortfolioAdapter.PortfolioVH>() {


    inner class PortfolioVH(private val rowItemBinding: RowItemBinding) :
        RecyclerView.ViewHolder(rowItemBinding.root) {
        fun binding() {
            rowItemBinding.rowType = 1
            rowItemBinding.isSelectionView = isSelectionView
            rowItemBinding.portfolio = list[absoluteAdapterPosition]

            if (absoluteAdapterPosition % 2 == 0) {
                drawChart(
                    ContextCompat.getColor(itemView.context, R.color.dark_pink), createData(
                        list[absoluteAdapterPosition].market.history,
                    ), rowItemBinding
                )
            } else drawChart(
                ContextCompat.getColor(itemView.context, R.color.light_blue_900), createData(
                    list[absoluteAdapterPosition].market.history
                ), rowItemBinding
            )
            rowItemBinding.root.setOnClickListener {
                listener.onItemClick(list[absoluteAdapterPosition], absoluteAdapterPosition)
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

    private fun createData(list: MutableList<StockHistory>?): ArrayList<Entry> {

        val arrayList = arrayListOf<Entry>()
        list?.forEachIndexed { index, stockHistory ->
            arrayList.add(
                Entry(
                    index.toFloat(),
                    (stockHistory.stockHistoryLow + stockHistory.stockHistoryHigh) / 2
                )
            )
        }
        return arrayList
    }

    private fun drawChart(@ColorInt color: Int, values: ArrayList<Entry>, binding: RowItemBinding) {

        if (values.isEmpty() || values.size < 3) {
            binding.lineChart.visibility = View.INVISIBLE
            return
        }
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