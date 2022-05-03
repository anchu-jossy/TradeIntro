package com.techxform.tradintro.feature_main.presentation.watchlist

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.WatchlistFragmentBinding

class WatchlistFragment :
    BaseFragment<WatchlistFragmentBinding>(WatchlistFragmentBinding::inflate) {

    companion object {
        fun newInstance() = WatchlistFragment()
    }

    private lateinit var viewModel: WatchlistViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WatchlistViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.watchListRv.adapter = WatchListAdapter(arrayListOf())

    }
}