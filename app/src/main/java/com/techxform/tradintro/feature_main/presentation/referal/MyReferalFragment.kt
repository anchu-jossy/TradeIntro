package com.techxform.tradintro.feature_main.presentation.referal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.MyReferalFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.AddUserRequest
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.presentation.notification.NotificationAdapter
import com.techxform.tradintro.feature_main.presentation.recharge.MyReferalListAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyReferalFragment :
    BaseFragment<MyReferalFragmentBinding>(MyReferalFragmentBinding::inflate) {

    companion object {
        fun newInstance() = MyReferalFragment()
    }
    private lateinit var adapter: MyReferalListAdapter
    private lateinit var viewModel: MyReferalViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[MyReferalViewModel::class.java]
        observers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
        viewModel.getUserInviteList()
    }


    private fun clickListeners() {
        with(binding.rechargeTradeMoneyContainer) {
            button.setOnClickListener {
                viewModel.addUser(
                    AddUserRequest(
                        10,
                        binding.rechargeTradeMoneyContainer.label1Et.text.toString(),
                        binding.rechargeTradeMoneyContainer.label2Et.text.toString()
                    )
                )
            }
        }

    }

    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.addUseLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                it.status,
                Toast.LENGTH_SHORT
            ).show()
            viewModel.getUserInviteList()
        }

        viewModel.walletErrorLiveData.observe(viewLifecycleOwner) {
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        Toast.makeText(
                            requireContext(), getString(R.string.no_internet_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    )
                }
                Failure.ServerError ->
                    sequenceOf(
                        Toast.makeText(
                            requireContext(), getString(R.string.server_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    )
                else -> {
                }
            }
        }
        viewModel.updateUserListLiveData.observe(viewLifecycleOwner){
            if(it.data.isNotEmpty()) {
                adapter = MyReferalListAdapter(it.data)
                binding.listRv.adapter = adapter
            }

        }
    }
}