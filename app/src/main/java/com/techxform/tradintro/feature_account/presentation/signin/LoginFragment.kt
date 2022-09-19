package com.techxform.tradintro.feature_account.presentation.signin

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.LoginFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.LoginRequest
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import com.techxform.tradintro.feature_main.presentation.SplashScreenActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginFragmentBinding>(LoginFragmentBinding::inflate) {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private var isNotification: Boolean = false
    private var notificationType: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        observers()
        listeners()
        
        viewModel.loginRequestFromPref(requireContext()) {
            requireActivity().runOnUiThread {
                binding.userNameET.setText(it.username)
                binding.passwordET.setText(it.password)
            }
        }
    }

    private fun forgetPasswordDialog() {
        val alert = AlertDialog.Builder(requireContext())
        val edittext = EditText(requireContext())
        edittext.maxLines = 1;
        edittext.setSingleLine()
        alert.setMessage(getString(R.string.enter_reg_emailid))
        alert.setView(edittext)

        alert.setPositiveButton(
            "SEND"
        ) { dialog, _ ->
            dialog.dismiss()
            viewModel.forgetPassword(edittext.text.toString().trim())
        }
        alert.setNegativeButton(
            "CANCEL"
        ) { dialog, _ ->
            dialog.dismiss()
        }
        alert.show()
    }

    private fun listeners() {
        binding.btnSignIn.setOnClickListener {
            hideKeyboard()
            if (binding.userNameET.text.isNullOrEmpty() || binding.passwordET.text.isNullOrEmpty()) {
                requireContext().showShortToast(getString(R.string.user_name_pass_required_msg))
                return@setOnClickListener
            }
            viewModel.login(
                LoginRequest(
                    binding.userNameET.text.toString().trim(),
                    binding.passwordET.text.toString().trim()
                ),
                requireContext()
                //TradSharedPreference.createGetPreference(requireContext())
            )
        }
        binding.signUpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        binding.tvForgotPassword.setOnClickListener {
            forgetPasswordDialog()
        }

    }

    fun isNotification(isNotification: Boolean, notificationType: String) {
        this.isNotification = isNotification
        this.notificationType = notificationType
    }


    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }
        viewModel.forgetPasswordLiveData.observe(viewLifecycleOwner) {
            if (it.toString().contains(getString(R.string.success)))
                sequenceOf(
                    requireContext().showShortToast(getString(R.string.verify_email))
                )
            else
                sequenceOf(
                    requireContext().showShortToast(getString(R.string.verify_email_error))
                )
        }
        viewModel.resendEmailLiveData.observe(viewLifecycleOwner) {
            if (it.toString().contains(getString(R.string.success)))
                sequenceOf(
                    requireContext().showShortToast(getString(R.string.verify_account))
                )
            else
                sequenceOf(
                    requireContext().showShortToast(getString(R.string.verify_account_fail))
                )
        }

        viewModel.loginLiveData.observe(viewLifecycleOwner) {
            val b = Bundle()
            b.putBoolean(SplashScreenActivity.IS_NOTIFICATION, isNotification)
            b.putString(SplashScreenActivity.NOTIFICATION_TYPE, notificationType)
            findNavController().navigate(R.id.landingFragment, b)
        }

        viewModel.loginErrorLiveData.observe(viewLifecycleOwner) {
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        requireContext().showShortToast(
                            getString(R.string.no_internet_error)
                        )
                    )
                }
                Failure.ServerError -> {

                    requireContext().showShortToast(
                        getString(R.string.server_error)
                    )


                }

                else -> {
                    if ((it as Failure.FeatureFailure).message == "VerifyEmail") {
                        showVerifyDialog();
                    } else
                        requireContext().showShortToast(
                            it.message
                        )


                }
            }
        }

        viewModel.forgetPasswordErrorLiveData.observe(viewLifecycleOwner) {
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        requireContext().showShortToast(
                            getString(R.string.no_internet_error)
                        )
                    )
                }
                Failure.ServerError -> {

                    requireContext().showShortToast(
                        getString(R.string.server_error)
                    )


                }
            }
        }

        viewModel.resentEmailErrorLiveData.observe(viewLifecycleOwner) {
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        requireContext().showShortToast(
                            getString(R.string.no_internet_error)
                        )
                    )
                }
                Failure.ServerError -> {

                    requireContext().showShortToast(
                        getString(R.string.server_error)
                    )


                }
            }
        }

    }

    private fun showVerifyDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Verify Email Id")
        //set message for alert dialog
        builder.setMessage(
            "Error! Your email id has not been verified. " +
                    "Please verify and try again. To sent verification mail again choose Resend."
        )
        builder.setIcon(android.R.drawable.ic_dialog_email)
        builder.setPositiveButton("Resend") { dialogInterface, _ ->
            dialogInterface.dismiss()
            viewModel.resentEmail(binding.userNameET.text.toString().trim())
        }
        //performing negative action
        builder.setNegativeButton("Cancel") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

}