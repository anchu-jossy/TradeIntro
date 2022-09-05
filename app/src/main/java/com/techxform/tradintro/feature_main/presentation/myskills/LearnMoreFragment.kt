package com.techxform.tradintro.feature_main.presentation.myskills

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.LearnMoreBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.Level
import com.techxform.tradintro.feature_main.data.remote.dto.PortfolioItem
import com.techxform.tradintro.feature_main.data.remote.dto.UserDetailsResponse
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import com.techxform.tradintro.feature_main.presentation.portfolio.PortfolioAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LearnMoreFragment :
    BaseFragment<LearnMoreBinding>(LearnMoreBinding::inflate) {
    lateinit var userDetailsResponse: UserDetailsResponse

    private lateinit var viewModel: MySkillsViewModel
    var level: Int? = 0

    private val limit = 10
    private var isLoading = false
    private var noMorePages = false
    private var levelHistoryList: ArrayList<Level> = arrayListOf()
    private var adapter: LearnMoreAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[MySkillsViewModel::class.java]
        viewModel.userLevelsHistory(SearchModel(limit = limit, offset = 0))


        observers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mySkillsRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = binding.mySkillsRV.layoutManager?.itemCount
                val lastVisibleItem =
                    (binding.mySkillsRV.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                if (totalItemCount != null) {
                    if (!isLoading && totalItemCount <= (lastVisibleItem + 5) && !noMorePages) {
                        isLoading = true
                        viewModel.userLevelsHistory(
                            SearchModel(
                                "",
                                limit,
                                null,
                                offset = levelHistoryList.size,
                                0
                            )
                        )
                    }

                }
            }

        })
    }


    fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.userLevelsHistoryLiveData.observe(viewLifecycleOwner) {

            if (it.data.isEmpty() || it.data.size < limit) {
                noMorePages = true
            }
            isLoading = false
            levelHistoryList.addAll(it.data)
            setAdapter()

        }


        viewModel.userLevelsErrorLiveData.observe(viewLifecycleOwner) {
            handleError(it)
        }
    }

    private fun setAdapter()
    {
        if (adapter == null) {
            adapter =  LearnMoreAdapter(levelHistoryList)
            binding.mySkillsRV.adapter = adapter
        } else {
            adapter?.list = levelHistoryList
            adapter?.notifyDataSetChanged()
        }
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