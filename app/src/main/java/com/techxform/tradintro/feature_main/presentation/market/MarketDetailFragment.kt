package com.techxform.tradintro.feature_main.presentation.market

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.MarketDetailFragmentBinding
import com.techxform.tradintro.feature_main.domain.model.PriceType
import com.techxform.tradintro.feature_main.presentation.portfolio_view.PriceAdapter


class MarketDetailFragment :
    BaseFragment<MarketDetailFragmentBinding>(MarketDetailFragmentBinding::inflate) {

    companion object {
        fun newInstance() = MarketDetailFragment()
    }

    private lateinit var viewModel: MarketViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.priceRv.adapter = PriceAdapter(createPriceType())

        // binding.textViewBalance.text = getString(R.string.Rs) + " 00,000.00"
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MarketViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun createPriceType() : ArrayList<PriceType>
    {
        val priceTypes = arrayListOf<PriceType>()

        priceTypes.add(PriceType(4663.36, getString(R.string.open_lbl)))
        priceTypes.add(PriceType(4363.36, getString(R.string.close_lbl)))
        priceTypes.add(PriceType(6334.00, getString(R.string.volume_lbl)))
        priceTypes.add(PriceType(3728.34, getString(R.string.lower_circuit_lbl)))
        priceTypes.add(PriceType(47243.3, getString(R.string.upper_circuit_lbl)))
        priceTypes.add(PriceType(43234.34, getString(R.string.avg_traded_lbl)))

        return priceTypes

    }

}