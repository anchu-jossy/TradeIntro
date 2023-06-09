package com.techxform.tradintro.feature_main.presentation.change_password

import android.os.Bundle
import android.view.View
import android.text.method.PasswordTransformationMethod
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.ChangePasswordFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.ChangePasswordRequest
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment :
    BaseFragment<ChangePasswordFragmentBinding>(ChangePasswordFragmentBinding::inflate) {
    companion object {
        const val PASSWORD_LENGTH = 6
        fun newInstance() = ChangePasswordFragment()
    }

    private lateinit var viewModel: ChangePasswordViewModel


/*    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ChangePasswordViewModel::class.java]
        binding.vm = viewModel
        observers()
        // binding.label3Et.isEnabled = false

        /*  binding.label2Et.doAfterTextChanged {
              binding.label3Et.isEnabled =
                  (binding.label2Et.text.toString().length >= PASSWORD_LENGTH)
          }

          binding.label2Et.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
              if (binding.label2Et.text.toString().length < PASSWORD_LENGTH) {
                  requireContext().showShortToast(getString(R.string.password_min_length_msg))
              }
          }

          binding.label3Et.doAfterTextChanged {
              if (!binding.label2Et.text.toString().contains(binding.label3Et.text.toString(), false))
                  requireContext().showShortToast(getString(R.string.confirm_password_not_matched))

          }*/
        binding.button.setOnClickListener {
            if (binding.label1Et.text!!.trim().isEmpty()) {
                binding.label1Et.error = getString(R.string.confirm_password_no_password)
                //  requireContext().showShortToast(getString(R.string.confirm_password_no_password))
            } else if (binding.label2Et.text!!.trim().isEmpty()) {
                binding.label2Et.error = getString(R.string.confirm_password_no_new_password)
                //  requireContext().showShortToast(getString(R.string.confirm_password_no_new_password))
            } else if (binding.label2Et.text.toString().length < PASSWORD_LENGTH) {
                binding.label2Et.error = getString(R.string.password_min_length_msg)
                //   requireContext().showShortToast(getString(R.string.password_min_length_msg))
            } else if (!binding.label2Et.text.toString()
                    .contains(binding.label3Et.text.toString(), false)
            ) {
                binding.label3Et.error = getString(R.string.confirm_password_not_matched)
                // requireContext().showShortToast(getString(R.string.confirm_password_not_matched))
            } else {
                hideKeyboard()
                viewModel.changePassword(
                    ChangePasswordRequest(
                        binding.label1Et.text.toString(),
                        binding.label2Et.text.toString()
                    )
                )
            }
        }


        binding.showHidePassIv.setOnCheckedChangeListener { _, b ->
            binding.label2Et.transformationMethod = if (b) {
                null
            } else {
                PasswordTransformationMethod()
            }
        }

        binding.showHideConfirmPassIv.setOnCheckedChangeListener { compoundButton, b ->
            binding.label3Et.transformationMethod = if (b) {
                null
            } else {
                PasswordTransformationMethod()
            }
        }
    }


    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }
        viewModel.changePasswordLiveData.observe(viewLifecycleOwner) {
            binding.label1Et.text?.clear()
            binding.label2Et.text?.clear()
            binding.label3Et.text?.clear()
            requireContext().showShortToast(getString(R.string.password_changed_msg))
        }

        viewModel.changePasswordErrorLiveData.observe(viewLifecycleOwner) {
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        requireContext().showShortToast(getString(R.string.no_internet_error))
                    )
                }
                Failure.ServerError -> {
                    requireContext().showShortToast(getString(R.string.server_error))
                }
                else -> {
                    requireContext().showShortToast(
                        "Failed to change password. wrong current password."
                    )
                }
            }
        }
    }


}