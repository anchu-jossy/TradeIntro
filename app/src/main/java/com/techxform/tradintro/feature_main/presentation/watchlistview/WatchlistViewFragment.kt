package com.techxform.tradintro.feature_main.presentation.watchlistview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.WatchlistViewFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.domain.model.PriceType
import com.techxform.tradintro.feature_main.presentation.watchlist.WatchListAdapter
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[WatchlistViewViewModel::class.java]
        watchlistId = requireArguments().getInt("watchlistId")

        observers()
        binding.alertPriceType = PriceType(63.2f, getString(R.string.alert_price_lbl))

        val spinnerArr = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
        val arrAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerArr)
        arrAdapter.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item
        )
        binding.filterSpinner.adapter = arrAdapter

        viewModel.watchlistDetails(watchlistId)
    }

    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.watchlistViewLiveData.observe(viewLifecycleOwner) {
            binding.watchlist = it.data
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