package com.techxform.tradintro.feature_main.presentation.market

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
import com.techxform.tradintro.feature_main.data.remote.dto.Stock
import com.techxform.tradintro.feature_main.data.remote.dto.StockHistory

class MarketListAdapter(var list: ArrayList<Stock>, private val listener: OnItemClickListner) :
    RecyclerView.Adapter<MarketListAdapter.MarketListVH>() {


    inner class MarketListVH(private val rowItemBinding: RowItemBinding) :
        RecyclerView.ViewHolder(rowItemBinding.root) {
        fun binding() {
            rowItemBinding.rowType = 0
            rowItemBinding.stock = list[adapterPosition]

            val openPrice = list[adapterPosition].history?.first()?.stockHistoryOpen?.toInt() ?: 0
            val closePrice = list[adapterPosition].history?.first()?.stockHistoryClose?.toInt() ?: 0
            val totalPrice = openPrice.plus(closePrice)
            val todayPrice = (totalPrice / 2)
            var percentage=0;
            val size=list[adapterPosition]?.history?.size?:0;
            if(size >1) {
                val openPrice2 =
                    list[adapterPosition].history?.get(1)?.stockHistoryOpen?.toInt() ?: 0
                val closePrice2 =
                    list[adapterPosition].history?.get(1)?.stockHistoryClose?.toInt() ?: 0
                val totalPrice2 = openPrice2.plus(closePrice2)
                val yesterdayPrice = (totalPrice2 / 2)

                if(todayPrice !=0 && yesterdayPrice != 0 )
                percentage = ((todayPrice-yesterdayPrice)/((todayPrice+yesterdayPrice)/2))*100;
            }

            list[adapterPosition].totalPrice = todayPrice
            rowItemBinding.amountTv.text = todayPrice.toString()
            rowItemBinding.perTv.text = "% "+percentage.toString()
            if(percentage<0){
                rowItemBinding.perTv.setTextColor(Color.RED);
            }else if (percentage>0){
                rowItemBinding.perTv.setTextColor(Color.GREEN);
            }
            if (adapterPosition % 2 == 0) {
                drawChart(
                    ContextCompat.getColor(itemView.context, R.color.dark_pink),
                    createData(list[adapterPosition].history, adapterPosition),
                    rowItemBinding
                )
            } else drawChart(
                ContextCompat.getColor(itemView.context, R.color.light_blue_900),
                createData(list[adapterPosition].history, adapterPosition),
                rowItemBinding
            )
            rowItemBinding.cardContainer.setOnClickListener {
                listener.onItemClick(list[adapterPosition], adapterPosition)
            }
            rowItemBinding.constraintContainer.setOnClickListener {
                listener.onItemClick(list[adapterPosition], adapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketListVH {
        val binding = DataBindingUtil.inflate<RowItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.row_item,
            parent,
            false
        )
        return MarketListVH(binding)
    }

    override fun onBindViewHolder(holder: MarketListVH, position: Int) {
        holder.binding()
    }

    override fun getItemCount(): Int {
        return list.size
    }


    private fun createData(
        list: MutableList<StockHistory>,
        adapterPosition: Int
    ): ArrayList<Entry> {
        val arrayList = arrayListOf<Entry>()

        //TODO: remove it
        if (list.isNullOrEmpty()) {
           /* arrayList.add(Entry(1F, 20.45F))
            arrayList.add(Entry(2F, 40.45F))
            arrayList.add(Entry(3F, 10.45F))
            arrayList.add(Entry(4F, 60.45F))
            arrayList.add(Entry(5F, 20.45F))
            arrayList.add(Entry(6F, 100.45F))*/
            return arrayList
        } else {
            for (history in list){
                Entry(
                    history.stockHistoryOpen,
                    history.stockHistoryClose
                )
            }
            /*      arrayList.add(
                      Entry(
                          list[adapterPosition ].stockHistoryOpen,
                          list[adapterPosition ].stockHistoryClose
                      )
                  )*/
            return arrayList
        }

        list.forEachIndexed { index, stockHistory ->
            arrayList.add(Entry(index.toFloat(), stockHistory.stockHistoryClose))

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

    interface OnItemClickListner {
        fun onItemClick(stock: Stock,position: Int)
    }
}