package com.techxform.tradintro.feature_main.presentation.home

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
import com.techxform.tradintro.databinding.OriginalHomeFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OriginalHomeFragment :
    BaseFragment<OriginalHomeFragmentBinding>(OriginalHomeFragmentBinding::inflate) {


    private lateinit var viewModel: OriginalHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[OriginalHomeViewModel::class.java]

        observers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            cardContainerPortfolio.setOnClickListener {
                findNavController().navigate(R.id.nav_home)
            }
            cardContainerMarket.setOnClickListener {
                findNavController().navigate(R.id.nav_market)
            }
            cardContainerProfile.setOnClickListener {
                findNavController().navigate(R.id.nav_profile)
            }
            cardContainerWallet.setOnClickListener {
                findNavController().navigate(R.id.walletFragment)
            }

        }
        viewModel.userDashboard()

        /*       val face: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.open_sans)
               val searchText = binding.searchView as TextView
               searchText.typeface = face*/

    }

    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.userDashboardLiveData.observe(viewLifecycleOwner) {
            binding.userDashboard = it.data
        }

        viewModel.userDashboardErrorLiveData.observe(viewLifecycleOwner) {
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