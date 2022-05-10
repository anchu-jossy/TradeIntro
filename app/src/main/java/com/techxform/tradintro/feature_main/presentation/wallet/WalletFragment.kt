package com.techxform.tradintro.feature_main.presentation.wallet

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.WalletFragmentBinding

class WalletFragment : BaseFragment<WalletFragmentBinding>(WalletFragmentBinding::inflate) {

    companion object {
        fun newInstance() = WalletFragment()
    }

    private lateinit var viewModel: WalletViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[WalletViewModel::class.java]
    }

}