package com.techxform.tradintro.feature_main.presentation.wallet

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.WalletFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.domain.model.PaymentType
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletFragment : BaseFragment<WalletFragmentBinding>(WalletFragmentBinding::inflate) {

    companion object {
        fun newInstance() = WalletFragment()
    }

    private lateinit var viewModel: WalletViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[WalletViewModel::class.java]
        viewModel.walletSummary(PaymentType.VOUCHER)
        viewModel.walletSummaryLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                balanceLbl.text = it.data.balance.toString()
                lastAllocationAmountTv.text = it.data.tradeMoneyBalance.toString()
                lastAllocationDate.text = it.data.lastAllocatedOn

            }
        }
        viewModel.portfolioErrorLiveData.observe(viewLifecycleOwner)
        {
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        requireContext().showShortToast( getString(R.string.no_internet_error))

                    )
                }
                Failure.ServerError -> {
                    requireContext().showShortToast(getString(R.string.server_error))

                }
                else -> {
                    val errorMsg = (it as Failure.FeatureFailure).message
                    requireContext().showShortToast("Error: $errorMsg")
                }
            }
        }
    }

}