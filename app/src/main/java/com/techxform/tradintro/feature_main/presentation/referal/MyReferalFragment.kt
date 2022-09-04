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
import com.techxform.tradintro.core.utils.PreferenceHelper
import com.techxform.tradintro.core.utils.PreferenceHelper.userId
import com.techxform.tradintro.databinding.MyReferalFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.AddUserRequest
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
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
                with(binding.rechargeTradeMoneyContainer){
                    if(label1Et.text.isNullOrEmpty() || label2Et.text.isNullOrEmpty())
                        requireContext().showShortToast(getString(R.string.validate_fields))
                       else viewModel.addUser(
                            AddUserRequest(
                                PreferenceHelper.customPreference(requireContext()).userId,
                                binding.rechargeTradeMoneyContainer.label1Et.text.toString().trim(),
                                binding.rechargeTradeMoneyContainer.label2Et.text.toString().trim()
                            )
                        )
                }

            }
        }

    }

    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }
        viewModel.addUseLiveData.observe(viewLifecycleOwner) {
            it.status?.let{
                when(it){
                    "success"->{
                        binding.rechargeTradeMoneyContainer.label1Et.text?.clear()
                        binding.rechargeTradeMoneyContainer.label2Et.text?.clear()
                        requireContext().showShortToast("Success! User is invited.")
                    }
                    "failed"->{
                        requireContext().showShortToast("Sorry! User invite failed, Please try again..")
                    }
                    else->{
                        requireContext().showShortToast("Sorry, user is already invited.")
                    }
                }

            }
            viewModel.getUserInviteList()
        }

        viewModel.walletErrorLiveData.observe(viewLifecycleOwner) {
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        requireContext().showShortToast(getString(R.string.no_internet_error))

                    )
                }
                Failure.ServerError ->
                    sequenceOf(
                        requireContext().showShortToast(getString(R.string.server_error))
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