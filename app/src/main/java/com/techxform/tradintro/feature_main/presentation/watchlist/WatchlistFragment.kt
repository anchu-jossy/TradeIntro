package com.techxform.tradintro.feature_main.presentation.watchlist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.WatchlistFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.Stock
import com.techxform.tradintro.feature_main.data.remote.dto.WatchList
import com.techxform.tradintro.feature_main.domain.model.FilterModel
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import com.techxform.tradintro.feature_main.presentation.notification.NotificationAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.security.acl.Group

@AndroidEntryPoint
class WatchlistFragment :
    BaseFragment<WatchlistFragmentBinding>(WatchlistFragmentBinding::inflate) {

    companion object {
        fun newInstance() = WatchlistFragment()
    }

    private lateinit var viewModel: WatchlistViewModel
    private val limit = 10
    private var isLoading = false
    private var noMorePages = false
    private var watchList: ArrayList<WatchList> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[WatchlistViewModel::class.java]

        observers()
        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (binding.nestedScrollView.getChildAt(0) != null && (binding.nestedScrollView.getChildAt(
                    0
                ).bottom
                        <= (binding.nestedScrollView.height + binding.nestedScrollView.scrollY))
            ) {
                if (!isLoading && !noMorePages) {
                    isLoading = true
                    viewModel.watchlist(FilterModel("", limit, watchList.size, 0, ""))
                }
            }
        })
        watchList.clear()
        viewModel.watchlist(FilterModel("", limit, watchList.size, 0, ""))
    }

    private val listener = object : WatchListAdapter.ClickListener {
        override fun onClick(watchList: WatchList, position: Int) {
            val bundle = bundleOf("watchlistId" to watchList.watchlistId)
            findNavController().navigate(R.id.action_nav_watchlist_to_watchlistViewFragment, bundle)
        }

    }

    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.watchlistLiveData.observe(viewLifecycleOwner) {
            if (it.data.isEmpty() || it.data.size < limit)
                noMorePages = true
            watchList.addAll(it.data)
            isLoading = false
            binding.watchListRv.adapter = WatchListAdapter(watchList, listener)
        }

        viewModel.watchlistErrorLiveData.observe(viewLifecycleOwner) {
            isLoading = false
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