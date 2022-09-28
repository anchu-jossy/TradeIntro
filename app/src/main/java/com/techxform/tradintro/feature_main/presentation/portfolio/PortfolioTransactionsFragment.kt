package com.techxform.tradintro.feature_main.presentation.portfolio

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.core.utils.ScreenType
import com.techxform.tradintro.databinding.PortfolisFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.AlertPriceRequest
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.PortfolioItem
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisibiltyGone
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisible
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import com.techxform.tradintro.feature_main.presentation.equality_place_order.EqualityPlaceOrderFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PortfolioTransactionsFragment :
    BaseFragment<PortfolisFragmentBinding>(PortfolisFragmentBinding::inflate),
    View.OnClickListener {


    private val viewModel: PortfoliosViewModel by activityViewModels()
    private var portfolioList: ArrayList<PortfolioItem> = arrayListOf()
    private var lastKnownAlertPrice: Float = 0.0f
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
        observers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        binding.portfolioRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = binding.portfolioRv.layoutManager?.itemCount
                val lastVisibleItem =
                    (binding.portfolioRv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (totalItemCount != null) {
                    if (!isLoading && totalItemCount <= (lastVisibleItem + 5) && !noMorePages) {
                        isLoading = true
                        viewModel.portfolioListV2Transaction(
                            SearchModel(
                                binding.searchView.text.toString().trim(),
                                limit,
                                viewModel.getSelectedPortfolio()?.orderStockId?.toString(),
                                portfolioList.size,
                                0
                            ), totalItemCount > 8
                        )
                    }

                }
            }

        })




        reloadScreen(viewModel.getSelectedPortfolio()!!, true);
        binding.sellBtn.setOnClickListener(this)
        binding.buyBtn.setOnClickListener(this)

        binding.setAlertPriceBtn.setOnClickListener {
            alertPriceSetDialog()
        }
    }

    private fun alertPriceSetDialog() {

        val builder = AlertDialog.Builder(requireContext())
        val amountEt = EditText(requireContext())
        amountEt.hint = getString(R.string.alert_price_lbl)

        val container = LinearLayout(requireContext())
        container.orientation = LinearLayout.VERTICAL
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(20, 0, 20, 0)

        amountEt.layoutParams = lp
        //amountEt.gravity = Gravity.TOP or Gravity.LEFT
        amountEt.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
        amountEt.setLines(1)
        amountEt.maxLines = 1
        container.addView(amountEt, lp)

        builder.setView(container)
        viewModel.getSelectedPortfolio()?.let {
            it.alert?.let { alert ->
                builder.setTitle(R.string.portfolio_modify_alert_price_lbl)
                amountEt.setText(alert.notificationPrice.toString())
                builder.setNegativeButton(
                    R.string.remove_alert_lbl
                ) { dialog, _ ->
                    viewModel.deleteAlertPrice(
                        notificationId = alert.notificationId
                    )
                    dialog.dismiss()
                }
            } ?: run {
                builder.setTitle(R.string.add_portfolio_alert_price_lbl)
            }
            builder.setPositiveButton(
                if (it.alert == null) R.string.add_alert_lbl else R.string.modify_alert_lbl
            ) { dialog, _ ->
                if (amountEt.text.toString().isEmpty()) {
                    amountEt.error = (getString(R.string.enter_alert_price_lbl))
                    return@setPositiveButton
                } else {
                    lastKnownAlertPrice = amountEt.text.toString().toFloat()
                    viewModel.modifyPortfolioAlertPrice(
                        it.orderId,
                        AlertPriceRequest(amountEt.text.toString().toDouble())
                    )

                    dialog.dismiss()
                }
            }
            builder.show()
        }
    }


    private val rvListener = object : PortfolioAdapter.ClickListener {
        override fun onItemClick(portfolioItem: PortfolioItem, position: Int) {

            portfolioList.clear()
            viewModel.clearPortfolioList()
            val bundle = bundleOf(
                "orderId" to portfolioItem.orderId,
                "StockDashboard" to binding.stockDashboard,
                "PortfolioItem" to portfolioItem
            )
            findNavController().navigate(R.id.action_nav_to_portfolioViewFragment, bundle)


        }

    }

    private fun observers() {
        viewModel.portfolioTransactionLiveData.observe(viewLifecycleOwner) {
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

        viewModel.portfolioDashboardOfStockLiveData.observe(viewLifecycleOwner) {
            binding.stockDashboard = it.data
            setPortfolioAlertPrice(it.data.alertPrice)
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

        viewModel.deleteAlertPriceLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                requireContext().showShortToast(getString(R.string.portfolio_alert_price_removed))
                setPortfolioAlertPrice(null)
            }
        }

        viewModel.modifyAlertPriceLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                requireContext().showShortToast(getString(R.string.modified_portfolio_alert_price))
                setPortfolioAlertPrice(null)
            }
        }
        viewModel.modifyAlertPriceErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }
        viewModel.deleteAlertPriceErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }
    }

    private fun setPortfolioAlertPrice(alertPrice: Float? = null) {
        binding.portfolioSummery.tvAlert.text = getString(R.string.rs_format, alertPrice ?: 0.0f)

        alertPrice?.let {
            binding.setAlertPriceBtn.setCompoundDrawablesWithIntrinsicBounds(
                resources.getDrawable(
                    R.drawable.ic_baseline_alarm_on_24,
                ), null, null, null
            )
        } ?: run {
            binding.setAlertPriceBtn.setCompoundDrawablesWithIntrinsicBounds(
                resources.getDrawable(
                    R.drawable.ic_baseline_add_alarm_24,
                ), null, null, null
            )
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
        selectedPortfolio: PortfolioItem, isFirstTime: Boolean = false,
    ) {
        binding.showPortfolioStockDashboard = true;
        binding.searchView.visibility = View.GONE
        binding.lvBtn.visibility = View.VISIBLE

        binding.portfolioLbl.setText(R.string.transaction_list_lbl
        )
        adapter = null

        viewModel.portfolioDashboardOfStock(selectedPortfolio.orderStockId)

        portfolioList.clear()
        if (portfolioList.isEmpty())
            viewModel.portfolioListV2Transaction(
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