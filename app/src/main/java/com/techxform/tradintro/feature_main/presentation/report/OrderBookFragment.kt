package com.techxform.tradintro.feature_main.presentation.report

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.View
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.OrderBookFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderBookFragment :
    BaseFragment<OrderBookFragmentBinding>(OrderBookFragmentBinding::inflate) {


    private lateinit var viewModel: OrderBookViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[OrderBookViewModel::class.java]

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isTradeBook = requireArguments().getBoolean(IsTradeBook)

        if (isTradeBook)
            binding.titleTv.text = getString(R.string.trade_book_lbl)
        else binding.titleTv.text = getString(R.string.order_book_lbl)

        binding.orderBookRv.adapter = OrderBookAdapter(arrayListOf())
    }


    companion object {

        private const val IsTradeBook = "isTradeBook"

        @JvmStatic
        fun newInstance(isTradeBook: Boolean) =
            OrderBookFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IsTradeBook, isTradeBook)
                }
            }
    }

}