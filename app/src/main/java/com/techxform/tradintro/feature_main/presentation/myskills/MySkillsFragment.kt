package com.techxform.tradintro.feature_main.presentation.myskills

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.MySkillsBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.Levels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MySkillsFragment :
    BaseFragment<MySkillsBinding>(MySkillsBinding::inflate) {

    private lateinit var viewModel: MySkillsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[MySkillsViewModel::class.java]
        observers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.userLevels()

    }

    private val rvListener = object : MySkillsAdapter.ClickListener {
        override fun onItemClick(position: Int, levels: Levels) {
            findNavController().navigate(R.id.mySkillsViewFragment, MySkillsViewFragment.navBundle(levels))
        }

    }

    fun observers(){
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.userLevelsLiveData.observe(viewLifecycleOwner) {
            binding.levelImage = it.data.levels.find { it1 -> it1.levelPosition == it.data.myLevel }?.userLevelImage?: ""
            binding.textViewLevelDesc.text = String.format(getString(R.string.level_desc), it.data.myLevel)
            binding.textViewPoints.text = getString(R.string.current_point) + String.format(getString(R.string.points_format), it.data.myPoints)
            binding.textViewLevel.text = String.format(getString(R.string.level_format), it.data.myLevel)
            binding.mySkillsRV.adapter = MySkillsAdapter(it.data.levels, rvListener)

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