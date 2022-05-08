package com.techxform.tradintro.feature_main.presentation.change_password

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techxform.tradintro.R
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