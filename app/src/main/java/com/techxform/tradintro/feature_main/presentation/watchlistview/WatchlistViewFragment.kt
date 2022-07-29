package com.techxform.tradintro.feature_main.presentation.watchlistview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.WatchlistViewFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.WatchList
import com.techxform.tradintro.feature_main.domain.model.PriceType
import com.techxform.tradintro.feature_main.domain.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class WatchlistViewFragment :
    BaseFragment<WatchlistViewFragmentBinding>(WatchlistViewFragmentBinding::inflate) {

    companion object {
        fun newInstance() = WatchlistViewFragment()
    }

    private lateinit var viewModel: WatchlistViewViewModel
    private var watchlistId by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[WatchlistViewViewModel::class.java]
        observers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        watchlistId = requireArguments().getInt("watchlistId")

        binding.alertPriceType = PriceType(63.2f, getString(R.string.alert_price_lbl))


        viewModel.watchlistDetails(watchlistId)
    }

    private fun setGainProfit(watchList: WatchList) {
        var currentPrice = 0.0f
        val size = watchList.market?.history?.size ?: 0;
        if (size > 0) {
            currentPrice =
                (watchList.market?.history?.firstOrNull()?.stockHistoryOpen?.plus(watchList.market.history?.firstOrNull()!!.stockHistoryClose)
                    ?.div(2) ?: 0) as Float
        }
        val gainLoss = currentPrice.minus(watchList.watchStockPrice)
        val per =
            ((currentPrice - watchList.watchStockPrice) / ((currentPrice + watchList.watchStockPrice) / 2)) * 100
        with(binding) {
            binding.alertPrice.titleTv.text =Utils.formatStringToTwoDecimals(watchList.watchStockPrice.toString())
            gainLossPrice.titleTv.text = Utils.formatStringToTwoDecimals(gainLoss.toString())
            gainLossPerPrice.titleTv.text = getString(R.string.per_format_string,Utils.formatPercentageWithoutDecimals(per.toString()))
            gainLossPrice.subTitleTv.text = getString(R.string.gain_loss_lbl)
            gainLossPerPrice.subTitleTv.text = getString(R.string.per_gain_loss_lbl)

            if (per < 0) {
                gainLossPerPrice.titleTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
                gainLossPerPrice.subTitleTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
            }
            if (gainLoss < 0) {
                gainLossPrice.titleTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
                gainLossPrice.subTitleTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
            }
        }
    }

    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.watchlistViewLiveData.observe(viewLifecycleOwner) {
            binding.watchlist = it.data
            setGainProfit(it.data)
        }

        viewModel.watchlistViewErrorLiveData.observe(viewLifecycleOwner) {
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

}