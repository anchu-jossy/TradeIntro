package com.techxform.tradintro.feature_main.presentation.recharge

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.RechargeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RechargeFragment : BaseFragment<RechargeFragmentBinding>(RechargeFragmentBinding::inflate) {

    companion object {
        fun newInstance() = RechargeFragment()
    }

    private lateinit var viewModel: RechargeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[RechargeViewModel::class.java]
        observers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun observers() {
       /* viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.userDashboardLiveData.observe(viewLifecycleOwner) {
            binding.userDashboard = it.data
        }

        viewModel.userDashboardErrorLiveData.observe(viewLifecycleOwner) {
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        Toast.makeText(
                            requireContext(), getString(R.string.no_internet_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    )
                }
                else -> {}
            }
        }*/
    }



}