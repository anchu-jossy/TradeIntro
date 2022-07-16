package com.techxform.tradintro.feature_main.presentation.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.RegistrationFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : BaseFragment<RegistrationFragmentBinding>(RegistrationFragmentBinding::inflate) {

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    private lateinit var viewModel: RegistrationViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]

    }



}