package com.techxform.tradintro.feature_main.presentation.report

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.FragmentProfitLossReportBinding


class ProfitLossReportFragment :
    BaseFragment<FragmentProfitLossReportBinding>(FragmentProfitLossReportBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isHistorical = requireArguments().getBoolean(IS_HISTORICAL)
        binding.profitLossRv.isVisible = !isHistorical
        if (isHistorical)
            binding.titleTv.text = getString(R.string.historical_holdings_lbl)
        else binding.titleTv.text = getString(R.string.profit_loss_report_lbl)

        binding.profitLossRv.adapter = ProfitLossAdapter(arrayListOf())
        binding.stockRv.adapter = StockAdapter(arrayListOf())
    }

    companion object {

        private const val IS_HISTORICAL = "is_historical"

        @JvmStatic
        fun newInstance(isHistorical: Boolean) =
            ProfitLossReportFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_HISTORICAL, isHistorical)
                }
            }
    }
}