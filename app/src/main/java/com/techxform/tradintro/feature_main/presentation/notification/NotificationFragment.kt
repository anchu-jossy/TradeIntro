package com.techxform.tradintro.feature_main.presentation.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.NotificationFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.Notifications
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisibiltyGone
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisible
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment :
    BaseFragment<NotificationFragmentBinding>(NotificationFragmentBinding::inflate) {

    companion object {
        fun newInstance() = NotificationFragment()
        const val ALERT_TYPE = "alert"
        const val NEWS_TYPE = "news"
        const val NOTIFICATION_TYPE = "notification_type"


        fun navBundle(notificationType: String) = bundleOf(NOTIFICATION_TYPE to notificationType)
    }


    private lateinit var viewModel: NotificationViewModel
    private lateinit var adapter: NotificationAdapter
    private var notificationType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[NotificationViewModel::class.java]

        notificationType = requireArguments().getString(
            NOTIFICATION_TYPE
        )

        observers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.notifications(SearchModel("", 10, null,0, 0, type = notificationType ?: ""))
    }

    private val listener = object : NotificationAdapter.OnClickListener {

        override fun onItemClick(position: Int, notifications: Notifications) {
            findNavController().navigate(
                R.id.detailedNotificationFragment,
                DetailedNotificationFragment.navBundle(notifications)
            )
        }

        override fun onDeleteClick(position: Int, notifications: Notifications) {
            viewModel.deleteNotifications(position, notifications.notificationId)
        }

    }


    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.notificationLiveData.observe(viewLifecycleOwner) {
            if (it.data.isEmpty()) {
                binding.tvNodata.setVisible()
            } else {
                binding.tvNodata.setVisibiltyGone()
                adapter = NotificationAdapter(it.data, listener)
                binding.notificationRv.adapter = adapter
            }
        }
        viewModel.deleteNotificationLiveData.observe(viewLifecycleOwner) {
            adapter.list.removeAt(it)
            adapter.notifyItemRemoved(it)
            requireContext().showShortToast(                getString(R.string.notification_delete_msg),
            )

        }

        viewModel.notificationErrorLiveData.observe(viewLifecycleOwner) {
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        requireContext().showShortToast( getString(R.string.no_internet_error))

                    )
                }
                else -> {}
            }
        }
    }

}