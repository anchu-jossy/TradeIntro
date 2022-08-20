package com.techxform.tradintro.feature_main.presentation.market

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
    private var totalPrice by Delegates.notNull<Int>()


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
        totalPrice = requireArguments().getInt("totalPrice")

        binding.buttonGroup.isVisible = (UserDetailsSingleton.userDetailsResponse.treeLevel != 1)
        listeners()
        viewModel.marketDetail(stockId)
        binding.ediTextAddtoWatchList.setText("$totalPrice.00")



    }

    private fun listeners()
    {
        binding.buyBtn.setOnClickListener {
            if(binding.stock !=null)
                viewModel.buyStock(stockId, BuySellStockReq(5,0,  binding.stock!!.stockCode!!, 0, 0f, "2022-05-26T00:00:00.000Z"))
        }

        binding.addToWatchlistBtn.setOnClickListener {
            if (binding.stock?.watchList == null)
                viewModel.createWatchList(CreateWatchListRequest(stockId,  totalPrice))
            else viewModel.removeWatchlist(binding.stock?.watchList?.watchlistId?:0)
        }
        binding.sellBtn.setOnClickListener {
            if(binding.stock !=null)
                viewModel.sellStock(stockId, BuySellStockReq(5,0,  binding.stock!!.stockCode!!, 0, 0f, "2022-05-26T00:00:00.000Z"))
        }

        binding.setAlertPriceBtn.setOnClickListener {
            //TODO: Open Dialog
            alertPriceSetDialog()
        }
    }


    private fun alertPriceSetDialog()
    {
        val builder = AlertDialog.Builder(requireContext())
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
        //amountEt.gravity = Gravity.TOP or Gravity.LEFT
        amountEt.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
        amountEt.setLines(1)
        amountEt.maxLines = 1
        container.addView(amountEt, lp)

        builder.setView(container)
        builder.setPositiveButton(R.string.modify_alert_lbl
        ) { dialog, p1 ->
            if(amountEt.text.toString().isNullOrEmpty())
                Toast.makeText(requireContext(),R.string.enter_alert_price_lbl, Toast.LENGTH_SHORT).show()
            else {
                viewModel.modifyAlertPrice(
                    stockId,
                    AlertPriceRequest(amountEt.text.toString().toDouble())
                )
                dialog.dismiss()
            }
        }

        builder.setNegativeButton(R.string.remove_alert_lbl
        ) { dialog, p1 ->
            dialog.dismiss()
        }
        builder.show()

    }


    private fun createPriceType(stockHistory: StockHistory?): ArrayList<PriceType> {
        val priceTypes = arrayListOf<PriceType>()

        if (stockHistory == null) { //TODO: Remove
        /*    priceTypes.add(PriceType(4663.36f, getString(R.string.open_lbl)))
            priceTypes.add(PriceType(4363.36f, getString(R.string.close_lbl)))
            priceTypes.add(PriceType(3728.34f, getString(R.string.lower_circuit_lbl)))
            priceTypes.add(PriceType(47243.3f, getString(R.string.upper_circuit_lbl)))*/
        } else {
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

           /* val avg = (stockHistory.stockHistoryHigh + stockHistory.stockHistoryLow + stockHistory.stockHistoryClose + stockHistory.stockHistoryOpen)/4
            priceTypes.add(
                PriceType(
                    avg,
                    getString(R.string.avg_traded_lbl)
                )
            )*/
        }
        return priceTypes

    }

    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.marketDetailLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                var average = 0.0f
                if (it.data.history != null)
                    average =
                        (it.data.history[0].stockHistoryOpen.plus(it.data.history[0].stockHistoryClose) / 2)
                binding.amountTv.text = getString(R.string.rs_format, average)
                binding.stock = it.data
                binding.priceRv.adapter = PriceAdapter(createPriceType(it.data?.history?.get(0)))

                binding.addToWatchlistBtn.text = if (binding.stock?.watchList == null)
                    getString(R.string.add_to_watchlist_lbl)
                else
                    getString(R.string.remove_watchlist_lbl)
            }
        }
        viewModel.createWatchListLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                Toast.makeText(
                    requireContext(),
                    "Successfully updated  to watchlist",
                    Toast.LENGTH_LONG
                )
                    .show()
                binding.addToWatchlistBtn.text = getString(R.string.remove_watchlist_lbl)
                binding.stock?.watchList = it.data
            }
        }
        /*viewModel.updateWatchListLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Successfully updated to watchlist", Toast.LENGTH_LONG)
                .show()
        }*/

        viewModel.deleteWatchlistLiveData.observe(viewLifecycleOwner){
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                Toast.makeText(
                    requireContext(),
                    "Successfully removed from watchlist",
                    Toast.LENGTH_SHORT
                )
                    .show()
                binding.stock?.watchList = null
                binding.addToWatchlistBtn.text = getString(R.string.add_to_watchlist_lbl)
            }
        }

        viewModel.modifyAlertPriceLiveData.observe(viewLifecycleOwner){
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                Toast.makeText(
                    requireContext(),
                    "Successfully modified alert price",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        viewModel.deleteWatchListErrorLiveData.observe(viewLifecycleOwner){
            handleError(it)
        }
        viewModel.marketErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }
        viewModel.buyStockErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }
        viewModel.sellStockErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }
        viewModel.modifyAlertPriceErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }

        viewModel.buyStockLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                findNavController().navigate(R.id.equalityPlaceOrderFragment, EqualityPlaceOrderFragment.navBundle(stockId,EqualityPlaceOrderFragment.BUY, ScreenType.MARKET, binding.stock!!))
            }
        }
        viewModel.sellStockLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                findNavController().navigate(R.id.equalityPlaceOrderFragment,  EqualityPlaceOrderFragment.navBundle(stockId,EqualityPlaceOrderFragment.SELL, ScreenType.MARKET,binding.stock!!))

            }
        }
    }

    private fun handleError(failure: Failure)
    {
        when (failure) {
            Failure.NetworkConnection -> {
                sequenceOf(
                    Toast.makeText(
                        requireContext(), getString(R.string.no_internet_error),
                        Toast.LENGTH_SHORT
                    ).show()
                )
            }
            Failure.ServerError-> {
                Toast.makeText(requireContext(), getString(R.string.server_error), Toast.LENGTH_LONG).show()

            }
            else -> {
                val errorMsg = (failure as Failure.FeatureFailure).message
                Toast.makeText(requireContext(), "Error: $errorMsg", Toast.LENGTH_LONG).show()
                //Toast.makeText(requireContext(), " Api failed", Toast.LENGTH_LONG).show()
            }
        }
    }

}