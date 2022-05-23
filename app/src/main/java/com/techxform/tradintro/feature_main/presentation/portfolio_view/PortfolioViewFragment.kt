package com.techxform.tradintro.feature_main.presentation.portfolio_view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.PortfolioViewFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.PortfolioItem
import com.techxform.tradintro.feature_main.domain.model.FilterModel
import com.techxform.tradintro.feature_main.domain.model.PriceType
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class PortfolioViewFragment :
    BaseFragment<PortfolioViewFragmentBinding>(PortfolioViewFragmentBinding::inflate) {

    companion object {
        fun newInstance() = PortfolioViewFragment()
    }

    private lateinit var viewModel: PortfolioViewViewModel

    private var orderId by Delegates.notNull<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[PortfolioViewViewModel::class.java]

        orderId = requireArguments().getInt("orderId")
        binding.portfolioDashboard = requireArguments().getParcelable("portfolioDashboard")!!
        observers()

        val spinnerArr = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")

        val arrAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerArr)

        arrAdapter.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item
        )
        binding.filterSpinner.adapter = arrAdapter

        viewModel.portfolioDetails(orderId, FilterModel("", 100, 0, 0, ""))

    }

    private fun createPriceType(portfolioItem: PortfolioItem): ArrayList<PriceType> {
        val priceTypes = arrayListOf<PriceType>()
        var currentPrice = 0.0f
        val size= portfolioItem?.market?.history?.size ?:0;
        if (size> 0) {
            currentPrice = (portfolioItem.market.history.first().stockHistoryOpen +
                    portfolioItem.market.history.first().stockHistoryClose) / 2;
        }
        priceTypes.add(PriceType(currentPrice, getString(R.string.current_price_lbl)))
        priceTypes.add(
            PriceType(
                portfolioItem.orderPrice,
                getString(R.string.avg_purchase_price_lbl)
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
        priceTypes.add(PriceType(portfolioItem.orderPrice, getString(R.string.alert_price_lbl)))

        priceTypes.add(
            PriceType(
                currentPrice.minus(portfolioItem.orderPrice),
                getString(R.string.gain_loss_lbl)
            )
        )
        priceTypes.add(
            PriceType(
                ((currentPrice - portfolioItem.orderPrice) / ((currentPrice + portfolioItem.orderPrice) / 2)) * 100,
                getString(R.string.per_gain_loss_lbl)
            )
        )
        return priceTypes

    }

    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.portfolioLiveData.observe(viewLifecycleOwner) {
            binding.portfolio = it.data
            binding.priceRv.adapter = PriceAdapter(createPriceType(it.data))
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
                else -> {
                }
            }
        }
    }


}