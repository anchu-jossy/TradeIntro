package com.techxform.tradintro.feature_main.presentation.portfolio

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.PortfolioItem
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import dagger.hilt.android.AndroidEntryPoint
import com.techxform.tradintro.databinding.PortfolisFragmentBinding

@AndroidEntryPoint
class PortfoliosFragment :
    BaseFragment<PortfolisFragmentBinding>(PortfolisFragmentBinding::inflate) {


    private lateinit var viewModel: PortfolisViewModel
    private var portfolioList = ArrayList<PortfolioItem>()
    private lateinit var adapter: PortfolioAdapter

    private val limit = 10
    private var isLoading = false
    private var noMorePages = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[PortfolisViewModel::class.java]
        binding.vm = viewModel
        /*       val face: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.open_sans)
               val searchText = binding.searchView as TextView
               searchText.typeface = face*/

        binding.searchView.queryHint = getString(R.string.search)
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    portfolioList.clear()
                    viewModel.portfolioList(SearchModel("", limit, 0, 0))
                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                portfolioList.clear()
                viewModel.portfolioList(SearchModel(query.trim(), limit, 0, 0))
                return false
            }

        })

        observers()
        viewModel.portfolioDashboard()
        viewModel.portfolioList(SearchModel("", limit, portfolioList.size, 0))

    }

    private val rvListener = object: PortfolioAdapter.ClickListener{
        override fun onItemClick(portfolioItem: PortfolioItem, position: Int) {
            val bundle = bundleOf("orderId" to portfolioItem.orderId,
            "portfolioDashboard" to binding.portfolioDashboard)
            findNavController().navigate(R.id.action_nav_home_to_portfolioViewFragment, bundle)
        }

    }

    private fun observers() {
        binding.portfolioRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = binding.portfolioRv.layoutManager?.itemCount
                val lastVisibleItem =
                    (binding.portfolioRv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                if (totalItemCount != null) {
                    if (!isLoading && totalItemCount <= (lastVisibleItem + 5) && !noMorePages) {
                        isLoading = true
                        viewModel.portfolioList(SearchModel("", limit, portfolioList.size, 0))
                    }

                }
            }

        })

        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.portfolioLiveData.observe(viewLifecycleOwner) {

            if(it.data.isEmpty() || it.data.size < limit)
                noMorePages = true
            portfolioList.addAll(it.data)
            isLoading = false
            setAdapter()
        }

        viewModel.portfolioDashboardLiveData.observe(viewLifecycleOwner) {
            binding.portfolioDashboard = it.data
        }

        viewModel.portfolioErrorLiveData.observe(viewLifecycleOwner) {
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
        viewModel.portfolioDashboardErrorLiveData.observe(viewLifecycleOwner) {
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
        if(!::adapter.isInitialized) {
            adapter = PortfolioAdapter(portfolioList, rvListener)
            binding.portfolioRv.adapter = adapter
        }else {
            adapter.list = portfolioList
            adapter.notifyDataSetChanged()
        }
    }

}