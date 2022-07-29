package com.techxform.tradintro.feature_main.presentation.report

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.OrderBookFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.PortfolioItem
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderBookFragment :
    BaseFragment<OrderBookFragmentBinding>(OrderBookFragmentBinding::inflate) {

    private lateinit var viewModel: OrderBookViewModel
    private lateinit var portfolioList: ArrayList<PortfolioItem>
    private val limit = 10



    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.portfolioLiveData.observe(viewLifecycleOwner) {
            portfolioList = it.data
            binding.orderBookRv.adapter = OrderBookAdapter(it.data)
        }

        viewModel.portfolioErrorLiveData.observe(viewLifecycleOwner) {
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        Toast.makeText(
                            requireContext(), getString(R.string.no_internet_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    )
                }
                else -> {}
            }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[OrderBookViewModel::class.java]
        observers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isTradeBook = requireArguments().getBoolean(IsTradeBook)

        if (isTradeBook) {
            binding.titleTv.text = getString(R.string.trade_book_lbl)
            viewModel.portfolioList(SearchModel("", limit, 0, 0, orderStatus = "0", portfolioStatus = "0,1"))

        } else {
            binding.titleTv.text = getString(R.string.order_book_lbl)
            viewModel.portfolioList(SearchModel("", limit, 0, 0, orderStatus = "0,1,2,3,4", portfolioStatus = "0,1"))
        }

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