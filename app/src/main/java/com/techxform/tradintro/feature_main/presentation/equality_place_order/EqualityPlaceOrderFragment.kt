package com.techxform.tradintro.feature_main.presentation.equality_place_order

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.DatePicker
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.FragmentEqualityPlaceOrderBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.Stock
import com.techxform.tradintro.feature_main.domain.model.FilterModel
import com.techxform.tradintro.feature_main.domain.model.PaymentType
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.math.roundToInt

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[EqualityPlaceOrderViewModel::class.java]
        observer()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.walletSummary(PaymentType.VOUCHER)
        orderId = requireArguments().getInt(ORDER_ID, 0)
        viewModel.marketDetail(orderId)

        isBuyOrSell = arguments?.get(IS_BUY_OR_ORDER) as String
        binding.buttonBuy.text=isBuyOrSell
        binding.buttonBuy.setOnClickListener(this)
        if (isBuyOrSell == BUY){
            binding.titleTv.text = getString(R.string.order_stock)}
        else{ binding.titleTv.text = getString(R.string.sell_stock)}
        viewModel.portfolioDetails(orderId, FilterModel("", 100, 0, 0, ""))
        isLimitVisible(false)
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
            binding.textDiff.text = diff.roundToInt().toString()

            val sum =
                market.history[1].stockHistoryOpen.plus(market.history[1].stockHistoryClose)
            val percent = (diff / sum) * 100
            binding.textRate.text =
                getString(R.string.rs_format, buyPrice) + "(" + percent.roundToInt() + "%)"
            binding.chargesEt.setText(getTotalCharge(buyPrice, quantity ?: 0).toString())

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

    private fun observer() {
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
                market = it.market
                setData(it.market)

            }
        }
        viewModel.marketDetailLiveData.observe(viewLifecycleOwner) {
            it.data?.let { stock ->
                market = stock
                setData(stock)
            }

        }
    }

    private fun setData(market: Stock) {

        with(binding) {
            binding.stock = market
            textdate.text = market.history[0].formatDate()
            textCode.text = market.history[0].stockHistoryCode?.split(".")?.get(1) ?: ""
            textName.text = market.stockName
            textName1.text = market.stockName
            textopenClose.text =
                "${market.history[0].stockHistoryOpen}" + "," + "${market.history[0].stockHistoryClose}"
            texthighLow.text =
                "${market.history[0].stockHistoryHigh}" + "," + "${market.history[0].stockHistoryLow}"
            exchangeTv.text =
                market.history[0].stockHistoryCode?.split(".")?.get(1) ?: ""
            val buyPrice =
                (market.history[0].stockHistoryClose + market.history[0].stockHistoryOpen) / 2
            binding.buyAmountEt.setText(buyPrice.toString())
            if (market.history.size > 1) {
                val diff =
                    market.history[1].stockHistoryOpen.minus(market.history[1].stockHistoryClose)
                binding.textDiff.text = diff.roundToInt().toString()

                val sum =
                    market.history[1].stockHistoryOpen.plus(market.history[1].stockHistoryClose)
                val percent = (diff / sum) * 100
                binding.textRate.text =
                    getString(R.string.rs_format, buyPrice) + "(" + percent.roundToInt() + "%)"
            }

            binding.chargesEt.setText(getTotalCharge(buyPrice, quantity ?: 0).toString())

            stockNameEt.setText(market.stockName)



            marketRb.setOnClickListener {
                isLimitVisible(false)
            }

            limitRb.setOnClickListener {
                isLimitVisible(true)
            }


            gtdRb.setOnCheckedChangeListener(checkListener)
            dayRb.setOnCheckedChangeListener(checkListener)
            gtcRb.setOnCheckedChangeListener(checkListener)


        }
    }

    private val checkListener = CompoundButton.OnCheckedChangeListener { p0, p1 ->
        when (p0.id) {
            R.id.gtdRb -> {
                binding.gtdRb.isChecked = p1
                binding.dayRb.isChecked = false
                binding.gtcRb.isChecked = false
                binding.validityDateGroup.isVisible = true
            }
            R.id.dayRb -> {
                binding.gtdRb.isChecked = false
                binding.dayRb.isChecked = p1
                binding.gtcRb.isChecked = false
                binding.validityDateGroup.isVisible = false
            }
            R.id.gtcRb -> {
                binding.gtdRb.isChecked = false
                binding.dayRb.isChecked = false
                binding.gtcRb.isChecked = p1
                binding.validityDateGroup.isVisible = false
            }
            else -> {
                binding.gtdRb.isChecked = false
                binding.dayRb.isChecked = false
                binding.gtcRb.isChecked = false
                binding.validityDateGroup.isVisible = false
            }
        }
    }

    private fun isLimitVisible(isVisible: Boolean) {
        with(binding) {
            limitGroup.isVisible = isVisible
            /* orderValidityLbl.visibility = View.VISIBLE
             gtcRb.visibility = View.VISIBLE
             gtdRb.visibility = View.VISIBLE
             dayRb.visibility = View.VISIBLE
             limitedPrizeLbl.visibility = View.VISIBLE
             colon6.visibility = View.VISIBLE
             limitPrizeEt.visibility = View.VISIBLE*/
        }
    }

   /* private fun setDefaultRbView() {
        binding.orderValidityDateLbl.visibility = View.GONE
        binding.colon20.visibility = View.GONE
        binding.orderDateEt.visibility = View.GONE
    }*/


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

        binding.orderDateEt.text = ("$p3/$p2/$p1")

    }


}