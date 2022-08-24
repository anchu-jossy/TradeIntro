package com.techxform.tradintro.feature_main.presentation.recharge_trade_money

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
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

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(getString(R.string.recharge_lbl))
        );
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

        binding.rechargeTradeMoneyContainer.label2Et.doAfterTextChanged { text ->
            if (text.isNullOrEmpty()) {
                binding.rechargeTradeMoneyContainer.label1Et.setText("0")
            } else
                binding.rechargeTradeMoneyContainer.label1Et.setText(
                    (userMargin?.plus(
                        text.toString().toInt()
                    ).toString())
                )

        }
        binding.redeemVoucherContainer.label2Et.doAfterTextChanged { text ->
            if (text.isNullOrEmpty()) {
                binding.redeemVoucherContainer.label1Et.setText("0")
            } else
                binding.redeemVoucherContainer.label1Et.setText(
                    (userMargin?.plus(
                        text.toString().toInt()
                    ).toString())
                )


        }
    }

    private fun observers() {


        viewModel.updateWalletLiveData.observe(viewLifecycleOwner) {

            it.paymentLink?.let { it1 ->
                val i = PaymentResponseActivity.newIntent(
                    requireActivity(),
                    it1
                )
                resultLauncher.launch(i)

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
                }
            }
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            //if (result.resultCode == Activity.RESULT_OK) {
            binding.rechargeTradeMoneyContainer.label2Et.setText("0")
            //}
        }

    private fun calculation() {
        var rechargeAmount = 0
        if (!binding.rechargeTradeMoneyContainer.label2Et.text.isNullOrEmpty()) {
            rechargeAmount = binding.rechargeTradeMoneyContainer.label2Et.text.toString().toInt()
        }
        val gst = (rechargeAmount * 18) / 100
        val otherChargeAmount = 0f
        val totalAmount = rechargeAmount + gst + otherChargeAmount
        viewModel.updateWallet(
            UpdateWalletRequest(
                this.userId,
                totalAmount,
                rechargeAmount,
                gst,
                otherChargeAmount
            )
        )

    }

    private fun clickListeners() {
        binding.rechargeTradeMoneyContainer.button.setOnClickListener {
            calculation()

        }
        binding.redeemVoucherContainer.button.setOnClickListener {
            calculation()
//            viewModel.updateWallet(
//                UpdateWalletRequest(
//                    walletSummaryResponse.userId,
//                    totalAmount,
//                    rechargeAmount,
//                    gst,
//                    otherChargeAmount
//                )
//            )
        }


    }


}
