package com.techxform.tradintro.feature_main.presentation.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.FragmentProfitLossReportBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfitLossReportFragment :
    BaseFragment<FragmentProfitLossReportBinding>(FragmentProfitLossReportBinding::inflate) {

    private lateinit var viewModel: ReportViewModel

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
            binding.titleTv.text = getString(R.string.historical_holdings_lbl)
        } else {
            binding.titleTv.text = getString(R.string.profit_loss_report_lbl)
            viewModel.summaryReport()
            val reports = resources.getStringArray(R.array.profit_loss_gain)
            binding.reportsSelectionSpinner.adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, reports)

            binding.reportsSelectionSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {


                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
        }
        viewModel.historicalReport(SearchModel(limit = 10, offset = 0))



        }




    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.portfolioLiveData.observe(viewLifecycleOwner) {
//            portfolioList = it.data
            //TODO:set adapter
            binding.stockRv.adapter = StockAdapter(it.data)

        }

        viewModel.portfolioErrorLiveData.observe(viewLifecycleOwner) {
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        Toast.makeText(
                            requireContext(), getString(R.string.no_internet_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    )
                }
                else -> {}
            }
        }
        viewModel.summaryLiveData.observe(viewLifecycleOwner){
                       if(it.data!=null){
                     //      binding.profitLossRv.adapter = ProfitLossAdapter()
                       }


        }

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