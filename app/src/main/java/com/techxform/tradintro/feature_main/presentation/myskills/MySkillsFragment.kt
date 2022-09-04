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
import com.techxform.tradintro.core.utils.UserDetailsSingleton
import com.techxform.tradintro.databinding.MySkillsBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.Levels
import com.techxform.tradintro.feature_main.data.remote.dto.UserDetailsResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MySkillsFragment :
    BaseFragment<MySkillsBinding>(MySkillsBinding::inflate) {
    lateinit var userDetailsResponse: UserDetailsResponse

    private lateinit var viewModel: MySkillsViewModel
    var level: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[MySkillsViewModel::class.java]
        viewModel.userLevels()
        observers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        this.userDetailsResponse = UserDetailsSingleton.userDetailsResponse
        with(userDetailsResponse) {
            binding.levelImage = userLevel.user_level_image
            binding.textViewLevelDesc.text =
                String.format(getString(R.string.level_desc), treeLevel)
            binding.textViewLevel.text = String.format(getString(R.string.level_format), treeLevel)

        }
        binding.textViewLearn.setOnClickListener {
            findNavController().navigate(R.id.learnMoreFragment)
        }
    }

    private val rvListener = object : MySkillsAdapter.ClickListener {
        override fun onItemClick(position: Int, levels: Levels, myLevel: Int?) {
            levels.userLevel = myLevel
            findNavController().navigate(
                R.id.mySkillsViewFragment,
                MySkillsViewFragment.navBundle(levels)
            )
        }

    }

    fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.userLevelsLiveData.observe(viewLifecycleOwner) {

            binding.textViewPoints.text = getString(R.string.current_point) + String.format(
                getString(R.string.points_format),
                it.data.myPoints
            )
            binding.mySkillsRV.adapter = MySkillsAdapter(it.data.levels, rvListener, this.level)

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