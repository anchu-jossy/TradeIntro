package com.techxform.tradintro.feature_main.presentation.trade

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.R

class TradeFragment : Fragment() {

    companion object {
        fun newInstance() = TradeFragment()
    }

    private lateinit var viewModel: TradeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.trade_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TradeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}