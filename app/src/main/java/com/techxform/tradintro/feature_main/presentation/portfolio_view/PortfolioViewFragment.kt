package com.techxform.tradintro.feature_main.presentation.portfolio_view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.PortfolioViewFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.domain.model.PriceType
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PortfolioViewFragment : BaseFragment<PortfolioViewFragmentBinding>(PortfolioViewFragmentBinding::inflate) {

    companion object {
        fun newInstance() = PortfolioViewFragment()
    }

    private lateinit var viewModel: PortfolioViewViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[PortfolioViewViewModel::class.java]

        val spinnerArr = arrayOf("1","2","3","4","5","6","7","8","9","10")

        val arrAdapter = ArrayAdapter(requireContext(),  android.R.layout.simple_spinner_item,spinnerArr)

        arrAdapter.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item)
        binding.filterSpinner.adapter = arrAdapter


        binding.priceRv.adapter = PriceAdapter(createPriceType())

    }

    private fun createPriceType() : ArrayList<PriceType>
    {
        val priceTypes = arrayListOf<PriceType>()

        priceTypes.add(PriceType(4663.36, getString(R.string.current_price_lbl)))
        priceTypes.add(PriceType(4363.36, getString(R.string.avg_purchase_price_lbl)))
        priceTypes.add(PriceType(6334.00, getString(R.string.quantity_lbl)))
        priceTypes.add(PriceType(3728.34, getString(R.string.total_price_lbl)))
        priceTypes.add(PriceType(47243.3, getString(R.string.total_value_lbl)))
        priceTypes.add(PriceType(43234.34, getString(R.string.alert_price_lbl)))
        priceTypes.add(PriceType(2823.45, getString(R.string.gain_loss_lbl)))
        priceTypes.add(PriceType(474.36, getString(R.string.per_gain_loss_lbl)))

        return priceTypes

    }







}