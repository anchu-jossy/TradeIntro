package com.techxform.tradintro.feature_main.presentation.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[WatchlistViewModel::class.java]
        observers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.searchView.addTextChangedListener {
            if (binding.searchView.text.toString().isEmpty()) {
                watchList.clear()
                viewModel.watchlist(FilterModel("", limit, watchList.size, 0, ""))
            }else if (binding.searchView.text.toString().length > 3) {
                watchList.clear()
                viewModel.watchlist(FilterModel(binding.searchView.text.toString(), limit, watchList.size, 0, ""))
            }
        }


    }

    private val listener = object : WatchListAdapter.ClickListener {
        override fun onClick(watchList: WatchList, position: Int) {
            val bundle = bundleOf("watchlistId" to watchList.watchlistId)
            findNavController().navigate(R.id.action_nav_watchlist_to_watchlistViewFragment, bundle)
        }


    }

    private fun observers() {
        var position : Int =0
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.watchlistLiveData.observe(viewLifecycleOwner) {
            if (it.data.isEmpty() || it.data.size < limit)
                noMorePages = true
            watchList.addAll(it.data)
            isLoading = false
            binding.watchListRv.adapter = WatchListAdapter(watchList, listener)
            val itemTouchHelperCallback =
                object :
                    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {

                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        position = viewHolder.absoluteAdapterPosition
                        Toast.makeText(
                            requireContext(),
                            "onswiped",
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.removeWatchlist(watchList[position].watchlistId)
                    }

                }

            val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
            itemTouchHelper.attachToRecyclerView(binding.watchListRv)
            binding.watchListRv.adapter?.notifyItemRemoved(position)
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
                Failure.ServerError -> {
                    sequenceOf(
                        Toast.makeText(
                            requireContext(), "Internal Server Error",
                            Toast.LENGTH_SHORT
                        ).show()
                    )
                    viewModel.watchlist(FilterModel("", limit, watchList.size, 0, ""))

                }
                else -> {}
            }
        }
        viewModel.deleteWatchlistLiveData.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(),"Successfully Deleted",Toast.LENGTH_LONG).show()
            viewModel.watchlist(FilterModel("", limit, watchList.size, 0, ""))

        }
    }
}