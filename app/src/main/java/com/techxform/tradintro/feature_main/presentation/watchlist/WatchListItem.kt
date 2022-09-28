package com.techxform.tradintro.feature_main.presentation.watchlist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.techxform.tradintro.R
import com.techxform.tradintro.databinding.RowItemBinding
import com.techxform.tradintro.databinding.RowItemSwipeBinding
import com.techxform.tradintro.feature_main.data.remote.dto.StockHistory
import com.techxform.tradintro.feature_main.data.remote.dto.WatchList
import com.techxform.tradintro.feature_main.presentation.utils.BaseItem

class WatchListItem(val selection: (action: Action, watchList: WatchList, position: Int) -> Unit) :
    BaseItem<RowItemSwipeBinding, WatchList>() {

    enum class Action {
        SELECT, DELETE
    }

    private var context: Context? = null
    override fun inflate(inflater: LayoutInflater, parent: ViewGroup): RowItemSwipeBinding {
        context = parent.context
        return RowItemSwipeBinding.inflate(inflater, parent, false)
    }

    override val bindFun = fun(binding: RowItemSwipeBinding, position: Int, item: WatchList) {
        binding.rowType = -1
        binding.watchlist = item
        if (position % 2 == 0) {
            context?.let {
                drawChart(
                    ContextCompat.getColor(it, R.color.dark_pink),
                    createData(item.market!!.history),
                    binding
                )
            }
        } else context?.let {
            drawChart(
                ContextCompat.getColor(it, R.color.light_blue_900),
                createData(item.market!!.history),
                binding
            )
        }
        binding.constraintContainer.setOnClickListener {

            selection(Action.SELECT, item, position)
        }

        binding.btnDelete.setOnClickListener {
            selection(Action.DELETE, item, position)
        }
    }

    private fun createData(
        list: MutableList<StockHistory>,

        ): ArrayList<Entry> {
        val arrayList = arrayListOf<Entry>()


        list.forEachIndexed { index, stockHistory ->
            arrayList.add(
                Entry(
                    index.toFloat(),
                    (stockHistory.stockHistoryHigh + stockHistory.stockHistoryLow) / 2
                )
            )

        }


        return arrayList
    }


    private fun drawChart(@ColorInt color: Int, values: ArrayList<Entry>, binding: RowItemSwipeBinding) {
        var set1 = LineDataSet(values, "Sample Data")
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


}

