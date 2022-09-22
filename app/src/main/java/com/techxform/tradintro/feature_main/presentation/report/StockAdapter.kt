package com.techxform.tradintro.feature_main.presentation.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.databinding.StockRowLayoutBinding
import com.techxform.tradintro.feature_main.data.remote.dto.PortfolioItem

class StockAdapter(
    val type: @ProfitLossReportFragment.Companion.TYPE Int,
    val list: ArrayList<PortfolioItem>
) :
    RecyclerView.Adapter<StockAdapter.StockVH>() {


    inner class StockVH(private val stockRowLayoutBinding: StockRowLayoutBinding) :
        RecyclerView.ViewHolder(stockRowLayoutBinding.root) {
        fun bind() {
            stockRowLayoutBinding.type = type
            stockRowLayoutBinding.portfolio = list[absoluteAdapterPosition]
            val lm = LinearLayoutManager(
                stockRowLayoutBinding.root.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            stockRowLayoutBinding.priceRv.layoutManager = lm
            stockRowLayoutBinding.priceRv.adapter =
                PriceAdapter(createPricingList(list[absoluteAdapterPosition]))
        }

        private fun createPricingList(portfolioItem: PortfolioItem): ArrayList<Pair<String, String>> {
            var priceList = arrayListOf<Pair<String, String>>()
            val context = stockRowLayoutBinding.root.context

            priceList.add(
                Pair(
                    context.getString(R.string.quantity_lbl),
                    portfolioItem.orderQty.toString()
                )
            )
            priceList.add(
                Pair(
                    context.getString(R.string.avg_purchase_price_lbl),
                    portfolioItem.orderPrice.toString()
                )
            )

            when (type) {
                0 -> {
                    priceList.add(
                        Pair(
                            context.getString(R.string.current_price_lbl),
                            portfolioItem.market.currentValue().toString()
                        )
                    )
                    priceList.add(
                        Pair(
                            context.getString(R.string.avg_sell_price_lbl),
                            portfolioItem.orderPrice.toString()
                        )
                    )
                }
                1 -> {
                    priceList.add(
                        Pair(
                            context.getString(R.string.avg_sell_price_lbl),
                            portfolioItem.orderPrice.toString()
                        )
                    )
                    priceList.add(
                        Pair(
                            context.getString(R.string.total_buy_value_lbl),
                            portfolioItem.totalStockValue.toString()
                        )
                    )
                    priceList.add(
                        Pair(
                            context.getString(R.string.total_sell_value_lbl),
                            portfolioItem.totalStockValue.toString()
                        )
                    )
                }
                2 -> {
                    priceList.add(
                        Pair(
                            context.getString(R.string.total_buy_value_lbl),
                            portfolioItem.totalStockValue.toString()
                        )
                    )
                    priceList.add(
                        Pair(
                            context.getString(R.string.current_price_lbl),
                            portfolioItem.market.currentValue().toString()
                        )
                    )
                    priceList.add(
                        Pair(
                            context.getString(R.string.total_current_value_lbl),
                            (portfolioItem.market.currentValue() * portfolioItem.orderQty).toString()
                        )
                    )
                }
            }



            return priceList
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockVH {
        val binding = DataBindingUtil.inflate<StockRowLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.stock_row_layout,
            parent,
            false
        )
        return StockVH(binding)
    }

    override fun onBindViewHolder(holder: StockVH, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}