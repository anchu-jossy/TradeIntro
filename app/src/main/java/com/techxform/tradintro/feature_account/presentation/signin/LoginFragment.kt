package com.techxform.tradintro.feature_account.presentation.signin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.LoginFragmentBinding
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

    private fun listeners(){
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
                )
            )
        }

    }

    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }

        viewModel.loginLiveData.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.landingFragment)
        }
    }

}