package com.techxform.tradintro.feature_main.presentation.report

import android.app.Dialog
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.LayoutDialogBinding
import com.techxform.tradintro.databinding.OrderBookFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.PortfolioItem
import com.techxform.tradintro.feature_main.data.remote.dto.UpdatePortfolioRequest
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderBookFragment :
    BaseFragment<OrderBookFragmentBinding>(OrderBookFragmentBinding::inflate) {

    private lateinit var viewModel: OrderBookViewModel
    private lateinit var portfolioList: ArrayList<PortfolioItem>
    private val limit = 10



    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.portfolioLiveData.observe(viewLifecycleOwner) {
            portfolioList = it.data
            binding.orderBookRv.adapter = OrderBookAdapter(it.data, object :
                OrderBookAdapter.ClickListener {
                override fun onEditBtnClick(portfolio: PortfolioItem) {


                    val displayRectangle = Rect()
                    val window: Window = requireActivity().window
                    window.decorView.getWindowVisibleDisplayFrame(displayRectangle)


                    val binding: LayoutDialogBinding = DataBindingUtil
                        .inflate(LayoutInflater.from(context), R.layout.layout_dialog, null, false)
                    binding.root.minimumWidth = ((displayRectangle.width() * 1f).toInt());
                    binding.root.minimumHeight = (((displayRectangle.height() * 1f).toInt()));


                    binding.portfolio = portfolio
                    val dialog = Dialog(requireContext())
                    dialog.setContentView(binding.root)
                    dialog.show()
                    binding.closeIv.setOnClickListener {
                        dialog.dismiss()
                    }
                    binding.sellBtn.setOnClickListener {
                        dialog.dismiss()
                        viewModel.updatePortfolio(
                            portfolio.orderId,
                            UpdatePortfolioRequest(
                                binding.TransPriceET.text.toString().toFloat(),
                                binding.quantityET.text.toString().toFloat()
                            )
                        )
                    }


                }

                override fun onDeleteBtnClick(portfolio: PortfolioItem) {
                    viewModel.deletePortfolio(portfolio.orderId)

                }

            })
        }
        viewModel.updatePortfolioLiveData.observe(viewLifecycleOwner) {
            requireContext().showShortToast(getString(R.string.updated_sucess))
        }
        viewModel.deletePortfolioLiveData.observe(viewLifecycleOwner) {
            binding.titleTv.text = getString(R.string.trade_book_lbl)
            viewModel.portfolioList(
                SearchModel(
                    "",
                    limit,
                    0,
                    0,
                    orderStatus = "0,1,2,3,4",
                    portfolioStatus = "0,1"
                )
            )
requireContext().showShortToast(getString(R.string.delete_success))

        }


        viewModel.portfolioErrorLiveData.observe(viewLifecycleOwner) {
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        requireContext().showShortToast(getString(R.string.no_internet_error))

                    )
                }
                else -> {
                    val errorMsg = (it as Failure.FeatureFailure).message
                    sequenceOf(
                        requireContext().showShortToast( "Error: $errorMsg")

                    )


                }
            }

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[OrderBookViewModel::class.java]
        observers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isTradeBook = requireArguments().getBoolean(IsTradeBook)

        if (isTradeBook) {
            binding.titleTv.text = getString(R.string.trade_book_lbl)
            viewModel.portfolioList(SearchModel("", limit, 0, 0, orderStatus = "0", portfolioStatus = "0,1"))

        } else {
            binding.titleTv.text = getString(R.string.order_book_lbl)
            viewModel.portfolioList(SearchModel("", limit, 0, 0, orderStatus = "0,1,2,3,4", portfolioStatus = "0,1"))
        }

    }


    companion object {

        private const val IsTradeBook = "isTradeBook"

        @JvmStatic
        fun newInstance(isTradeBook: Boolean) =
            OrderBookFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IsTradeBook, isTradeBook)
                }
            }
    }

}