package com.techxform.tradintro.feature_main.presentation.recharge

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.RechargeFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.domain.model.PaymentType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RechargeFragment : BaseFragment<RechargeFragmentBinding>(RechargeFragmentBinding::inflate) {

    companion object {
        fun newInstance() = RechargeFragment()
    }

    private lateinit var viewModel: RechargeViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[RechargeViewModel::class.java]
        observers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListeners()
        viewModel.walletSummary(PaymentType.VOUCHER)
    }

    private fun calculation() {
        val rechargeAmount = binding.rechargeAmountEt.text.toString().toFloat()
        val gst = (rechargeAmount * 18) / 100
        val otherChargeAmount = 0f
        val totalAmount = rechargeAmount + gst + otherChargeAmount
    }

    private fun clickListeners() {
        binding.rechargeBtn.setOnClickListener {
            //calculation()
        }

        binding.redeemBtn.setOnClickListener {

        }
    }

    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.walletSummaryLiveData.observe(viewLifecycleOwner) {
            binding.balanceTv.text = getString(R.string.rs_format, it.data.tradeMoneyBalance)
        }

        viewModel.walletErrorLiveData.observe(viewLifecycleOwner) {
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
    }


}