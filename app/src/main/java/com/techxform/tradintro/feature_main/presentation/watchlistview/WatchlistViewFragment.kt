package com.techxform.tradintro.feature_main.presentation.watchlistview

import android.R
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.WatchlistViewFragmentBinding

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


        val spinnerArr = arrayOf("1","2","3","4","5","6","7","8","9","10")
        val arrAdapter = ArrayAdapter(requireContext(),  R.layout.simple_spinner_item,spinnerArr)
        arrAdapter.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item)
        binding.filterSpinner.adapter = arrAdapter
    }

}