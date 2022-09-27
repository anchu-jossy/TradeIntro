package com.techxform.tradintro.feature_main.presentation.watchlistview

import android.content.DialogInterface
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
import com.techxform.tradintro.feature_main.data.remote.dto.BuySellStockReq
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.WatchList
import com.techxform.tradintro.feature_main.domain.model.PriceType
import com.techxform.tradintro.feature_main.domain.util.Utils
import com.techxform.tradintro.feature_main.presentation.equality_place_order.EqualityPlaceOrderFragment
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisibiltyGone
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
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
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[WatchlistViewViewModel::class.java]
        observers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        watchlistId = requireArguments().getInt("watchlistId")
        binding.alertPriceType = PriceType(63.2f, getString(R.string.alert_price_lbl))

        listeners()
        viewModel.watchlistDetails(watchlistId)
    }

    private fun listeners() {
        binding.setAlertPriceBtn.setOnClickListener {
            alertPriceSetDialog(watchList.alert,::posListener ,  ::negListener)
        }
        binding.buyBtn.setOnClickListener {
            if (watchList != null && watchList.market != null) {
                findNavController().navigate(
                    R.id.equalityPlaceOrderFragment, EqualityPlaceOrderFragment.navBundle(
                        watchList.market!!.stockId,
                        EqualityPlaceOrderFragment.BUY, ScreenType.WATCHLIST, watchList.market!!
                    )
                )

            }
        }
        binding.sellBtn.setOnClickListener {
            if (watchList != null && watchList.market!! != null)
                findNavController().navigate(
                    R.id.equalityPlaceOrderFragment, EqualityPlaceOrderFragment.navBundle(
                        watchList.market!!.stockId,
                        EqualityPlaceOrderFragment.SELL, ScreenType.WATCHLIST, watchList.market!!
                    )
                )
        }
    }
    private fun posListener(amount: Double) {
        viewModel.setAlertPrice(
            watchlistId,
            AlertPriceRequest(amount)
        )
    }
    private fun negListener(){}
    private fun alertPriceSetDialog() {


        /*val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.alert_price_lbl)

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
        amountEt.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
        amountEt.setLines(1)
        amountEt.maxLines = 1
        amountEt.setText((watchList.alert?.notificationPrice ?: 0.0f).toString())
        container.addView(amountEt, lp)
        builder.setView(container)
        var posBtn = getString(R.string.set_alert_lbl)
        var negBtn = getString(R.string.dismiss_lbl)

        if (watchList.alert != null) {
            posBtn = getString(R.string.modify_alert_lbl)
            negBtn = getString(R.string.remove_alert_lbl)
        }
        builder.setPositiveButton(
            posBtn
        ) { dialog, p1 ->
            if (amountEt.text.toString().isNullOrEmpty())
                requireContext().showShortToast(getString(R.string.enter_alert_price_lbl))
            else {
                viewModel.setAlertPrice(
                    watchlistId,
                    AlertPriceRequest(amountEt.text.toString().toDouble())
                )
                dialog.dismiss()
            }
        }

        builder.setNegativeButton(
            negBtn
        ) { dialog, p1 ->
            dialog.dismiss()
        }
        builder.show()
*/
    }

    private fun setGainProfit(watchList: WatchList) {
        var currentPrice = 0.0f
        val size = watchList.market?.history?.size ?: 0;
        if (size > 0) {
            currentPrice =
                (watchList.market?.history?.firstOrNull()?.stockHistoryHigh?.plus(watchList.market.history?.firstOrNull()!!.stockHistoryLow)
                    ?.div(2) ?: 0) as Float
        }
        val gainLoss = currentPrice.minus(watchList.watchStockPrice)
        val per =
            ((currentPrice - watchList.watchStockPrice) / ((currentPrice + watchList.watchStockPrice) / 2)) * 100
        with(binding) {
            alertPrice.titleTv.text =
                Utils.formatStringToTwoDecimals(watchList.watchStockPrice.toString())
            gainLossPrice.titleTv.text = Utils.formatStringToTwoDecimals(gainLoss.toString())
            gainLossPerPrice.titleTv.text = getString(
                R.string.per_format_string,
                Utils.formatPercentageWithoutDecimals(per.toString())
            )
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
            }
            if (gainLoss < 0) {
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
            }
        }
    }

    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.modifyAlertPriceLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                //TODO: on success modify watchlist
                Toast.makeText(
                    requireContext(),
                    "Successfully modified alert price",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        viewModel.modifyAlertPriceErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }

        viewModel.watchlistViewLiveData.observe(viewLifecycleOwner) {
            binding.watchlist = it.data
            watchList = it.data
            setGainProfit(it.data)
            if (watchList.alert != null && watchList.alert!!.notificationPrice != 0.0f) {
                binding.setAlertPriceBtn.setText(R.string.edit_alert_price_lbl)
            }
        }

        viewModel.watchlistViewErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }
        viewModel.buyStockErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }
        viewModel.sellStockErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }
        viewModel.buyStockLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                findNavController().navigate(
                    R.id.equalityPlaceOrderFragment, EqualityPlaceOrderFragment.navBundle(
                        watchList.market!!.stockId,
                        EqualityPlaceOrderFragment.BUY, ScreenType.WATCHLIST, watchList.market!!
                    )
                )
            }
        }
        viewModel.sellStockLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                findNavController().navigate(
                    R.id.equalityPlaceOrderFragment, EqualityPlaceOrderFragment.navBundle(
                        watchList.market!!.stockId,
                        EqualityPlaceOrderFragment.SELL, ScreenType.WATCHLIST, watchList.market!!
                    )
                )

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
                Toast.makeText(
                    requireContext(),
                    getString(R.string.server_error),
                    Toast.LENGTH_LONG
                ).show()

            }
            else -> {
                val errorMsg = (failure as Failure.FeatureFailure).message
                Toast.makeText(requireContext(), "Error: $errorMsg", Toast.LENGTH_LONG).show()
                //Toast.makeText(requireContext(), " Api failed", Toast.LENGTH_LONG).show()
            }
        }
    }

}