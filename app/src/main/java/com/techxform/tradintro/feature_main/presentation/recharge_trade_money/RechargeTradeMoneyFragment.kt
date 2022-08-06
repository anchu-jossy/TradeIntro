package com.techxform.tradintro.feature_main.presentation.recharge_trade_money

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.RechargeTradeMoneyFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.UpdateWalletRequest
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
        clickListeners()
        observers()
        viewModel.userDetails()
        viewModel.userDetailLiveData.observe(viewLifecycleOwner) {
            it.data?.let { data ->
                userId=data.userId
                userMargin = data.userMargin
            }
        }

        binding.rechargeTradeMoneyContainer.label2Et.doAfterTextChanged { text ->

            binding.rechargeTradeMoneyContainer.label1Et.setText(
                (userMargin?.plus(
                    text.toString().toInt()
                ).toString())
            )

        }
        binding.redeemVoucherContainer.label2Et.doAfterTextChanged { text ->

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
                val i =   PaymentResponseActivity.newIntent(requireActivity(),
                    it1
                )
                resultLauncher.launch(i)

            }
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
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        //if (result.resultCode == Activity.RESULT_OK) {
        binding.rechargeTradeMoneyContainer.label2Et.setText("0")
        //}
    }
    private fun calculation() {
        var  rechargeAmount=0
        if(!binding.rechargeTradeMoneyContainer.label2Et.text.isNullOrEmpty()){
         rechargeAmount   =  binding.rechargeTradeMoneyContainer.label2Et.text.toString().toInt()
        }
        val gst = (rechargeAmount * 18) / 100
        val otherChargeAmount = 0f
        val totalAmount = rechargeAmount + gst + otherChargeAmount
        viewModel.updateWallet(
            UpdateWalletRequest(
                userId,
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
