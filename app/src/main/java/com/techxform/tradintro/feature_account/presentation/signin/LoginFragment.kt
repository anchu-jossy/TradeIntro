package com.techxform.tradintro.feature_account.presentation.signin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.LoginFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.LoginRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginFragmentBinding>(LoginFragmentBinding::inflate) {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        observers()
        listeners()

    }

    private fun listeners() {
        binding.btnSignIn.setOnClickListener {
            if (binding.userNameET.text.isNullOrEmpty() && binding.passwordET.text.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.user_name_pass_required_msg),
                    Toast.LENGTH_SHORT
                ).show()
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

    }

    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.loginLiveData.observe(viewLifecycleOwner) {
            //(requireActivity().application as TradIntroApp).token = it.data.token
            findNavController().navigate(R.id.landingFragment)
        }

        viewModel.loginErrorLiveData.observe(viewLifecycleOwner) {
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.no_internet_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    )
                }
                Failure.ServerError -> {
                    (
                            Toast.makeText(
                                requireContext(), getString(R.string.server_error),
                                Toast.LENGTH_SHORT
                            ).show()
                            )
                }


                else -> {
                }
            }
        }
    }

}