package com.techxform.tradintro.feature_main.presentation.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.DetailedNotificationFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.Notifications
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailedNotificationFragment : BaseFragment<DetailedNotificationFragmentBinding>(
    DetailedNotificationFragmentBinding::inflate
) {

    private lateinit var viewModel: DetailedNotificationViewModel

    companion object {
        const val NOTIFICATION: String = "notifications"
        fun navBundle(notifications: Notifications) = bundleOf(NOTIFICATION to notifications)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[DetailedNotificationViewModel::class.java]
        observers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notifications = requireArguments().getParcelable<Notifications>(NOTIFICATION)
        binding.notification = notifications
        if(notifications?.notificationType == 0)
            notifications.notificationId?.let { viewModel.readNotification(it) }

    }

    fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.readNotificationLiveData.observe(viewLifecycleOwner) {
            requireContext().showShortToast(getString(R.string.success))
        }

        viewModel.readNotificationErrorLiveData.observe(viewLifecycleOwner) {
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        requireContext().showShortToast(getString(R.string.no_internet_error))

                    )
                }
                else -> {}
            }
        }

    }

}