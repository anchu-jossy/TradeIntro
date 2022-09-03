package com.techxform.tradintro.feature_main.presentation.change_password

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.ChangePasswordFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.ChangePasswordRequest
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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[ChangePasswordViewModel::class.java]
        binding.vm = viewModel

        observers()
        binding.label3Et.isEnabled = false

        binding.label2Et.doAfterTextChanged {
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

        }
        binding.button.setOnClickListener {
            if (!binding.label2Et.text.toString().contains(binding.label3Et.text.toString(), false))
                requireContext().showShortToast(getString(R.string.confirm_password_not_matched))
            else {
                hideKeyboard()
                viewModel.changePassword(
                    ChangePasswordRequest(
                        binding.label1Et.text.toString(),
                        binding.label2Et.text.toString()
                    )
                )
            }
        }


        binding.showHidePassIv.setOnCheckedChangeListener { compoundButton, b ->
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
        viewModel.changePasswordLiveData.observe(viewLifecycleOwner) {
            requireContext().showShortToast(getString(R.string.password_changed_msg))
        }
    }

}