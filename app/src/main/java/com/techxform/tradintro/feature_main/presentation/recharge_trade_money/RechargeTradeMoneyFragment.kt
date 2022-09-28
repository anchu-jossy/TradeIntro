package com.techxform.tradintro.feature_main.presentation.recharge_trade_money

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.core.utils.UserDetailsSingleton
import com.techxform.tradintro.databinding.RechargeTradeMoneyFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.UpdateWalletRequest
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisibiltyGone
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisible
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import com.techxform.tradintro.feature_main.presentation.PaymentResponseActivity
import com.techxform.tradintro.feature_main.presentation.profile.UpdateProfileViewModel
import com.techxform.tradintro.feature_main.presentation.report.SpinnerAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RechargeTradeMoneyFragment :
    BaseFragment<RechargeTradeMoneyFragmentBinding>(RechargeTradeMoneyFragmentBinding::inflate) {

    companion object {
        fun newInstance() = RechargeTradeMoneyFragment()
    }


    private lateinit var viewModel: UpdateProfileViewModel
    private var userMargin: Int? = 0
    private var userId: Int? = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[UpdateProfileViewModel::class.java]
        viewModel.taxes()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private var rechargeAmount: Int? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val options = resources.getStringArray(R.array.recharge_amount_options)
        binding.rechargeTradeMoneyContainer.label1Et.adapter =
            SpinnerAdapter(requireContext(), options)
        binding.rechargeTradeMoneyContainer.label1Et.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p2 == 0) {
                        rechargeAmount = null
                        calculateNetAmount(0)
                        return
                    } else {
                        val chosen = options[p2]
                        val value = chosen.split(" ")[0]
                        rechargeAmount = (userMargin?.let {
                            (value.toInt() * 100000).div(
                                it
                            )
                        })
                        rechargeAmount?.let {

                            calculateNetAmount(it)
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(getString(R.string.recharge_lbl))
        )
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.redeem_lbl)))
        binding.redeemVoucherContainer.linearLayout.setVisibiltyGone()
        binding.rechargeTradeMoneyContainer.linearLayout.setVisible()
        //handling tab click event
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                    binding.redeemVoucherContainer.linearLayout.setVisibiltyGone()
                    binding.rechargeTradeMoneyContainer.linearLayout.setVisible()
                } else if (tab.position == 1) {
                    binding.redeemVoucherContainer.linearLayout.setVisible()
                    binding.rechargeTradeMoneyContainer.linearLayout.setVisibiltyGone()

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        clickListeners()
        observers()


        with(UserDetailsSingleton.userDetailsResponse) {
            this@RechargeTradeMoneyFragment.userId = userId
            this@RechargeTradeMoneyFragment.userMargin = userMargin

        }
        viewModel.walletSummary(null)
    }

    private fun calculateNetAmount(amount: Int) {
        if (amount == 0) {
            binding.rechargeTradeMoneyContainer.label3Et.text = ""
            binding.rechargeTradeMoneyContainer.label3Et.setVisibiltyGone()
            binding.rechargeTradeMoneyContainer.label3Tv.setVisibiltyGone()
            binding.rechargeTradeMoneyContainer.label2Et.text = ""
        } else {

            if (viewModel.taxAmount > 0) {
                val gst = (amount * viewModel.taxAmount) / 100
                val otherChargeAmount = viewModel.otherCharges
                val totalAmount = amount + gst + otherChargeAmount
                val s = "\u20B9 %.2f".format(totalAmount.toFloat())
                binding.rechargeTradeMoneyContainer.label3Et.text = s
                binding.rechargeTradeMoneyContainer.label3Et.setVisible()
                binding.rechargeTradeMoneyContainer.label3Tv.setVisible()
            } else {
                binding.rechargeTradeMoneyContainer.label3Et.setVisibiltyGone()
                binding.rechargeTradeMoneyContainer.label3Tv.setVisibiltyGone()
            }

            binding.rechargeTradeMoneyContainer.label2Et.text = amount.toString()
        }
    }

    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }
        viewModel.walletSummaryLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                val s =
                    getString(R.string.trade_money_balance_formatted).format(it.data.tradeMoneyBalance.toString())
                tvBalance.text = s
            }
        }
        //1 lakh to 10 as tradmoney ->
        //recharge amount= trademoney/margin+tax+other charges
        //show net amount.
        viewModel.updateWalletLiveData.observe(viewLifecycleOwner) {

            it.paymentLink?.let { it1 ->
                val i = PaymentResponseActivity.newIntent(
                    requireActivity(),
                    it1
                )
                resultLauncher.launch(i)
            }
        }
        viewModel.redeemVoucherLiveData.observe(viewLifecycleOwner) {
            if (it.data.voucherAmount != null) {
                binding.redeemVoucherContainer.label1Et.text?.clear()
                viewModel.walletSummary(null)
                val alert = AlertDialog.Builder(requireContext())
                alert.setMessage(
                    getString(R.string.voucher_redeemed_success_msg) +
                            getString(
                                R.string.rs_format,
                                it.data.voucherAmount
                            )
                )
                alert.setPositiveButton(
                    getString(R.string.ok)
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                alert.show()
            }
        }

        viewModel.walletErrorLiveData.observe(viewLifecycleOwner) {
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        requireContext().showShortToast(getString(R.string.no_internet_error))
                    )
                }
                else -> {
                    val errorMsg = (it as Failure.FeatureFailure).message
                    requireContext().showShortToast(errorMsg)
                }
            }
        }
    }


    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.walletSummary(null)
            binding.rechargeTradeMoneyContainer.label1Et.setSelection(0)
        }

    private fun calculation() {
        rechargeAmount?.let {
            if (it == 0) {
                Toast.makeText(
                    requireContext(),
                    "Select a valid Trade Money Value.",
                    Toast.LENGTH_SHORT
                ).show()
                return@let
            }
            val gst = (it * viewModel.taxAmount) / 100
            val otherChargeAmount = viewModel.otherCharges
            val totalAmount = it + gst + otherChargeAmount
            viewModel.updateWallet(
                UpdateWalletRequest(
                    this.userId,
                    totalAmount,
                    rechargeAmount,
                    gst,
                    otherChargeAmount
                )
            )
        } ?: run {
            Toast.makeText(
                requireContext(),
                "Select a valid Trade Money Value.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun clickListeners() {
        binding.rechargeTradeMoneyContainer.button.setOnClickListener {
            calculation()
        }
        binding.redeemVoucherContainer.button.setOnClickListener {
            if (binding.redeemVoucherContainer.label1Et.text.toString().trim().isEmpty()) {
                binding.redeemVoucherContainer.label1Et.error = "Enter voucher code"
            } else {
                viewModel.redeemVoucher(
                    binding.redeemVoucherContainer.label1Et.text.toString().trim()
                )
            }
            hideKeyboard()
        }


    }


}
