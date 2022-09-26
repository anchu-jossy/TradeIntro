package com.techxform.tradintro.feature_main.presentation.watchlistview

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.core.utils.ScreenType
import com.techxform.tradintro.databinding.WatchlistViewFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.AlertPriceRequest
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.Notifications
import com.techxform.tradintro.feature_main.data.remote.dto.WatchList
import com.techxform.tradintro.feature_main.domain.model.PriceType
import com.techxform.tradintro.feature_main.domain.util.Utils
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import com.techxform.tradintro.feature_main.presentation.equality_place_order.EqualityPlaceOrderFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class WatchlistViewFragment :
    BaseFragment<WatchlistViewFragmentBinding>(WatchlistViewFragmentBinding::inflate) {

    companion object {
        fun newInstance() = WatchlistViewFragment()
    }

    private lateinit var viewModel: WatchlistViewViewModel
    private var watchlistId by Delegates.notNull<Int>()
    private lateinit var watchList: WatchList

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(this)[WatchlistViewViewModel::class.java]
        observers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        watchlistId = requireArguments().getInt("watchlistId")
        binding.alertPriceType = PriceType(0.0f, getString(R.string.alert_price_lbl))
        listeners()
        viewModel.watchlistDetails(watchlistId)
    }

    private fun listeners() {
        binding.setAlertPriceBtn.setOnClickListener {
            alertPriceSetDialog()
        }
        binding.removeWatchlistBtn.setOnClickListener {
            removeWatchListConformation()
        }
        binding.buyBtn.setOnClickListener {
            if (watchList.market != null) {
                findNavController().navigate(R.id.equalityPlaceOrderFragment,
                    EqualityPlaceOrderFragment.navBundle(watchList.market!!.stockId,
                        EqualityPlaceOrderFragment.BUY, ScreenType.WATCHLIST, watchList.market!!))

            }
        }
        binding.sellBtn.setOnClickListener {
            if (watchList.market != null)
                findNavController().navigate(R.id.equalityPlaceOrderFragment,
                    EqualityPlaceOrderFragment.navBundle(watchList.market!!.stockId,
                        EqualityPlaceOrderFragment.SELL, ScreenType.WATCHLIST, watchList.market!!))
        }
    }

    private fun removeWatchListConformation() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.delete_watch_list_ttl)
        builder.setMessage(R.string.delete_watch_list_desc)
        builder.setIcon(android.R.drawable.stat_sys_warning)
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            dialogInterface.dismiss()
            viewModel.removeWatchlist(watchList.watchlistId ?: 0)
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

        watchList.alert?.let {
            builder.setTitle(R.string.modify_alert_price_lbl)
            amountEt.setText(it.notificationPrice.toString())
            builder.setNegativeButton(
                R.string.remove_alert_lbl
            ) { dialog, _ ->
                viewModel.modifyWatchListAlertPrice(
                    watchlistId,
                    AlertPriceRequest(amountEt.text.toString().toDouble())
                )
                dialog.dismiss()
            }
        } ?: run {
            builder.setTitle(R.string.add_alert_price_lbl)
        }
        builder.setPositiveButton(
            if (watchList.alert == null) R.string.add_alert_lbl else R.string.modify_alert_lbl
        ) { dialog, _ ->
            if (amountEt.text.toString().isEmpty()) {
                amountEt.error = (getString(R.string.enter_alert_price_lbl))
                return@setPositiveButton
            } else {
                watchList.watchlistId.let {
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

    private fun setWatchListAlertPrice(alertPrice: Notifications? = null) {
        this.watchList.alert = alertPrice;
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

    private fun setGainProfit(watchList: WatchList) {


        with(binding) {
            val per = watchList.gainLossDiffAmount()
            alertPrice.titleTv.text =
                Utils.formatStringToTwoDecimals(watchList.alert?.notificationPrice.toString()
                    ?: "0")
            gainLossPrice.titleTv.text =
                Utils.formatStringToTwoDecimals(watchList.gainLossDiffAmount().toString())
            gainLossPerPrice.titleTv.text = watchList.asPercentageText()
            gainLossPrice.subTitleTv.text = getString(R.string.gain_loss_lbl)
            gainLossPerPrice.subTitleTv.text = getString(R.string.per_gain_loss_lbl)

            if (per < 0) {
                gainLossPerPrice.titleTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
                gainLossPerPrice.subTitleTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
                gainLossPrice.titleTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
                gainLossPrice.subTitleTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
            } else if (per > 0) {
                gainLossPerPrice.titleTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
                gainLossPerPrice.subTitleTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
                gainLossPrice.titleTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
                gainLossPrice.subTitleTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
            } else {
                gainLossPerPrice.titleTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
                gainLossPerPrice.subTitleTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
                gainLossPrice.titleTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
                gainLossPrice.subTitleTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }

        }
    }

    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.modifyAlertPriceErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }

        viewModel.watchlistViewLiveData.observe(viewLifecycleOwner) {
            binding.watchlist = it.data
            watchList = it.data
            setWatchListAlertPrice(watchList.alert)
            setGainProfit(it.data)
        }

        viewModel.watchlistViewErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
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
                viewModel.watchlistDetails(watchlistId)
            }
        }
        viewModel.modifyAlertPriceErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }
        viewModel.deleteWatchListErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }
        viewModel.deleteAlertPriceErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }

        viewModel.deleteWatchlistLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                requireContext().showShortToast(getString(R.string.removed_to_watchlist))
                findNavController().popBackStack()
            }
        }
    }

    private fun handleError(failure: Failure) {
        when (failure) {
            Failure.NetworkConnection -> {
                sequenceOf(
                    Toast.makeText(
                        requireContext(), getString(R.string.no_internet_error),
                        Toast.LENGTH_SHORT
                    ).show()
                )
            }
            Failure.ServerError -> {
                Toast.makeText(requireContext(),
                    getString(R.string.server_error),
                    Toast.LENGTH_LONG).show()

            }
            else -> {
                val errorMsg = (failure as Failure.FeatureFailure).message
                Toast.makeText(requireContext(), "Error: $errorMsg", Toast.LENGTH_LONG).show()
                //Toast.makeText(requireContext(), " Api failed", Toast.LENGTH_LONG).show()
            }
        }
    }

}