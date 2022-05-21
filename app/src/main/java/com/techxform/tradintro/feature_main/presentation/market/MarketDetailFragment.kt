package com.techxform.tradintro.feature_main.presentation.market

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.MarketDetailFragmentBinding
import com.techxform.tradintro.feature_main.domain.model.PriceType
import com.techxform.tradintro.feature_main.presentation.portfolio_view.PriceAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarketDetailFragment :
    BaseFragment<MarketDetailFragmentBinding>(MarketDetailFragmentBinding::inflate) {

    //private val viewModel: MarketViewModel by activityViewModels()
    companion object {
        fun newInstance() = MarketDetailFragment()
    }

    private lateinit var viewModel: MarketViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MarketViewModel::class.java]
        //binding.vm = viewModel
        binding.priceRv.adapter = PriceAdapter(createPriceType())

        // binding.textViewBalance.text = getString(R.string.Rs) + " 00,000.00"
    }



    private fun createPriceType() : ArrayList<PriceType>
    {
        val priceTypes = arrayListOf<PriceType>()

        priceTypes.add(PriceType(4663.36, getString(R.string.open_lbl)))
        priceTypes.add(PriceType(4363.36, getString(R.string.close_lbl)))
        priceTypes.add(PriceType(3728.34, getString(R.string.lower_circuit_lbl)))
        priceTypes.add(PriceType(47243.3, getString(R.string.upper_circuit_lbl)))

        return priceTypes

    }

}