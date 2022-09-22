package com.techxform.tradintro.feature_main.presentation.portfolio

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.core.utils.ScreenType
import com.techxform.tradintro.databinding.PortfolisFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.PortfolioItem
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisibiltyGone
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisible
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import com.techxform.tradintro.feature_main.presentation.equality_place_order.EqualityPlaceOrderFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PortfoliosFragment :
    BaseFragment<PortfolisFragmentBinding>(PortfolisFragmentBinding::inflate),
    View.OnClickListener {


    private lateinit var viewModel: PortfolisViewModel
    private var portfolioList: ArrayList<PortfolioItem> = arrayListOf()

    private var adapter: PortfolioAdapter? = null

    private val limit = 10
    private var isLoading = false
    private var noMorePages = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[PortfolisViewModel::class.java]
        observers()
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
                portfolioList.clear()
                viewModel.portfolioListV2(
                    SearchModel(
                        binding.searchView.text.toString().trim(),
                        limit,
                        viewModel.getSelectedPortfolio()?.orderStockId?.toString(),
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
                        viewModel.getSelectedPortfolio()?.orderStockId?.toString(),
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
                                viewModel.getSelectedPortfolio()?.orderStockId?.toString(),
                                portfolioList.size,
                                0
                            ), totalItemCount>8
                        )
                    }

                }
            }

        })

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,onBackPressedCallback
           )


        reloadScreen(viewModel.isStockSelected(), viewModel.getSelectedPortfolio(), true);
        binding.sellBtn.setOnClickListener(this)
        binding.buyBtn.setOnClickListener(this)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (getFragment() is PortfoliosFragment) {
                if (viewModel.getSelectedPortfolio() != null) {
                    viewModel.setSelectedPortfolioItem(null)
                    reloadScreen()
                } else findNavController().navigateUp()
            } else findNavController().navigateUp()
        }
    }

    private fun clearSearchView() {
        binding.searchView.removeTextChangedListener(searchTextListener)
        binding.searchView.text.clear()
        binding.searchView.addTextChangedListener(searchTextListener)
    }


    private val rvListener = object : PortfolioAdapter.ClickListener {
        override fun onItemClick(portfolioItem: PortfolioItem, position: Int) {
            if (viewModel.getSelectedPortfolio() == null) {
                noMorePages = false
                prevSearchTerm = ""
                clearSearchView()
                reloadScreen(true, portfolioItem)
            } else {
                portfolioList.clear()
                viewModel.clearPortfolioList()
                val bundle = bundleOf(
                    "orderId" to portfolioItem.orderId,
                    "StockDashboard" to binding.stockDashboard,
                    "PortfolioItem" to portfolioItem
                )
                findNavController().navigate(R.id.action_nav_home_to_portfolioViewFragment, bundle)
            }

        }

    }

    private fun observers() {
        viewModel.portfolioLiveData.observe(viewLifecycleOwner) {
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
        viewModel.portfolioDashboardOfStockLiveData.observe(viewLifecycleOwner) {
            binding.stockDashboard = it.data
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


    private fun reloadScreen(
        isStockSelected: Boolean = false,
        selectedPortfolio: PortfolioItem? = null, isFirstTime: Boolean = false
    ) {
        binding.showPortfolioStockDashboard = isStockSelected;
        binding.searchView.visibility = if (isStockSelected) View.GONE else View.VISIBLE
        binding.lvBtn.visibility = if (isStockSelected) View.VISIBLE else View.GONE

        binding.portfolioLbl.setText(
            if (isStockSelected) R.string.transaction_list_lbl else
                R.string.portfolio_list_lbl
        )

        viewModel.setSelectedPortfolioItem(selectedPortfolio)
        adapter = null
        if (isStockSelected && selectedPortfolio != null) {
            viewModel.portfolioDashboardOfStock(selectedPortfolio.orderStockId)
        } else {
            viewModel.portfolioDashboardV2()
        }
        portfolioList.clear()
        if (portfolioList.isEmpty())
            viewModel.portfolioListV2(
                SearchModel(
                    "", limit,
                    viewModel.getSelectedPortfolio()?.orderStockId?.toString(),
                    portfolioList.size, 0
                ), false
            )
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.buyBtn -> {
                viewModel.getSelectedPortfolio()?.let {
                    findNavController().navigate(
                        R.id.equalityPlaceOrderFragment, EqualityPlaceOrderFragment.navBundle(
                            it.market.stockId,
                            EqualityPlaceOrderFragment.BUY, ScreenType.PORTFOLIO, it.market
                        )
                    )
                }
            }
            R.id.sellBtn -> {
                viewModel.getSelectedPortfolio()?.let {
                    findNavController().navigate(
                        R.id.equalityPlaceOrderFragment, EqualityPlaceOrderFragment.navBundle(
                            it.market.stockId,
                            EqualityPlaceOrderFragment.SELL,
                            ScreenType.PORTFOLIO,
                            it.market
                        )
                    )
                }
            }
        }
    }

    private fun handleError(failure: Failure)
    {
        when (failure) {
            Failure.NetworkConnection -> {
                sequenceOf(
                    requireContext().showShortToast(getString(R.string.no_internet_error))

                )
            }
            Failure.ServerError-> {
                requireContext().showShortToast(getString(R.string.server_error))

            }
            else -> {
                val errorMsg = (failure as Failure.FeatureFailure).message
                requireContext().showShortToast("Error: $errorMsg")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//unregister listener here
        onBackPressedCallback.isEnabled = false
        onBackPressedCallback.remove()
    }

}