package com.techxform.tradintro.feature_main.presentation.recharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.RechargeFragmentBinding
import com.techxform.tradintro.feature_main.domain.model.PaymentType
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import dagger.hilt.android.AndroidEntryPoint


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
        viewModel.walletHistory(SearchModel(type = PaymentType.RECHARGE.name.lowercase(), limit = LIMIT))

    }

    private fun observer()
    {
        viewModel.walletHistoryLiveData.observe(viewLifecycleOwner) {
            adapter = RechargeSummaryAdapter(it)
            binding.rechargeRv.adapter = adapter
        }

    }

}