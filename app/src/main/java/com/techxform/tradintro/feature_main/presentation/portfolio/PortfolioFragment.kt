package com.techxform.tradintro.feature_main.presentation.portfolio

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment

class PortfolioFragment : BaseFragment() {

    companion object {
        fun newInstance() = PortfolioFragment()
    }

    private lateinit var viewModel: PortfolioViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.portfolio_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PortfolioViewModel::class.java)
        // TODO: Use the ViewModel
    }

}