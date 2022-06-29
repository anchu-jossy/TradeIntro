package com.techxform.tradintro.feature_main.presentation.recharge_trade_money

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.RechargeTradeMoneyFragmentBinding
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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[UpdateProfileViewModel::class.java]
        observers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListeners()
        viewModel.userDetails()
        viewModel.userDetailLiveData.observe(viewLifecycleOwner) {
            it.data?.let { data ->
                userMargin = it.data.userMargin
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

    private fun clickListeners() {
        binding.rechargeTradeMoneyContainer.button.setOnClickListener {

         //   rechargeCalculation()
        }
        binding.redeemVoucherContainer.button.setOnClickListener {

        }


    }

    private fun observers() {

    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            //if (result.resultCode == Activity.RESULT_OK) {
            binding.redeemVoucherContainer.label1Et.setText("")
            binding.rechargeTradeMoneyContainer.label1Et.setText("")
            //}
        }
}
