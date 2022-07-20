package com.techxform.tradintro.feature_main.presentation.report

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.ReportFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportFragment : BaseFragment<ReportFragmentBinding>(ReportFragmentBinding::inflate) {

    companion object {
        fun newInstance() = ReportFragment()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reports = resources.getStringArray(R.array.reports)
        binding.reportsSelectionSpinner.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, reports)

        binding.reportsSelectionSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    when (reports[p2]) {
                        getString(R.string.trade_book_lbl) -> {
                            childFragmentManager.beginTransaction().replace(
                                R.id.spinnerContainer,
                                OrderBookFragment.newInstance(true)
                            ).addToBackStack(null).commit()
                        }
                        getString(R.string.order_book_lbl) -> {
                            childFragmentManager.beginTransaction().replace(
                                R.id.spinnerContainer,
                                OrderBookFragment.newInstance(false)
                            ).addToBackStack(null).commit()
                        }
                        getString(R.string.historical_holdings_lbl) -> {

                            childFragmentManager.beginTransaction().replace(
                                R.id.spinnerContainer,
                                ProfitLossReportFragment.newInstance(true)
                            ).addToBackStack(null).commit()
                        }
                        getString(R.string.profit_loss_report_lbl) -> {
                            childFragmentManager.beginTransaction().replace(
                                R.id.spinnerContainer,
                                ProfitLossReportFragment.newInstance(false)
                            ).addToBackStack(null).commit()

                        }

                    }


                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
    }


}