package com.techxform.tradintro.feature_main.presentation.equality_place_order

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.FragmentEqualityPlaceOrderBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.Stock
import com.techxform.tradintro.feature_main.domain.model.FilterModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class EqualityPlaceOrderFragment :
    BaseFragment<FragmentEqualityPlaceOrderBinding>(FragmentEqualityPlaceOrderBinding::inflate),
    View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: EqualityPlaceOrderViewModel
    private var orderId: Int = 0
    private lateinit var isBuyOrSell: String
    private val myCalendar = Calendar.getInstance()
    var quantity: Int? = null
    lateinit var market: Stock

    companion object {
        const val IS_BUY_OR_ORDER = "IsBuyOrOrderButtonClicked"
        const val ORDER_ID = "orderId"
        const val BUY = "BUY"
        const val SELL = "SELL"

        @JvmStatic
        fun newInstance() =
            EqualityPlaceOrderFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[EqualityPlaceOrderViewModel::class.java]
        viewModel.walletSummary("voucher")
        orderId = requireArguments().getInt(ORDER_ID, 0)
        viewModel.marketDetail(orderId)

        isBuyOrSell = arguments?.get(IS_BUY_OR_ORDER) as String
        binding.buttonBuy.setOnClickListener(this)
        if (isBuyOrSell == BUY)
            binding.titleTv.text = getString(R.string.order_stock)
        else binding.titleTv.text = getString(R.string.sell_stock)
        viewModel.portfolioDetails(orderId, FilterModel("", 100, 0, 0, ""))
        setMarketView()
        binding.radioGrp.check(R.id.marketRb)
        binding.quantityEt.addTextChangedListener {
            quantity = if (it.toString() == "")
                0
            else it.toString().toInt()
            val buyPrice =
                (market.history[0].stockHistoryClose + market.history[0].stockHistoryOpen) / 2
            binding.buyAmountEt.setText(buyPrice.toString())
            val diff =
                market.history[1].stockHistoryOpen.minus(market.history[1].stockHistoryClose)
            binding.textDiff.text = diff.toString()

            val sum =
                market.history[1].stockHistoryOpen.plus(market.history[1].stockHistoryClose)
            val percent = (diff / sum) * 100
            binding.textRate.text = getString(R.string.rs_format, buyPrice) + "(" + percent + "%)"
            binding.chargesEt.setText(getTotalCharge(buyPrice, quantity ?: 0).toString())

        }
        viewModel.portfolioErrorLiveData.observe(viewLifecycleOwner) {
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
                    Toast.makeText(requireContext(), " Server failed", Toast.LENGTH_LONG).show()

                }
                else -> {
                    val errorMsg = (it as Failure.FeatureFailure).message
                    Toast.makeText(requireContext(), "Error: $errorMsg", Toast.LENGTH_LONG).show()
                    //Toast.makeText(requireContext(), " Api failed", Toast.LENGTH_LONG).show()
                }
            }
        }
        viewModel.walletSummaryLiveData.observe(viewLifecycleOwner) {

            binding.balanceEt.setText(it.data?.tradeMoneyBalance.toString())
            binding.usableBalanceEt.setText(it.data?.balance.toString())
        }
        viewModel.portfolioLiveData.observe(viewLifecycleOwner) {
            it.data?.let { it ->
                market=it.market
                setData(it.market)

            }
        }
        viewModel.marketDetailLiveData.observe(viewLifecycleOwner) {
            it.data?.let { stock ->
                market=stock
                setData(stock)
            }

        }
        binding.orderDateEt.setOnClickListener {
            val dialog = DatePickerDialog(
                requireContext(), this,
                myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )
            dialog.show()
        }


    }

    private fun setData(market: Stock) {

        with(binding) {
            binding.stock = market
            textdate.text = market.history[0].stockHistoryDate
            textCode.text = market.history[0].stockHistoryCode.split(".")[1]
            textName.text = market.stockName
            textName1.text = market.stockName
            textopenClose.text =
                "${market.history[0].stockHistoryOpen}" + "," + "${market.history[0].stockHistoryClose}"
            texthighLow.text =
                "${market.history[0].stockHistoryHigh}" + "," + "${market.history[0].stockHistoryLow}"
            exchangeTv.text =
                market.history[0].stockHistoryCode.split(".")[1]

            stockNameEt.setText(market.stockName)



            marketRb.setOnClickListener {
                setMarketView()
            }
            limitRb.setOnClickListener {
                setLimitRbView()
            }
            gtdRb.setOnClickListener {
                this.orderValidityDateLbl.visibility = View.VISIBLE
                this.colon20.visibility = View.VISIBLE
                this.orderDateEt.visibility = View.VISIBLE

            }
            dayRb.setOnClickListener {
                setDefaultRbView()
            }
            gtcRb.setOnClickListener {
                setDefaultRbView()
            }


        }
    }

    private fun setLimitRbView() {
        with(binding) {
            orderValidityLbl.visibility = View.VISIBLE
            gtcRb.visibility = View.VISIBLE
            gtdRb.visibility = View.VISIBLE
            dayRb.visibility = View.VISIBLE
            limitedPrizeLbl.visibility = View.VISIBLE
            colon6.visibility = View.VISIBLE
            limitPrizeEt.visibility = View.VISIBLE
        }
    }

    private fun setDefaultRbView() {
        binding.orderValidityDateLbl.visibility = View.GONE
        binding.colon20.visibility = View.GONE
        binding.orderDateEt.visibility = View.GONE
    }

    private fun setMarketView() {
        with(binding) {
            orderValidityLbl.visibility = View.GONE
            gtcRb.visibility = View.GONE
            gtdRb.visibility = View.GONE
            dayRb.visibility = View.GONE
            limitedPrizeLbl.visibility = View.GONE
            colon6.visibility = View.GONE
            limitPrizeEt.visibility = View.GONE

        }
    }

    private fun getTotalCharge(orderPrice: Float, quantity: Int): Float {

        val brokageValue = ((quantity * orderPrice) / 10) / 2
        val transValue = (brokageValue / 10) / 2
        return brokageValue + transValue

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.buttonBuy -> {
                if (binding.quantityEt.text.toString().toInt() < 1) {
                    Toast.makeText(requireContext(), "quantity cannot be zero", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {

        binding.orderDateEt.setText("$p3/$p2/$p1")

    }


}