package com.techxform.tradintro.feature_main.presentation.recharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.RechargeFragmentBinding
import com.techxform.tradintro.feature_main.domain.model.PaymentType
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisibiltyGone
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisible
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
            if (it.isNotEmpty()) {
                binding.titleTv.text = getString(R.string.transaction_details_lbl)
                binding.title1Tv.text = getString(R.string.trade_money_lbl)
                binding.tvNodata.setVisibiltyGone()
            } else binding.tvNodata.setVisible()
            adapter = RechargeSummaryAdapter(it)
            binding.rechargeRv.adapter = adapter
        }

    }

}