package com.techxform.tradintro.feature_main.presentation.report

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.databinding.OrderBookRowBinding
import com.techxform.tradintro.feature_main.data.remote.dto.PortfolioItem

class OrderBookAdapter(
    val isTradeBook: Boolean,
    val list: ArrayList<PortfolioItem>, val listener: ClickListener) : RecyclerView.Adapter<OrderBookAdapter.OrderBookVH>() {


    inner class OrderBookVH(private val orderBookRowBinding: OrderBookRowBinding): RecyclerView.ViewHolder(orderBookRowBinding.root)
    {
        fun bind()
        {
            orderBookRowBinding.portfolio = list[absoluteAdapterPosition]
            val lm = LinearLayoutManager(orderBookRowBinding.root.context, LinearLayoutManager.HORIZONTAL, false)
            orderBookRowBinding.priceRv.layoutManager = lm
            orderBookRowBinding.priceRv.adapter = PriceAdapter(createPricingList(list[absoluteAdapterPosition]))
            if (isTradeBook) {
                orderBookRowBinding.btnEdit.visibility = View.GONE
            } else {
                orderBookRowBinding.btnEdit.setOnClickListener {
                    listener.onEditBtnClick(list[absoluteAdapterPosition])
                }
               /* orderBookRowBinding.btnDelete.setOnClickListener {
                    listener.onDeleteBtnClick(list[absoluteAdapterPosition])
                }*/
            }
        }


        private fun createPricingList(portfolioItem: PortfolioItem) : ArrayList<Pair<String,String>> {
            var priceList = arrayListOf<Pair<String, String>>()
            val context = orderBookRowBinding.root.context

            priceList.add(
                Pair(
                    context.getString(R.string.txn_type_price_lbl),
                    portfolioItem.orderPrice.toString()
                )
            )
            priceList.add(
                Pair(
                    context.getString(R.string.quantity_lbl),
                    portfolioItem.orderQty.toString()
                )
            )
            priceList.add(
                Pair(
                    context.getString(R.string.brokerage_lbl),
                    portfolioItem.brokerage.toString()
                )
            )
            priceList.add(
                Pair(
                    context.getString(R.string.txn_charges_lbl),
                    portfolioItem.transactionCharge.toString()
                )
            )
            priceList.add(
                Pair(
                    context.getString(R.string.total_price_lbl),
                    portfolioItem.orderTotal.toString()
                )
            )

            return priceList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderBookVH {
        val binding = DataBindingUtil.inflate<OrderBookRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.order_book_row,
            parent,
            false
        )
        return OrderBookVH(binding)
    }

    override fun onBindViewHolder(holder: OrderBookVH, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface ClickListener{
        fun onEditBtnClick(portfolio: PortfolioItem)
        fun onDeleteBtnClick(portfolio: PortfolioItem)
    }
}