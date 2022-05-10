package com.techxform.tradintro.feature_main.presentation.notification

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.NotificationFragmentBinding

class NotificationFragment : BaseFragment<NotificationFragmentBinding>(NotificationFragmentBinding::inflate) {

    companion object {
        fun newInstance() = NotificationFragment()
    }

    private lateinit var viewModel: NotificationViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[NotificationViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.notificationRv.adapter = NotificationAdapter(arrayListOf(),listener)
    }

    private val listener = object : NotificationAdapter.OnClickListener{

        override fun onItemClick(position: Int) {
            findNavController().navigate(R.id.action_notificationFragment_to_detailedNotificationFragment)

        }

        override fun onDeleteClick(position: Int) {
            //TODO:delete
        }

    }

}