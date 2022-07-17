package com.techxform.tradintro.feature_main.presentation.recharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.RechargeFragmentBinding
import com.techxform.tradintro.feature_main.domain.model.PaymentType
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class RechargeSummaryFragment :
    BaseFragment<RechargeFragmentBinding>(RechargeFragmentBinding::inflate){

    companion object {
        const val LIMIT = 10
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
        observer()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.trade_money)))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.voucher_code)))

        viewModel.walletHistory(SearchModel(type = PaymentType.RECHARGE.name.lowercase(), limit = LIMIT))
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position)
                {
                    0 ->  viewModel.walletHistory(SearchModel(type=PaymentType.RECHARGE.name.lowercase(
                        Locale.getDefault()), limit= LIMIT))
                    1 ->  viewModel.walletHistory(SearchModel(type = PaymentType.VOUCHER.name.lowercase(Locale.getDefault()), limit=LIMIT))
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


    }

    private fun observer()
    {
        viewModel.walletHistoryLiveData.observe(viewLifecycleOwner) {
            adapter = RechargeSummaryAdapter(it)
            adapter.setSelectionType(binding.tabLayout.selectedTabPosition)
            binding.rechargeRv.adapter = adapter
        }

    }

}