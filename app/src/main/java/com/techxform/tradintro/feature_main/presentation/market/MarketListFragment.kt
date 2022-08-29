package com.techxform.tradintro.feature_main.presentation.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.MarketFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.Stock
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisibiltyGone
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarketListFragment : BaseFragment<MarketFragmentBinding>(MarketFragmentBinding::inflate),
    AdapterView.OnItemSelectedListener {

    companion object {
        fun newInstance() = MarketListFragment()
    }

    private lateinit var viewModel: MarketViewModel
    private var marketList: ArrayList<Stock> = arrayListOf()
    private val limit = 10
    private var isLoading = false
    private var noMorePages = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[MarketViewModel::class.java]
        observers()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.marketList,
            R.layout.layout_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.catagorySpinner.adapter = adapter
        binding.catagorySpinner.onItemSelectedListener = this

        binding.marketSearchView.addTextChangedListener {
            if(binding.marketSearchView.text.toString().length > 3)
            {
                marketList.clear()
                viewModel.marketList(SearchModel(binding.marketSearchView.text.toString().trim(), limit, null,0, 0))
                binding.marketSearchView.isEnabled = false
            }else if(binding.marketSearchView.text.isNullOrEmpty())
            {
                marketList.clear()
                viewModel.marketList(SearchModel(null, limit, null,0, 0))
                binding.marketSearchView.isEnabled = false
            }
        }




        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (binding.nestedScrollView.getChildAt(0) != null && (binding.nestedScrollView.getChildAt(
                    0
                ).bottom
                        <= (binding.nestedScrollView.height + binding.nestedScrollView.scrollY))
            ) {
                if (!isLoading && !noMorePages) {
                    isLoading = true
                    viewModel.marketList(SearchModel(binding.marketSearchView.text.toString().trim(), limit, null,marketList.size, 0))
                }
            }
        })
        marketList.clear()
        viewModel.marketList(SearchModel("", limit,null, marketList.size, 0))

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val text: String = parent?.getItemAtPosition(position).toString()
    }

    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.marketListLiveData.observe(viewLifecycleOwner) {
            binding.marketSearchView.isEnabled = true
            if (it.data.isEmpty() || it.data.size < limit)
                noMorePages = true
            marketList.addAll(it.data)
            isLoading = false
            setAdapter()
        }

        viewModel.marketErrorLiveData.observe(viewLifecycleOwner) {
            binding.marketSearchView.isEnabled = true
            isLoading = false
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        Toast.makeText(
                            requireContext(), getString(R.string.no_internet_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    )
                }
                else -> {}
            }
        }
    }

    private fun setAdapter() {
        if(marketList.isNullOrEmpty())
        {
            binding.noMarketsTv.setVisible()
            binding.marketListRv.setVisibiltyGone()
        }else if(!marketList.isNullOrEmpty() && binding.marketListRv.visibility == View.GONE)
        {
            binding.marketListRv.setVisible()
            binding.noMarketsTv.setVisibiltyGone()
        }

        binding.marketListRv.adapter =
            MarketListAdapter(marketList, object : MarketListAdapter.OnItemClickListner {
                override fun onItemClick(stock: Stock, position: Int) {
                    val bundle = bundleOf("stockId" to stock.stockId,"totalPrice" to stock.totalPrice)
                    findNavController().navigate(R.id.action_nav_market_to_marketDetailFragment, bundle)
                }
            })
    }


}