package com.techxform.tradintro.feature_main.presentation.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.HomeFragmentBinding

class HomeFragment : BaseFragment<HomeFragmentBinding>(HomeFragmentBinding::inflate) {


    private lateinit var viewModel: HomeViewModel



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.vm = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.portfolioRv.adapter = PortfolioAdapter(arrayListOf())
    }







}