package com.techxform.tradintro.feature_main.presentation.watchlist


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.WatchlistFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.WatchList
import com.techxform.tradintro.feature_main.domain.model.FilterModel
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import com.techxform.tradintro.feature_main.presentation.utils.SwipeAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WatchlistFragment :
    BaseFragment<WatchlistFragmentBinding>(WatchlistFragmentBinding::inflate) {

    companion object {
        fun newInstance() = WatchlistFragment()
    }


    private lateinit var viewModel: WatchlistViewModel
    private val limit = 10
    private var isLoading = false
    private var previousDeletedWatchlistPosition: Int? = null
    private var noMorePages = false
    private var watchList: ArrayList<WatchList> = arrayListOf()
    private var isSearch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[WatchlistViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        observers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvAddWatchlist.setOnClickListener {
            findNavController().navigate(R.id.nav_market)
        }



        binding.searchView.addTextChangedListener {
            if (binding.searchView.text.toString().isNotEmpty()) {
                isSearch = true
                watchList.clear()
                viewModel.watchlist(
                    FilterModel(
                        binding.searchView.text.toString().trim(),
                        limit,
                        0,
                        0,
                        ""
                    )
                )

            } else if (binding.searchView.text.isNullOrEmpty() && isSearch) {
                isSearch = false
                watchList.clear()
                viewModel.watchlist(FilterModel(null, limit, 0, 0, ""))

            }
        }


        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (binding.nestedScrollView.getChildAt(0) != null && (binding.nestedScrollView.getChildAt(
                    0
                ).bottom
                        <= (binding.nestedScrollView.height + binding.nestedScrollView.scrollY))
            ) {
                if (!isLoading && !noMorePages) {
                    isLoading = true
                    viewModel.watchlist(
                        FilterModel(
                            binding.searchView.text.toString().trim(),
                            limit,
                            watchList.size,
                            0,
                            ""
                        )
                    )
                }
            }
        })
        watchList.clear()
        viewModel.watchlist(FilterModel(null, limit, watchList.size, 0, ""))
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
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                if (it.data.isEmpty() || it.data.size < limit)
                    noMorePages = true
                watchList = it.data
                isLoading = false
                setupAdaptor(watchList)
            }
        }
        viewModel.watchlistErrorLiveData.observe(viewLifecycleOwner) {
            isLoading = false
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        requireContext().showShortToast(getString(R.string.no_internet_error))

                    )
                }
                Failure.ServerError -> {
                    sequenceOf(
                        requireContext().showShortToast(getString(R.string.internal_server_error))

                    )
                }
                else -> {
                    val errorMsg = (it as Failure.FeatureFailure).message
                    requireContext().showShortToast("Error: $errorMsg")
                }

            }
        }
        viewModel.deleteWatchlistLiveData.observe(viewLifecycleOwner) {
            requireContext().showShortToast(getString(R.string.removed_to_watchlist))
            previousDeletedWatchlistPosition?.let { it1 ->
                    watchList.removeAt(it1)
                    binding.watchListRv.adapter?.notifyItemRemoved(it1)
            }
            previousDeletedWatchlistPosition=null
            // viewModel.watchlist(FilterModel("", limit, 0, 0, ""))
        }


    }


    private fun setupAdaptor(_watchList: ArrayList<WatchList>) {
        //binding.watchListRv.adapter = WatchListAdapter(_watchList, listener)
        binding.watchListRv.adapter = SwipeAdapter(WatchListItem { action, item, pos ->
            when (action) {
                WatchListItem.Action.SELECT -> listener.onClick(item, pos)
                WatchListItem.Action.DELETE -> showDeleteConformation(item, pos)
            }
            Log.e("action", "$action.name $pos.toString()")

        }, _watchList)
    }

    private fun showDeleteConformation(item: WatchList, pos: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.delete_watch_list_ttl)
        builder.setMessage(R.string.delete_watch_list_desc)
        builder.setIcon(android.R.drawable.stat_sys_warning)
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            dialogInterface.dismiss()
            previousDeletedWatchlistPosition = pos
            viewModel.removeWatchlist(item.watchlistId ?: 0)
        }
        //performing negative action
        builder.setNegativeButton("Not now") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

}