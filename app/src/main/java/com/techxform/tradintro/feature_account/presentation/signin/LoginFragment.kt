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
    }

    private fun forgetPasswordDialog() {
        val alert = AlertDialog.Builder(requireContext())
        val edittext = EditText(requireContext())
        alert.setMessage(getString(R.string.enter_emailid))
        alert.setView(edittext)
        alert.setPositiveButton(
            getString(R.string.ok)
        ) { dialog, _ ->
            dialog.dismiss()
            viewModel.forgetPassword(edittext.text.toString())
        }
        alert.show()
    }

    private fun listeners() {
        binding.btnSignIn.setOnClickListener {
            if (binding.userNameET.text.isNullOrEmpty() || binding.passwordET.text.isNullOrEmpty()) {
                requireContext().showShortToast(getString(R.string.user_name_pass_required_msg))
                return@setOnClickListener
            }
            viewModel.login(
                LoginRequest(
                    binding.userNameET.text.toString(),
                    binding.passwordET.text.toString()
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
                    requireContext().showShortToast(getString(R.string.error)))


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
                    requireContext().showShortToast(
                        (it as Failure.FeatureFailure).message
                    )


                }
            }
        }

    }

}