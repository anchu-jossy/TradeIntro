package com.techxform.tradintro.feature_main.presentation.change_password

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.ChangePasswordFragmentBinding

class ChangePasswordFragment :
    BaseFragment<ChangePasswordFragmentBinding>(ChangePasswordFragmentBinding::inflate) {

    companion object {
        fun newInstance() = ChangePasswordFragment()
    }

    private lateinit var viewModel: ChangePasswordViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[ChangePasswordViewModel::class.java]

        binding.vm = viewModel
    }

}