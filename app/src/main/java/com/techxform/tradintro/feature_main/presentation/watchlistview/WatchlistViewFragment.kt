package com.techxform.tradintro.feature_main.presentation.watchlistview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.WatchlistViewFragmentBinding
import com.techxform.tradintro.feature_main.domain.model.PriceType

class WatchlistViewFragment : BaseFragment<WatchlistViewFragmentBinding>(WatchlistViewFragmentBinding::inflate) {

    companion object {
        fun newInstance() = WatchlistViewFragment()
    }

    private lateinit var viewModel: WatchlistViewViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[WatchlistViewViewModel::class.java]

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.alertPriceType = PriceType(63.2f, getString(R.string.alert_price_lbl))

        val spinnerArr = arrayOf("1","2","3","4","5","6","7","8","9","10")
        val arrAdapter = ArrayAdapter(requireContext(),  android.R.layout.simple_spinner_item,spinnerArr)
        arrAdapter.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item)
        binding.filterSpinner.adapter = arrAdapter
    }

}