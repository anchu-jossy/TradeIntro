package com.techxform.tradintro.feature_main.presentation.notification

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private var adapter: NotificationAdapter? = null
    private var notificationType: String? = null
    private val limit = 10
    private var skipLoading = false
    private var isLoading = false
    private var noMorePages = false
    private var notificationList: ArrayList<Notifications> = arrayListOf()

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

    private var prevSearchTerm: String = ""
    private lateinit var searchTextListener: TextWatcher;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchTextListener = binding.searchView.addTextChangedListener {
            if (binding.searchView.text.toString().isNotEmpty()) {
                prevSearchTerm = binding.searchView.text.toString()
                notificationList.clear()
                viewModel.notifications(
                    SearchModel(
                        binding.searchView.text.toString().trim(),
                        limit,
                        null,
                        0,
                        0
                    )
                )
                //binding.searchView.isEnabled = false
            } else if (binding.searchView.text.isNullOrEmpty() && prevSearchTerm.isNotEmpty()) {
                prevSearchTerm = ""
                notificationList.clear()
                viewModel.notifications(
                    SearchModel(
                        null, limit,
                        null,
                        0, 0
                    )
                )
                binding.searchView.isEnabled = false
            }
        }


        binding.notificationRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = binding.notificationRv.layoutManager?.itemCount
                val lastVisibleItem =
                    (binding.notificationRv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                if (totalItemCount != null) {
                    if (!isLoading && totalItemCount <= (lastVisibleItem + 5) && !noMorePages) {
                        isLoading = true
                        skipLoading = false;
                        viewModel.notifications(
                            SearchModel(
                                binding.searchView.text.toString().trim(),
                                limit,
                                null,
                                notificationList.size,
                                0
                            )
                        )
                    }

                }
            }

        })
        if (!skipLoading)
            viewModel.notifications(SearchModel("", 10, null, 0, 0, type = notificationType ?: ""))
    }

    private fun clearSearchView() {
        binding.searchView.removeTextChangedListener(searchTextListener)
        binding.searchView.text?.clear()
        binding.searchView.addTextChangedListener(searchTextListener)
    }

    private val listener = object : NotificationAdapter.OnClickListener {

        override fun onItemClick(position: Int, notifications: Notifications) {
            noMorePages = false
            adapter = null;
            skipLoading = true
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
            binding.searchView.isEnabled = true
            if (it.data.isEmpty() || it.data.size < limit) {
                noMorePages = true
            }
            notificationList.addAll(it.data)
            setAdapter()
            isLoading = false
            viewModel.dismissLoading()
        }
        viewModel.deleteNotificationLiveData.observe(viewLifecycleOwner) {
            adapter?.list?.removeAt(it)
            adapter?.notifyItemRemoved(it)
            requireContext().showShortToast(
                getString(R.string.notification_delete_msg),
            )
        }

        viewModel.notificationErrorLiveData.observe(viewLifecycleOwner) {
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

    private fun setAdapter() {
        if (notificationList.isEmpty()) {
            binding.tvNodata.setVisible()
            binding.notificationRv.setVisibiltyGone()
        } else if (notificationList.isNotEmpty() && binding.notificationRv.visibility == View.GONE) {
            binding.tvNodata.setVisible()
            binding.notificationRv.setVisibiltyGone()
        }

        if (adapter == null) {
            adapter = NotificationAdapter(notificationList, listener)
            binding.notificationRv.adapter = adapter
        } else {
            adapter?.list = notificationList
            adapter?.notifyDataSetChanged()
        }
    }

}