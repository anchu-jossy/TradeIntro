package com.techxform.tradintro.feature_main.presentation.recharge_trade_money

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.RechargeTradeMoneyFragmentBinding

class RechargeTradeMoneyFragment :
    BaseFragment<RechargeTradeMoneyFragmentBinding>(RechargeTradeMoneyFragmentBinding::inflate) {

    companion object {
        fun newInstance() = RechargeTradeMoneyFragment()
    }

    private lateinit var viewModel: RechargeTradeMoneyViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RechargeTradeMoneyViewModel::class.java)
        binding.vm = viewModel
    }

}