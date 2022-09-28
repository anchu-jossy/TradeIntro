package com.techxform.tradintro.feature_main.presentation.market

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.core.utils.ScreenType
import com.techxform.tradintro.core.utils.UserDetailsSingleton
import com.techxform.tradintro.databinding.MarketDetailFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.domain.model.PriceType
import com.techxform.tradintro.feature_main.domain.util.Utils.formatStringToTwoDecimals
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisibiltyGone
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisible
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import com.techxform.tradintro.feature_main.presentation.equality_place_order.EqualityPlaceOrderFragment
import com.techxform.tradintro.feature_main.presentation.portfolio_view.PriceAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates


@AndroidEntryPoint
class MarketDetailFragment :
    BaseFragment<MarketDetailFragmentBinding>(MarketDetailFragmentBinding::inflate) {

    companion object {
        fun newInstance() = MarketDetailFragment()
    }

    private lateinit var viewModel: MarketDetailViewModel
    private var stockId by Delegates.notNull<Int>()
    private var watchListId: Int? = null;
    private var totalPrice :Float?=null;


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[MarketDetailViewModel::class.java]
        observers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stockId = requireArguments().getInt("stockId")
        //totalPrice = requireArguments().getInt("totalPrice")

        binding.buttonGroup.isVisible = (UserDetailsSingleton.userDetailsResponse.treeLevel != 1)
        listeners()
        viewModel.marketDetail(stockId)
       // binding.ediTextAddtoWatchList.setText("$totalPrice.00")
    }

    private fun listeners() {
        binding.buyBtn.setOnClickListener {
            if (binding.stock != null)
                findNavController().navigate(
                    R.id.equalityPlaceOrderFragment,
                    EqualityPlaceOrderFragment.navBundle(
                        stockId,
                        EqualityPlaceOrderFragment.BUY,
                        ScreenType.MARKET,
                        binding.stock!!
                    )
                )

        }

        binding.addToWatchlistBtn.setOnClickListener {
            if (binding.stock?.watchList == null) {
                Log.e("totalPrice", totalPrice.toString())
                totalPrice?.let {
                    viewModel.createWatchList(CreateWatchListRequest(stockId, it))
                }
            }
            else removeWatchListConformation()
        }
        binding.sellBtn.setOnClickListener {
            if (binding.stock != null)
                findNavController().navigate(
                    R.id.equalityPlaceOrderFragment,
                    EqualityPlaceOrderFragment.navBundle(
                        stockId,
                        EqualityPlaceOrderFragment.SELL,
                        ScreenType.MARKET,
                        binding.stock!!
                    )
                )
        }


        binding.setAlertPriceBtn.setOnClickListener {
            //alertPriceSetDialog()
            alertPriceSetDialog(watchListAlert,::posListener ,  ::negListener)
        }
    }

    private fun negListener(id:Int) {
        viewModel.deleteAlertPrice(
            notificationId = id
        )
    }

    private fun posListener(amount: Double) {
        watchListId?.let {
            viewModel.modifyWatchListAlertPrice(
                it,
                AlertPriceRequest(amount)
            )
        }
    }

    private fun removeWatchListConformation(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.delete_watch_list_ttl)
        builder.setMessage(R.string.delete_watch_list_desc)
        builder.setIcon(android.R.drawable.stat_sys_warning)
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            dialogInterface.dismiss()
            viewModel.removeWatchlist(binding.stock?.watchList?.watchlistId ?: 0)
        }
        //performing negative action
        builder.setNegativeButton("Not now") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
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

        watchListAlert?.let {
            builder.setTitle(R.string.watchlist_modify_alert_price_lbl)
            amountEt.setText(it.notificationPrice.toString())
            builder.setNegativeButton(
                R.string.remove_alert_lbl
            ) { dialog, _ ->
                viewModel.deleteAlertPrice(
                    notificationId = it.notificationId
                )
                dialog.dismiss()
            }
        } ?: run {
            builder.setTitle(R.string.add_watchlist_alert_price_lbl)
        }
        builder.setPositiveButton(
            if (watchListAlert == null) R.string.add_alert_lbl else R.string.modify_alert_lbl
        ) { dialog, _ ->
            if (amountEt.text.toString().isEmpty()) {
                amountEt.error = (getString(R.string.enter_alert_price_lbl))
                return@setPositiveButton
            } else {
                watchListId?.let {
                    viewModel.modifyWatchListAlertPrice(
                        it,
                        AlertPriceRequest(amountEt.text.toString().toDouble())
                    )
                }
                dialog.dismiss()
            }
        }



        builder.show()

    }


    private fun createPriceType(stockHistory: StockHistory?): ArrayList<PriceType> {
        val priceTypes = arrayListOf<PriceType>()

        if (stockHistory != null) { //TODO: Remove
            priceTypes.add(PriceType(stockHistory.stockHistoryOpen, getString(R.string.open_lbl)))
            priceTypes.add(PriceType(stockHistory.stockHistoryClose, getString(R.string.close_lbl)))

            priceTypes.add(
                PriceType(
                    stockHistory.stockHistoryLow,
                    getString(R.string.low_lbl)
                )
            )
            priceTypes.add(
                PriceType(
                    stockHistory.stockHistoryHigh,
                    getString(R.string.high_lbl)
                )
            )


        }
        return priceTypes

    }

    private var watchListAlert: Notifications? = null
    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.marketDetailLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {

                val diffPer = it.data.perDiff()
                val symbol = if (diffPer > 0) {
                    binding.amountTv.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green
                        )
                    )
                    "+"
                } else if (diffPer < 0) {
                    binding.amountTv.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                    ""
                } else {
                    binding.amountTv.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    ""
                }

                val formatted = formatStringToTwoDecimals(diffPer.toString())
                binding.amountTv.text =
                    "${getString(R.string.rs_format, it.data.currentValue())} " +
                            "($symbol $formatted%)"

                binding.stock = it.data
                totalPrice=it.data.currentValue()
                setWatchListAlertPrice(it.data.watchListAlert)

                it.data.watchList?.let { wl ->
                    watchListId = wl.watchlistId
                    binding.setAlertPriceBtn.setVisible()
                } ?: run {
                    watchListId = null
                    binding.setAlertPriceBtn.setVisibiltyGone()
                }

                it.data.portfolioItems?.let { items ->
                    var buyQuantity = 0
                    var buyAmount = 0.0f
                    var sellQuantity = 0
                    var sellAmount = 0.0f
                    items.forEach { item ->
                        if (item.orderType == 0) {
                            buyQuantity += item.orderQty
                            buyAmount += item.orderTotal
                        } else {
                            sellQuantity += item.orderQty
                            sellAmount += item.orderTotal
                        }
                    }
                    val qty = buyQuantity - sellQuantity
                    val totalPrice = buyAmount - sellAmount;
                    val totalValue = it.data.currentValue() * qty;
                    val gainLossValue = totalValue - totalPrice;
                    val gainLossPercentage = ((gainLossValue / totalPrice) * 100)
                    binding.tvPortfolioQty.text = "$qty Qty in Portfolio"

                    val pSymbol = if (gainLossPercentage > 0) {
                        binding.tvPortfolioChange.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                        "+"
                    } else if (gainLossPercentage < 0) {
                        binding.tvPortfolioChange.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.red
                            )
                        )
                        ""
                    } else {
                        binding.tvPortfolioChange.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            )
                        )
                        ""
                    }
                    val pFormatted = formatStringToTwoDecimals(gainLossPercentage.toString())
                    binding.tvPortfolioChange.text = "($pSymbol $pFormatted%)"
                }
                binding.priceRv.adapter = PriceAdapter(createPriceType(it.data.history[0]))
                val code = it.data.history[0].stockHistoryCode?.split(".")?.get(1)
                binding.exchangeTv.text =
                    code?.replaceFirstChar { char -> char.lowercase() }
                //binding.addToWatchlistBtn.text =
                if (binding.stock?.watchList == null) {
                    binding.addToWatchlistBtn.setCompoundDrawablesWithIntrinsicBounds(
                        resources.getDrawable(
                            R.drawable.ic_round_add_circle_outline_24,
                        ), null, null, null
                    )
                    //getString(R.string.add_to_watchlist_lbl)
                } else {
                    binding.addToWatchlistBtn.setCompoundDrawablesWithIntrinsicBounds(
                        resources.getDrawable(
                            R.drawable.ic_round_remove_circle_outline_24,
                        ), null, null, null
                    )
                    //getString(R.string.remove_watchlist_lbl)
                }
            }
        }
        viewModel.createWatchListLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                requireContext().showShortToast(getString(R.string.added_to_watchlist))
                //binding.addToWatchlistBtn.text = getString(R.string.remove_watchlist_lbl)
                binding.addToWatchlistBtn.setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(
                        R.drawable.ic_round_remove_circle_outline_24,
                    ), null, null, null
                )
                binding.stock?.watchList = it.data
                watchListId = it.data.watchlistId
                setWatchListAlertPrice(null)
                binding.setAlertPriceBtn.setVisible()
            }
        }


        viewModel.deleteWatchlistLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                requireContext().showShortToast(getString(R.string.removed_to_watchlist))
                binding.stock?.watchList = null
                //binding.addToWatchlistBtn.text = getString(R.string.add_to_watchlist_lbl)
                binding.addToWatchlistBtn.setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(
                        R.drawable.ic_round_add_circle_outline_24,
                    ), null, null, null
                )
                binding.setAlertPriceBtn.setVisibiltyGone()
                watchListId = null
                setWatchListAlertPrice(null)
            }
        }

        viewModel.deleteAlertPriceLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                requireContext().showShortToast(getString(R.string.watch_list_alert_price_removed))
                setWatchListAlertPrice(null)
            }
        }

        viewModel.modifyAlertPriceLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                requireContext().showShortToast(getString(R.string.modified_watch_list_alert_price))
                viewModel.marketDetail(stockId)
            }
        }
        viewModel.modifyAlertPriceErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }
        viewModel.deleteAlertPriceErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }

        viewModel.deleteWatchListErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }



        viewModel.marketErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }



    }

    private fun setWatchListAlertPrice(alertPrice: Notifications? = null) {
        this.watchListAlert = alertPrice;
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