package com.techxform.tradintro.feature_main.presentation.market

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.MarketDetailFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.CreateWatchListRequest
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.StockHistory
import com.techxform.tradintro.feature_main.domain.model.PriceType
import com.techxform.tradintro.feature_main.presentation.portfolio_view.PriceAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class MarketDetailFragment :
    BaseFragment<MarketDetailFragmentBinding>(MarketDetailFragmentBinding::inflate) {

    companion object {
        fun newInstance() = MarketDetailFragment()
    }

    private lateinit var viewModel: MarketDetailViewModel
    private var stockId by Delegates.notNull<Int>()
    private var totalPrice by Delegates.notNull<Int>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MarketDetailViewModel::class.java]

        stockId = requireArguments().getInt("stockId")
        totalPrice = requireArguments().getInt("totalPrice")

        binding.amountTv.text =getString(R.string.rs_format,totalPrice.toFloat())
        observers()
        viewModel.marketDetail(stockId)
        binding.ediTextAddtoWatchList.setText("$totalPrice.00")
        binding.watchlistPlusBtn.setOnClickListener {
            if (binding.stock?.watchList == null)
                viewModel.createWatchList(CreateWatchListRequest(stockId,  binding.ediTextAddtoWatchList.text.toString().toDouble()))
            else viewModel.updateWatchList(binding.ediTextAddtoWatchList.text.toString().toDouble())
        }


    }


    private fun createPriceType(stockHistory: StockHistory?): ArrayList<PriceType> {
        val priceTypes = arrayListOf<PriceType>()

        if (stockHistory == null) { //TODO: Remove
        /*    priceTypes.add(PriceType(4663.36f, getString(R.string.open_lbl)))
            priceTypes.add(PriceType(4363.36f, getString(R.string.close_lbl)))
            priceTypes.add(PriceType(3728.34f, getString(R.string.lower_circuit_lbl)))
            priceTypes.add(PriceType(47243.3f, getString(R.string.upper_circuit_lbl)))*/
        } else {
            priceTypes.add(PriceType(stockHistory.stockHistoryOpen, getString(R.string.open_lbl)))
            priceTypes.add(PriceType(stockHistory.stockHistoryClose, getString(R.string.close_lbl)))

            priceTypes.add(
                PriceType(
                    stockHistory.stockHistoryLow,
                    getString(R.string.lower_circuit_lbl)
                )
            )
            priceTypes.add(
                PriceType(
                    stockHistory.stockHistoryHigh,
                    getString(R.string.upper_circuit_lbl)
                )
            )

            val avg = (stockHistory.stockHistoryHigh + stockHistory.stockHistoryLow + stockHistory.stockHistoryClose + stockHistory.stockHistoryOpen)/4
            priceTypes.add(
                PriceType(
                    avg,
                    getString(R.string.avg_traded_lbl)
                )
            )
        }
        return priceTypes

    }

    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.marketDetailLiveData.observe(viewLifecycleOwner) {
            binding.stock = it.data
            binding.priceRv.adapter = PriceAdapter(createPriceType(it.data?.history?.get(0)))
        }
        viewModel.createWatchListLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Successfully added to watchlist", Toast.LENGTH_LONG)
                .show()
        }
        viewModel.updateWatchListLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Successfully updated to watchlist", Toast.LENGTH_LONG)
                .show()
        }
        viewModel.marketErrorLiveData.observe(viewLifecycleOwner) {
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        Toast.makeText(
                            requireContext(), getString(R.string.no_internet_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    )
                }
                Failure.ServerError-> {
                    Toast.makeText(requireContext(), " Server failed", Toast.LENGTH_LONG).show()

                }
                else -> {
                    Toast.makeText(requireContext(), " Api failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}