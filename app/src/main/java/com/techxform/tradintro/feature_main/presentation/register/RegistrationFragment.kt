package com.techxform.tradintro.feature_main.presentation.register

import android.app.AlertDialog
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.RegistrationFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.RegisterRequest
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import com.techxform.tradintro.feature_main.presentation.change_password.ChangePasswordFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment :
    BaseFragment<RegistrationFragmentBinding>(RegistrationFragmentBinding::inflate) {

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    private lateinit var viewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]
        observers()

        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signInBtn.setOnClickListener {
            findNavController().navigate(R.id.action_to_LoginFragment)
        }

        binding.tncTv.movementMethod = LinkMovementMethod.getInstance()
        binding.registerBtn.setOnClickListener {
            if (validation()) {
                val name = binding.fullNameEt.text.toString().trim().split(" ")
                val fName = name[0]
                var lName: String? = null
                if (name.size > 1)
                    lName = name[1]
                viewModel.register(
                    RegisterRequest(
                        fName.trim(),
                        lName?.trim(),
                        binding.emailEt.text.toString().trim(),
                        binding.passwordEt.text.toString().trim()
                    )
                )

            }
        }

    }

    private fun registrationDialog() {
        val alert = AlertDialog.Builder(requireContext())
        alert.setMessage(getString(R.string.check_email))
        alert.setPositiveButton(
            getString(R.string.ok)
        ) { dialog, _ ->
            dialog.dismiss()
            findNavController().popBackStack()

        }
        alert.show()
    }

    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }
        viewModel.registerLiveData.observe(viewLifecycleOwner) {
            it.status?.let {
                when (it) {
                    "success" -> {
                        registrationDialog()
                    }
                    "failed" -> {
                        requireContext().showShortToast("Sorry! User registration failed, Please try again.")
                    }
                    else -> {
                        requireContext().showShortToast("Alert! User already exists.")
                    }
                }

            }

        }

        viewModel.registerErrorLiveData.observe(viewLifecycleOwner) {
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
                    val errorMsg = (it as Failure.FeatureFailure).message
                    requireContext().showShortToast(errorMsg)
                }
            }
        }
    }


    private fun validation(): Boolean {
        var validation = true
        if (binding.fullNameEt.text.toString().isEmpty()) {
            validation = false
            binding.fullNameEt.error = getString(R.string.enter_user_name)

        } else
            if (binding.emailEt.text.toString().isEmpty()) {
                validation = false
                binding.emailEt.error = getString(R.string.enter_emailid)
            } else if (binding.passwordEt.text.toString().isEmpty()) {
                validation = false
                binding.passwordEt.error = getString(R.string.enter_password)
            } else if (binding.passwordEt.text.toString().length < ChangePasswordFragment.PASSWORD_LENGTH) {
                validation = false
                binding.passwordEt.error = getString(R.string.password_min_length_msg)
            } else if (binding.confirmPassEt.text.toString().isEmpty()) {
                validation = false
                binding.confirmPassEt.error = getString(R.string.enter_confirm_password)
            } else if (binding.passwordEt.text.toString() != binding.confirmPassEt.text.toString()) {
                validation = false
                binding.confirmPassEt.error = getString(R.string.password_not_match_msg)
            }

        return validation
    }


}