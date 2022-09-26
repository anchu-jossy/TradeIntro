package com.techxform.tradintro.feature_main.presentation.watchlist

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.WatchlistFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.WatchList
import com.techxform.tradintro.feature_main.domain.model.FilterModel
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


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
    private var isSearch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[WatchlistViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
                viewModel.watchlist(FilterModel("", limit, 0, 0, ""))

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
        viewModel.watchlist(FilterModel("", limit, watchList.size, 0, ""))


    }

    private val listener = object : WatchListAdapter.ClickListener {
        override fun onClick(watchList: WatchList, position: Int) {
            val bundle = bundleOf("watchlistId" to watchList.watchlistId)
            findNavController().navigate(R.id.action_nav_watchlist_to_watchlistViewFragment, bundle)
        }


    }

    private fun observers() {
        var position: Int = 0
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.watchlistLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                if (it.data.isEmpty() || it.data.size < limit)
                    noMorePages = true
                watchList = it.data
                isLoading = false
                binding.watchListRv.adapter = WatchListAdapter(watchList, listener)
                val itemTouchHelperCallback =
                    object :
                        ItemTouchHelper.SimpleCallback(
                            0,
                            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                        ) {
                        override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
                        ): Boolean {
Toast.makeText(requireContext(),"testing",Toast.LENGTH_LONG).show()
                            return false
                        }

                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                            showDeleteDialog(viewHolder.absoluteAdapterPosition)
                        }

                        override fun onChildDraw(
                            c: Canvas,
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            dX: Float,
                            dY: Float,
                            actionState: Int,
                            isCurrentlyActive: Boolean
                        ) {
                            RecyclerViewSwipeDecorator.Builder(
                                c,
                                recyclerView,
                                viewHolder,
                                dX,
                                dY,
                                actionState,
                                isCurrentlyActive
                            )
                                .addBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.red
                                    )
                                )
                                .addActionIcon(R.drawable.ic_delete)
                                .create()
                                .decorate()
                            super.onChildDraw(
                                c,
                                recyclerView,
                                viewHolder,
                                dX,
                                dY,
                                actionState,
                                isCurrentlyActive
                            )
                        }

                    }

                val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
                itemTouchHelper.attachToRecyclerView(binding.watchListRv)
                binding.watchListRv.adapter?.notifyItemRemoved(position)

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
                    viewModel.watchlist(FilterModel("", limit, watchList.size, 0, ""))

                }
                else -> {
                }
            }
        }
        viewModel.deleteWatchlistLiveData.observe(viewLifecycleOwner) {
            requireContext().showShortToast(getString(R.string.delete_success))
            viewModel.watchlist(FilterModel("", limit, 0, 0, ""))

        }
    }
    private fun showDeleteDialog(absoluteAdapterPosition: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Are you sure do you want to delete the item")
        //set message for alert dialog

        builder.setIcon(android.R.drawable.ic_menu_delete)
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            dialogInterface.dismiss()

            viewModel.removeWatchlist(watchList[absoluteAdapterPosition].watchlistId)
        }
        //performing negative action
        builder.setNegativeButton("Not now") { dialogInterface, _ ->
            dialogInterface.dismiss()
            viewModel.watchlist(FilterModel("", limit, 0, 0, ""))

        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }
}