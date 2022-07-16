package com.techxform.tradintro.feature_main.presentation.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.RegistrationFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.RegisterRequest
import com.techxform.tradintro.feature_main.presentation.SplashScreenActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment :
    BaseFragment<RegistrationFragmentBinding>(RegistrationFragmentBinding::inflate) {

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    private lateinit var viewModel: RegistrationViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]
        observers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tncTv.movementMethod = LinkMovementMethod.getInstance();
        binding.registerBtn.setOnClickListener {
            if (validation()) {
                val name = binding.fullNameEt.text.toString().split(" ")
                val fName = name[0]
                var lName: String? = null
                if (name.size > 1)
                    lName = name[1]
                viewModel.register(
                    RegisterRequest(
                        fName,
                        lName,
                        binding.emailEt.text.toString(),
                        binding.passwordEt.text.toString()
                    )
                )

            }
        }

    }


    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }
        viewModel.registerLiveData.observe(viewLifecycleOwner){
           findNavController().popBackStack()
        }



        viewModel.registerErrorLiveData.observe(viewLifecycleOwner) {
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


    private fun validation(): Boolean {
        var validation = true
        if (binding.fullNameEt.text.toString().isNullOrEmpty()) {
            validation = false
            binding.fullNameEt.error = getString(R.string.enter_user_name)
        }
        if (binding.emailEt.text.toString().isNullOrEmpty()) {
            validation = false
            binding.emailEt.error = getString(R.string.enter_emailid)
        }
        if (binding.passwordEt.text.toString().isNullOrEmpty()) {
            validation = false
            binding.passwordEt.error = getString(R.string.enter_password)
        }

        if (binding.confirmPassEt.text.toString().isNullOrEmpty()) {
            validation = false
            binding.confirmPassEt.error = getString(R.string.enter_confirm_password)
        } else if (binding.passwordEt.text.toString() != binding.confirmPassEt.text.toString()) {
            validation = false
            binding.confirmPassEt.error = getString(R.string.password_not_match_msg)
        }

        return validation
    }


}