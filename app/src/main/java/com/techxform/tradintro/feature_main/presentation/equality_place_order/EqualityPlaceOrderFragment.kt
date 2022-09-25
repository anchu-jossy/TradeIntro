package com.techxform.tradintro.feature_main.presentation.equality_place_order

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.DatePicker
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.core.utils.UserDetailsSingleton
import com.techxform.tradintro.databinding.FragmentEqualityPlaceOrderBinding
import com.techxform.tradintro.feature_main.data.remote.dto.BuySellStockReq
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.Stock
import com.techxform.tradintro.feature_main.domain.util.Utils
import com.techxform.tradintro.feature_main.domain.util.Utils.getAfterMonthsTime
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisibiltyGone
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisible
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.math.roundToInt
import kotlin.properties.Delegates

@AndroidEntryPoint
class EqualityPlaceOrderFragment :
    BaseFragment<FragmentEqualityPlaceOrderBinding>(FragmentEqualityPlaceOrderBinding::inflate),
    View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: EqualityPlaceOrderViewModel
    private var orderId: Int = 0
    private lateinit var isBuyOrSell: String
    private val myCalendar = Calendar.getInstance()
    private var quantity: Int = 1
    lateinit var market: Stock
    private var userId by Delegates.notNull<Int>()
    private var screenType: Int = 0
    private var orderPrice = 0.0f

    companion object {
        const val IS_BUY_OR_ORDER = "IsBuyOrOrderButtonClicked"
        const val ORDER_ID = "orderId"
        const val BUY = "BUY"
        const val SELL = "SELL"
        const val FROM_SCREEN = "from_screen"
        const val STOCK = "stock"

        @JvmStatic
        fun newInstance(orderId: Int, orderType: String, fromScreen: Int, stock: Stock) =
            EqualityPlaceOrderFragment().apply {
                arguments = navBundle(orderId, orderType, fromScreen, stock)
            }

        fun navBundle(orderId: Int, orderType: String, fromScreen: Int, stock: Stock) =
            Bundle().apply {
                putInt(ORDER_ID, orderId)
                putString(IS_BUY_OR_ORDER, orderType)
                putInt(FROM_SCREEN, fromScreen)
                putParcelable(STOCK, stock)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.walletSummary(null)
        orderId = requireArguments().getInt(ORDER_ID, 0)
        screenType = requireArguments().getInt(FROM_SCREEN, 0)

        arguments?.getString(IS_BUY_OR_ORDER)?.let {
            isBuyOrSell = it as String
            binding.buttonBuy.text = isBuyOrSell
            if (isBuyOrSell == BUY) {
                binding.titleTv.text = getString(R.string.order_stock)
            } else {
                binding.titleTv.text = getString(R.string.sell_stock)
            }
        }
        binding.chargesGroup.isVisible = (UserDetailsSingleton.userDetailsResponse.treeLevel != 1)


        val stock = if (Build.VERSION.SDK_INT >= 33) {
            requireArguments().getParcelable(STOCK, Stock::class.java)
        } else {
            requireArguments().getParcelable(STOCK)
        }
        if (stock != null) {
            market = stock
            setData(stock)
        }
        binding.buttonBuy.setOnClickListener(this)
        isLimitVisible(false)
        binding.validityDateGroup.isVisible = false
        binding.radioGrp.check(R.id.marketRb)
        setChargesAndBuyAmount()
        binding.quantityEt.addTextChangedListener {
            if (it.toString() == "") {
                quantity = 1
                binding.quantityEt.setText("1")
            } else quantity = it.toString().toInt()


            orderPrice =
                ((market.history!![0].stockHistoryHigh + market.history!![0].stockHistoryLow) / 2)
            val diff =
                market.history!![1].stockHistoryHigh.minus(market.history!![1].stockHistoryLow)
            binding.textDiff.text = diff.roundToInt().toString()

            val sum =
                market.history!![1].stockHistoryHigh.plus(market.history!![1].stockHistoryLow)
            val percent = (diff / sum) * 100
            (getString(
                R.string.rs_format,
                orderPrice
            ) + "(" + percent.roundToInt() + "%)").also { rate ->
                binding.textRate.text = rate
            }
            setChargesAndBuyAmount()

        }

        binding.limitPrizeEt.addTextChangedListener {
            if (it.toString() == "") {
                binding.limitPrizeEt.setText("0")
            }
            setChargesAndBuyAmount()
        }

        binding.orderDateEt.setOnClickListener {
            val dialog = DatePickerDialog(
                requireContext(), this,
                myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )
            dialog.datePicker.maxDate = getAfterMonthsTime(myCalendar, 3)
            dialog.show()
        }


    }


    private fun observer() {
        viewModel.portfolioErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }
        viewModel.walletErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }
        viewModel.userDetailLiveData.observe(viewLifecycleOwner) {
            if (it.data.treeLevel == 1) {
                binding.limitRb.setVisibiltyGone()
            } else {
                binding.limitRb.setVisible()
            }
        }

        viewModel.walletSummaryLiveData.observe(viewLifecycleOwner) {
            it.data.let { walletResponse ->
                userId = walletResponse.userId!!
                binding.balanceEt.setText(walletResponse.tradeMoneyBalance.toString())
                binding.usableBalanceEt.setText(walletResponse.tradeMoneyBalance.toString())
            }
        }

        viewModel.buyStockLiveData.observe(viewLifecycleOwner) {
            it.data.orderId.let {
                requireContext().showShortToast(getString(R.string.bought_success))

                clearBackstack()
                findNavController().navigate(R.id.nav_portfoliolist)
            }

        }
        viewModel.sellStockLiveData.observe(viewLifecycleOwner) {
            it.data.orderId?.let {
                requireContext().showShortToast(
                    getString(R.string.sold_success),
                )

                clearBackstack()
                findNavController().navigate(R.id.nav_portfoliolist)
            }

        }
    }


    private fun setChargesAndBuyAmount() {
        val totalCharge = getTotalCharge(orderPrice, quantity)
        binding.chargesEt.text =
            Utils.formatBigDecimalIntoTwoDecimal(totalCharge).toPlainString()

        binding.buyAmountEt.text = if (binding.limitRb.isChecked) {
            //here limit edited price is converting to float only to use common method
            val totalStockValue =
                getTotalStockValue(binding.limitPrizeEt.text.toString().toFloat(), quantity)
            getOrderTotal(totalStockValue = totalStockValue, totalCharge = totalCharge)
        } else {
            val totalStockValue = getTotalStockValue(orderPrice, quantity)
            getOrderTotal(totalStockValue = totalStockValue, totalCharge = totalCharge)
        }
    }

    /**
     * Ordertotal= total stockvalue+charges
     */
    private fun getOrderTotal(totalStockValue: BigDecimal, totalCharge: BigDecimal): String? {
        return Utils.formatBigDecimalIntoTwoDecimal(
            totalStockValue.plus(totalCharge)
        ).toPlainString()
    }

    private fun setData(market: Stock) {

        with(binding) {
            binding.stock = market
            textName.text = market.stockName
            textName1.text = market.stockName

            binding.limitPrizeEt.setText(
                Utils.formatBigDecimalIntoTwoDecimal(orderPrice.toBigDecimal()).toPlainString()
            )

            with(market.history[0]) {
                textdate.text = Utils.formatDateTimeString(stockHistoryDate)
                textCode.text = stockHistoryCode?.split(".")?.get(1) ?: ""
                ("$stockHistoryOpen,$stockHistoryClose").also {
                    textopenClose.text = it
                }
                ("$stockHistoryHigh,$stockHistoryLow").also {
                    texthighLow.text = it
                }
                exchangeTv.text =
                    stockHistoryCode?.split(".")?.get(1) ?: ""

                orderPrice =
                    (stockHistoryHigh + stockHistoryLow) / 2
            }
            with(market.history[1]) {
                val diff =
                    stockHistoryHigh.minus(stockHistoryLow)
                binding.textDiff.text = diff.roundToInt().toString()
                val sum =
                    stockHistoryHigh.plus(stockHistoryLow)

                val percent = (diff / sum) * 100
                (getString(
                    R.string.rs_format,
                    orderPrice
                ) + "(" + percent.roundToInt() + "%)").also { binding.textRate.text = it }

            }
            val totalCharge = getTotalCharge(orderPrice, quantity)
            binding.chargesEt.text =
                Utils.formatBigDecimalIntoTwoDecimal(totalCharge).toPlainString()

            stockNameEt.setText(market.stockName)

            setClickListeners()


        }
    }

    private fun setClickListeners() {
        with(binding) {
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
        binding.limitGroup.isVisible = isVisible

    }

    /**
     * To getTotalStockValue Stock price we need to multiply order price ana quantity
     */
    private fun getTotalStockValue(orderPrice: Float, quantity: Int): BigDecimal {
        return orderPrice.toBigDecimal() * quantity.toBigDecimal()
    }


    /**
     * To getTotalStockValue total  charge is trans value +brokageValue
     */
    private fun getTotalCharge(orderPrice: Float, quantity: Int): BigDecimal {
        val totalStockValue = getTotalStockValue(orderPrice, quantity)
        val brokageValue = find5Percentage(totalStockValue)
        val transValue = find5Percentage(brokageValue)
        return (brokageValue + transValue)

    }

    /**
     * this calculation is used to find the 5 % of the value given
     */

    private fun find5Percentage(value: BigDecimal): BigDecimal {
        "2".toBigDecimal()
        return (value.divide(BigDecimal.TEN)).divide("2".toBigDecimal(), RoundingMode.DOWN)

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.buttonBuy -> {
                if (quantity < 1 || orderValidity() == -1 || orderValidityDate() == String()
                ) {
                    requireContext().showShortToast(getString(R.string.validate_fields))

                } else if (isBuyOrSell == BUY) {
                    viewModel.buyStock(
                        orderId,
                        BuySellStockReq(
                            quantity,
                            if (binding.marketRb.isChecked) 0 else 1,
                            binding.stock!!.stockCode!!,
                            orderValidity(),
                            orderPrice,
                            orderValidityDate()
                        )
                    )

                } else {

                    viewModel.sellStock(
                        orderId,
                        BuySellStockReq(
                            quantity,
                            if (binding.marketRb.isChecked) 0 else 1,
                            binding.stock!!.stockCode!!,
                            orderValidity(),
                            orderPrice,
                            orderValidityDate()
                        )
                    )
                }
            }
        }
    }

    private fun orderValidityDate(): String {
        when {
            binding.marketRb.isChecked || binding.gtcRb.isChecked || binding.dayRb.isChecked ->
                return Utils.formatCurrentDate()
            binding.gtdRb.isChecked ->
                return if (binding.orderDateEt.text.toString().isNotEmpty())
                    Utils.formatDateString(binding.orderDateEt.text.toString())
                else String()

        }
        return String()
    }

    private fun orderValidity(): Int {
        when {
            binding.marketRb.isChecked || binding.dayRb.isChecked ->
                return 0
            binding.gtdRb.isChecked ->
                return 1
            binding.gtcRb.isChecked ->
                return 2

        }
        return -1
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        binding.orderDateEt.text = ("$p3/$p2/$p1")

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