package com.techxform.tradintro.feature_main.presentation.recharge_trade_money

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techxform.tradintro.R

class RechargeTradeMoneyFragment : Fragment() {

    companion object {
        fun newInstance() = RechargeTradeMoneyFragment()
    }

    private lateinit var viewModel: RechargeTradeMoneyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recharge_trade_money_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RechargeTradeMoneyViewModel::class.java)
        // TODO: Use the ViewModel
    }

}