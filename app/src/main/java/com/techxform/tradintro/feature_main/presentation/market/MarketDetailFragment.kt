package com.techxform.tradintro.feature_main.presentation.market

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.MarketDetailFragmentBinding


class MarketDetailFragment :
    BaseFragment<MarketDetailFragmentBinding>(MarketDetailFragmentBinding::inflate) {

    companion object {
        fun newInstance() = MarketDetailFragment()
    }

    private lateinit var viewModel: MarketViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewBalance.text = getString(R.string.Rs) + " 00,000.00"
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MarketViewModel::class.java)
        // TODO: Use the ViewModel
    }

}