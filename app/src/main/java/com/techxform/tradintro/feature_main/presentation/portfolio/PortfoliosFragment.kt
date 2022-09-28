package com.techxform.tradintro.feature_main.presentation.portfolio

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.PortfolisFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.PortfolioItem
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisibiltyGone
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisible
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PortfoliosFragment :
    BaseFragment<PortfolisFragmentBinding>(PortfolisFragmentBinding::inflate) {


    private val viewModel: PortfoliosViewModel by activityViewModels()
    private var portfolioList: ArrayList<PortfolioItem> = arrayListOf()

    private var adapter: PortfolioAdapter? = null

    private val limit = 10
    private var isLoading = false
    private var noMorePages = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        portfolioList.clear()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private var prevSearchTerm: String = ""
    private lateinit var searchTextListener: TextWatcher;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        searchTextListener = binding.searchView.addTextChangedListener {
            if (binding.searchView.text.toString().isNotEmpty()) {
                prevSearchTerm = binding.searchView.text.toString()
                clearItemList()
                viewModel.portfolioListV2(
                    SearchModel(
                        binding.searchView.text.toString().trim(),
                        limit,
                        null,
                        0,
                        0
                    ), true
                )
                //binding.searchView.isEnabled = false
            } else if (binding.searchView.text.isNullOrEmpty() && prevSearchTerm.isNotEmpty()) {
                prevSearchTerm = ""
                portfolioList.clear()
                viewModel.portfolioListV2(
                    SearchModel(
                        null, limit,
                        null,
                        0, 0
                    ), true
                )
                binding.searchView.isEnabled = false
            }
        }


        binding.portfolioRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = binding.portfolioRv.layoutManager?.itemCount
                val lastVisibleItem =
                    (binding.portfolioRv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                if (totalItemCount != null) {
                    if (!isLoading && totalItemCount <= (lastVisibleItem + 5) && !noMorePages) {
                        isLoading = true
                        viewModel.portfolioListV2(
                            SearchModel(
                                binding.searchView.text.toString().trim(),
                                limit,
                                null,
                                portfolioList.size,
                                0
                            ), totalItemCount > 8
                        )
                    }

                }
            }

        })
        reloadScreen()
        setAdapter()
        observers()
    }

    private fun reloadScreen() {
        binding.showPortfolioStockDashboard = false;
        binding.searchView.visibility = View.VISIBLE
        binding.lvBtn.visibility = View.GONE

        binding.portfolioLbl.setText(
            R.string.portfolio_list_lbl
        )

        viewModel.setSelectedPortfolioItem(null)

        adapter = null
        viewModel.portfolioDashboardV2()
        //portfolioList.clear()
        if (portfolioList.isEmpty())
            viewModel.portfolioListV2(
                SearchModel(
                    "", limit,
                    null,
                    portfolioList.size, 0
                ), false
            )
    }


    private fun clearSearchView() {
        binding.searchView.removeTextChangedListener(searchTextListener)
        binding.searchView.text.clear()
        binding.searchView.addTextChangedListener(searchTextListener)
    }


    private val rvListener = object : PortfolioAdapter.ClickListener {
        override fun onItemClick(portfolioItem: PortfolioItem, position: Int) {
            prevSearchTerm = ""
            clearSearchView()
            viewModel.setSelectedPortfolioItem(portfolioItem)
            viewModel.clearTransactionList()
            findNavController().navigate(R.id.action_nav_to_portfolioTransactionFragment)
        }
    }

    private fun observers() {
        viewModel.portfolioLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                binding.searchView.isEnabled = true
                if (it.data.isEmpty() || it.data.size < limit) {
                    if (it.data.isEmpty() && viewModel.isStockSelected()) {
                        // viewModel.dismissLoading()
                        return@observe
                    }
                    noMorePages = true
                }
                portfolioList.addAll(it.data)
                setAdapter()
                isLoading = false
                viewModel.dismissLoading()
            }
        }
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
            binding.portfolioInProgress.progressOverlay.isVisible = false
        }
        viewModel.loadingSearchLiveData.observe(viewLifecycleOwner) {
            binding.portfolioInProgress.progressOverlay.isVisible = it
            if (it)
                binding.noPortfoliosTv.setVisibiltyGone()
        }



        viewModel.portfolioDashboardLiveData.observe(viewLifecycleOwner) {
            binding.portfolioDashboard = it.data
        }

        viewModel.portfolioErrorLiveData.observe(viewLifecycleOwner) {
            isLoading = false
            binding.searchView.isEnabled = true
            handleError(it)
            viewModel.dismissLoading()
        }
        viewModel.portfolioDashboardErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
            viewModel.dismissLoading()
        }

    }


    private fun setAdapter() {
        if (portfolioList.isEmpty()) {
            binding.noPortfoliosTv.setVisible()
            binding.portfolioRv.setVisibiltyGone()
        } else if (portfolioList.isNotEmpty() && binding.portfolioRv.visibility == View.GONE) {
            binding.portfolioRv.setVisible()
            binding.noPortfoliosTv.setVisibiltyGone()
        }

        if (adapter == null) {
            adapter = PortfolioAdapter(portfolioList, viewModel.isStockSelected(), rvListener)
            binding.portfolioRv.adapter = adapter
        } else {
            adapter?.list = portfolioList
            adapter?.notifyDataSetChanged()
        }
    }

    private fun clearItemList() {
        portfolioList.clear()
        adapter?.let {
            it.list.clear()
            it.notifyDataSetChanged()
        }
    }


    private fun handleError(failure: Failure) {
        when (failure) {
            Failure.NetworkConnection -> {
                sequenceOf(
                    requireContext().showShortToast(getString(R.string.no_internet_error))

                )
            }
            Failure.ServerError -> {
                requireContext().showShortToast(getString(R.string.server_error))

            }
            else -> {
                val errorMsg = (failure as Failure.FeatureFailure).message
                requireContext().showShortToast("Error: $errorMsg")
            }
        }
    }

}