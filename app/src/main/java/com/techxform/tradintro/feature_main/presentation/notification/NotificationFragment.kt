package com.techxform.tradintro.feature_main.presentation.notification

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.NotificationFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseFragment<NotificationFragmentBinding>(NotificationFragmentBinding::inflate) {

    companion object {
        fun newInstance() = NotificationFragment()
    }

    private lateinit var viewModel: NotificationViewModel



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[NotificationViewModel::class.java]

        observers()
        viewModel.notifications(SearchModel("", 10,0,0))
    }

    private val listener = object : NotificationAdapter.OnClickListener{

        override fun onItemClick(position: Int) {
            findNavController().navigate(R.id.action_notificationFragment_to_detailedNotificationFragment)

        }

        override fun onDeleteClick(position: Int) {
            //TODO:delete
        }

    }


    private fun observers()
    {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.notificationLiveData.observe(viewLifecycleOwner) {
            binding.notificationRv.adapter = NotificationAdapter(it.data,listener)

        }

        viewModel.notificationErrorLiveData.observe(viewLifecycleOwner) {
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
        }
    }

}