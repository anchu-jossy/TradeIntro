package com.techxform.tradintro.feature_main.presentation.market

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.MarketFragmentBinding


class MarketListFragment : BaseFragment<MarketFragmentBinding>(MarketFragmentBinding::inflate),
    AdapterView.OnItemSelectedListener {

    companion object {
        fun newInstance() = MarketListFragment()
    }

    private lateinit var viewModel: MarketViewModel
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
        binding.marketSearchView.queryHint = getString(R.string.search)
        binding.marketListRv.adapter =
            MarketListAdapter(arrayListOf(), object : MarketListAdapter.onItemClickListner {
                override fun onItemClick() {
                    findNavController().navigate(R.id.action_nav_market_to_marketDetailFragment)
                }
            })
        binding.marketSearchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                return false
            }

        })

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val text: String = parent?.getItemAtPosition(position).toString()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MarketViewModel::class.java)
        // TODO: Use the ViewModel
    }

}