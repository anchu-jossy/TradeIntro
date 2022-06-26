package com.techxform.tradintro.feature_main.presentation.recharge

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.RechargeFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.presentation.PaymentResponseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RechargeSummaryFragment :
    BaseFragment<RechargeFragmentBinding>(RechargeFragmentBinding::inflate){

    companion object {
        fun newInstance() = RechargeSummaryFragment()
    }
    private lateinit var adapter: RechargeSummaryAdapter
    private lateinit var viewModel: RechargeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[RechargeViewModel::class.java]

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.trade_money)))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.voucher_code)))
        adapter = RechargeSummaryAdapter(emptyList())
        binding.rechargeRv.adapter = adapter

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                adapter = RechargeSummaryAdapter(emptyList())
                binding.rechargeRv.adapter = adapter
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


    }


}