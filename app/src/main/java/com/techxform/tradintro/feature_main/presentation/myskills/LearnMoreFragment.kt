package com.techxform.tradintro.feature_main.presentation.myskills

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.LearnMoreBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.UserDetailsResponse
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LearnMoreFragment :
    BaseFragment<LearnMoreBinding>(LearnMoreBinding::inflate) {
    lateinit var userDetailsResponse: UserDetailsResponse

    private lateinit var viewModel: MySkillsViewModel
    var level:Int?=0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[MySkillsViewModel::class.java]
        viewModel.userLevelsHistory(SearchModel(limit = 10,offset = 0))

        observers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    fun observers(){
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.userLevelsHistoryLiveData.observe(viewLifecycleOwner) {
            binding.mySkillsRV.layoutManager=LinearLayoutManager(requireContext())
            binding.mySkillsRV.adapter = LearnMoreAdapter(it.data)
        }


        viewModel.userLevelsErrorLiveData.observe(viewLifecycleOwner) {
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


}