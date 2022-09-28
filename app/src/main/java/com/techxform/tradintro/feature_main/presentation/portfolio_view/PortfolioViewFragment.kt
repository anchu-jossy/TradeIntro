package com.techxform.tradintro.feature_main.presentation.portfolio_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.core.utils.ScreenType
import com.techxform.tradintro.databinding.PortfolioViewFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.PortfolioItem
import com.techxform.tradintro.feature_main.data.remote.dto.StockDashboard
import com.techxform.tradintro.feature_main.domain.model.FilterModel
import com.techxform.tradintro.feature_main.domain.model.PriceType
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import com.techxform.tradintro.feature_main.presentation.equality_place_order.EqualityPlaceOrderFragment
import com.techxform.tradintro.feature_main.presentation.equality_place_order.EqualityPlaceOrderFragment.Companion.SELL
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class PortfolioViewFragment :
    BaseFragment<PortfolioViewFragmentBinding>(PortfolioViewFragmentBinding::inflate){

    companion object {
        fun newInstance() = PortfolioViewFragment()
    }

    private lateinit var viewModel: PortfolioViewViewModel
    private lateinit var portfolioItem: PortfolioItem

    private var orderId by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[PortfolioViewViewModel::class.java]
        observers()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderId = requireArguments().getInt("orderId")
        requireArguments().getParcelable<StockDashboard?>("StockDashboard")?.let {
            binding.stockDashboard = it
        }

        requireArguments().getParcelable<PortfolioItem?>("PortfolioItem")?.let {
            viewModel.setPortfolioItem(it)
        } ?: run {
            viewModel.portfolioDetails(orderId, FilterModel("", 100, 0, 0, ""))
        }

    }

    private fun createPriceType(portfolioItem: PortfolioItem): ArrayList<PriceType> {
        val priceTypes = arrayListOf<PriceType>()
        var currentPrice = 0.0f
        val size = portfolioItem.market.history.size ?: 0;
        if (size > 0) {
            currentPrice = (portfolioItem.market.history.first().stockHistoryHigh +
                    portfolioItem.market.history.first().stockHistoryLow) / 2
        }
        val c = portfolioItem.market.currentValue()
        //priceTypes.add(PriceType(currentPrice, getString(R.string.current_price_lbl)))
        priceTypes.add(
            PriceType(
                portfolioItem.orderPrice,
                getString(R.string.txn_type_price_lbl)
            )
        )
        priceTypes.add(
            PriceType(
                portfolioItem.orderQty.toFloat(),
                getString(R.string.quantity_lbl)
            )
        )
        priceTypes.add(PriceType(portfolioItem.orderTotal, getString(R.string.total_price_lbl)))
        priceTypes.add(
            PriceType(
                portfolioItem.totalStockValue,
                getString(R.string.total_value_lbl)
            )
        )
        priceTypes.add(
            PriceType(
                portfolioItem.brokerage,
                getString(R.string.brokerage_lbl)
            )
        )
        priceTypes.add(
            PriceType(
                portfolioItem.orderTotal - (portfolioItem.totalStockValue + portfolioItem.brokerage),
                getString(R.string.other_charges_lbl)
            )
        )
        priceTypes.add(
            PriceType(
                portfolioItem.orderTotal ,
                getString(R.string.net_value_lbl)
            )
        )
        priceTypes.add(
            PriceType(
                0f,
                getString(R.string.type_lbl),
                if(portfolioItem.orderType==0) "BUY" else "SELL"
            )
        )
        return priceTypes
    }

    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.portfolioLiveData.observe(viewLifecycleOwner) {
            it.data.let { data ->
                binding.portfolio = data
                portfolioItem = data
                binding.priceRv.adapter = PriceAdapter(createPriceType(data))
            }
        }

        viewModel.portfolioErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }

        viewModel.buyStockErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }
        viewModel.sellStockErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }

        viewModel.sellStockLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                findNavController().navigate(
                    R.id.equalityPlaceOrderFragment, EqualityPlaceOrderFragment.navBundle(
                        portfolioItem.market.stockId,
                        SELL, ScreenType.PORTFOLIO, portfolioItem.market
                    )
                )

            }
        }
    }

    private fun handleError(failure: Failure) {
        when (failure) {
            Failure.NetworkConnection -> {
                sequenceOf(
                    requireContext().showShortToast(getString(R.string.no_internet_error))

                )
            }
            Failure.ServerError -> {
                requireContext().showShortToast(getString(R.string.server_error))

            }
            else -> {
                val errorMsg = (failure as Failure.FeatureFailure).message
                requireContext().showShortToast("Error: $errorMsg")
            }
        }
    }


}