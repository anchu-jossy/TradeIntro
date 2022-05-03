package com.techxform.tradintro.feature_main.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.HomeFragmentBinding

class HomeFragment : BaseFragment<HomeFragmentBinding>(HomeFragmentBinding::inflate) {


    private lateinit var viewModel: HomeViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}