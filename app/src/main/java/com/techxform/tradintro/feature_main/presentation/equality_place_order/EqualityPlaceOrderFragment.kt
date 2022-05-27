package com.techxform.tradintro.feature_main.presentation.equality_place_order

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.FragmentEqualityPlaceOrderBinding
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
    val myCalendar = Calendar.getInstance()


    companion object {
        const val IS_BUY_OR_ORDER = "IsBuyOrOrderButtonClicked"
        const val ORDER_ID = "orderId"

        @JvmStatic
        fun newInstance() =
            EqualityPlaceOrderFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[EqualityPlaceOrderViewModel::class.java]
        orderId = arguments?.get(ORDER_ID) as Int
        isBuyOrSell = arguments?.get(IS_BUY_OR_ORDER) as String
        binding.buttonBuy.setOnClickListener(this)
        if (isBuyOrSell == "BUY")
            binding.titleTv.text = getString(R.string.order_stock)
        else binding.titleTv.text = getString(R.string.sell_stock)
        viewModel.portfolioDetails(orderId, FilterModel("", 100, 0, 0, ""))
        with(binding) {
            binding.radioGrp.check(R.id.marketRb)
            orderValidityLbl.visibility = View.GONE
            gtcRb.visibility = View.GONE
            gtdRb.visibility = View.GONE
            dayRb.visibility = View.GONE
            limitedPrizeLbl.visibility = View.GONE
            colon6.visibility = View.GONE
            limitPrizeEt.visibility = View.GONE

        }
        viewModel.portfolioLiveData.observe(viewLifecycleOwner) {
            binding.exchangeTv.text =
                it.data.market.history[0].stockHistoryCode.split(".")[1]
            binding.stockNameEt.setText(it.data.market.stockName)
            val buyPrice=(it.data.market.history[0].stockHistoryClose+it.data.market.history[0].stockHistoryOpen)/2
            binding.chargesEt.setText(getTotalCharge(buyPrice, 1).toString())
        }
        binding.marketRb.setOnClickListener {
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
        binding.limitRb.setOnClickListener {
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
        binding.gtdRb.setOnClickListener {
            with(binding) {
                binding.orderValidityDateLbl.visibility = View.VISIBLE
                binding.colon20.visibility = View.VISIBLE
                binding.orderDateEt.visibility = View.VISIBLE
            }

        }
        binding.dayRb.setOnClickListener {
            binding.orderValidityDateLbl.visibility = View.GONE
            binding.colon20.visibility = View.GONE
            binding.orderDateEt.visibility = View.GONE
        }
        binding.gtcRb.setOnClickListener {
            binding.orderValidityDateLbl.visibility = View.GONE
            binding.colon20.visibility = View.GONE
            binding.orderDateEt.visibility = View.GONE
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

    private fun getTotalCharge(orderPrice: Float, quantity: Int): Float {

        val brokagevalue = ((quantity * orderPrice) / 10) / 2
        val transValue = (brokagevalue / 10) / 2
        return brokagevalue + transValue

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.buttonBuy -> {
                if (binding.quantityEt.text.toString().toInt() <= 1) {
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