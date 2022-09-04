package com.techxform.tradintro.feature_main.presentation.change_password

import android.os.Bundle
import android.text.InputType
import android.view.View
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.ChangePasswordFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.ChangePasswordRequest
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment :
    BaseFragment<ChangePasswordFragmentBinding>(ChangePasswordFragmentBinding::inflate) {
    companion object {
        fun newInstance() = ChangePasswordFragment()
    }

    private lateinit var viewModel: ChangePasswordViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rechargeTradeMoneyContainer.label1Et.inputType= InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD


        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD.apply {
            binding.rechargeTradeMoneyContainer.label1Et.inputType =
                this
            binding.rechargeTradeMoneyContainer.label2Et.inputType =this
            binding.rechargeTradeMoneyContainer.label3Et.inputType=this
        }
        binding.rechargeTradeMoneyContainer.button.setOnClickListener {
        val  currentPass=  binding.rechargeTradeMoneyContainer.label1Et.text.toString()
            val  newPass=  binding.rechargeTradeMoneyContainer.label3Et.text.toString()

            viewModel.changePassword(ChangePasswordRequest(currentPass,newPass))

        }

        viewModel.changePasswordLiveData.observe(viewLifecycleOwner) {
//            if (it.status) {
//                requireContext().showShortToast("Logout successfully")
//                requireActivity().finish()
//            }
        }
    }


}