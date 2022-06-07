package com.techxform.tradintro.feature_main.presentation.notification

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.DetailedNotificationFragmentBinding
import com.techxform.tradintro.databinding.NotificationFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Notifications
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailedNotificationFragment : BaseFragment<DetailedNotificationFragmentBinding>(
    DetailedNotificationFragmentBinding::inflate) {

    companion object {
        const val NOTIFICATION:String = "notifications"
        fun navBundle(notifications: Notifications) = bundleOf( NOTIFICATION to notifications)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notifications = requireArguments().getParcelable<Notifications>(NOTIFICATION)
        binding.notification = notifications

    }

}