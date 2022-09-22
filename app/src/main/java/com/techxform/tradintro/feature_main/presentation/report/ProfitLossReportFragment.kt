package com.techxform.tradintro.feature_main.presentation.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.FragmentProfitLossReportBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.SummaryReport
import com.techxform.tradintro.feature_main.data.remote.dto.TotalPrices
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfitLossReportFragment :
    BaseFragment<FragmentProfitLossReportBinding>(FragmentProfitLossReportBinding::inflate) {

    private lateinit var viewModel: ReportViewModel

    private var type : @TYPE Int = HISTORICAL

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[ReportViewModel::class.java]
        observers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isHistorical = requireArguments().getBoolean(IS_HISTORICAL)
        binding.profitLossRv.isVisible = !isHistorical
        if (isHistorical) {
            type = HISTORICAL
            binding.reportsSelectionSpinner.isVisible = false
            binding.titleTv.text = getString(R.string.historical_holdings_lbl)
            viewModel.historicalReport(SearchModel(limit = 10, offset = 0))
        } else {
            binding.titleTv.text = getString(R.string.profit_loss_report_lbl)
            viewModel.summaryReport()
            val reports = resources.getStringArray(R.array.profit_loss_gain)
            binding.reportsSelectionSpinner.adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, reports)

            binding.reportsSelectionSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        when (reports[p2]) {
                            getString(R.string.realised_gain) -> {
                                type = REALISED_GAIN
                                viewModel.historicalReport(SearchModel(limit = 10, offset = 0))
                            }
                            getString(R.string.unrealised_gain) -> {
                                type = UNREALISED_GAIN
                                viewModel.reportCurrent(SearchModel(limit = 10, offset = 0))
                            }
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }


        }
    }

    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.portfolioLiveData.observe(viewLifecycleOwner) {
            binding.stockRv.adapter = StockAdapter(type, it.data)
        }

        viewModel.portfolioErrorLiveData.observe(viewLifecycleOwner) {
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        requireContext().showShortToast(getString(R.string.no_internet_error))

                    )
                }
                else -> {}
            }
        }
        viewModel.summaryLiveData.observe(viewLifecycleOwner) {
            if (it.data != null) {
                binding.profitLossRv.adapter = ProfitLossAdapter(createList(it.data))
            }
        }

    }

    private fun createList(summaryReport: SummaryReport): ArrayList<TotalPrices> {
        var list = ArrayList<TotalPrices>()
        list.add(
            TotalPrices(
                ContextCompat.getColor(requireContext(), R.color.dark_pink),
                summaryReport.realizedProfitLossValue?: 0f,
                getString(R.string.total_realised_profit_loss_lbl),
                getString(R.string.realised_profit_loss_lbl1)
            )
        )
        list.add(
            TotalPrices(
                ContextCompat.getColor(requireContext(), R.color.light_blue_900),
                summaryReport.totalInvestmentValue?: 0f,
                getString(R.string.total_investment_value_lbl),
                getString(R.string.total_investment_lbl)
            )
        )
        list.add(
            TotalPrices(
                ContextCompat.getColor(requireContext(), R.color.dark_pink),
                summaryReport.unRealizedProfitLossValue?: 0f,
                getString(R.string.total_unrealised_profit_loss_lbl),
                getString(R.string.unrealised_profit_loss_lbl)
            )
        )
        return list
    }

    companion object {

        private const val IS_HISTORICAL = "is_historical"

        @Target(AnnotationTarget.TYPE)
        @IntDef(HISTORICAL, REALISED_GAIN, UNREALISED_GAIN)
        @Retention(AnnotationRetention.SOURCE)
        annotation class TYPE

        const val HISTORICAL = 0
        const val REALISED_GAIN = 1
        const val UNREALISED_GAIN = 2

        @JvmStatic
        fun newInstance(isHistorical: Boolean) =
            ProfitLossReportFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_HISTORICAL, isHistorical)
                }
            }
    }
}