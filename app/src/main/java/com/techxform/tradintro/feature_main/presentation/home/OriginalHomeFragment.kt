package com.techxform.tradintro.feature_main.presentation.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.OriginalHomeFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.PortfolioItem
import com.techxform.tradintro.feature_main.presentation.portfolio.PortfolioAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OriginalHomeFragment :
    BaseFragment<OriginalHomeFragmentBinding>(OriginalHomeFragmentBinding::inflate) {


    private lateinit var viewModel: OriginalHomeViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[OriginalHomeViewModel::class.java]
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
        observers()
        viewModel.userDashboard()

        /*       val face: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.open_sans)
               val searchText = binding.searchView as TextView
               searchText.typeface = face*/

//        binding.portfolioRv.adapter = PortfolioAdapter(arrayListOf(),rvListener)

    }

    private fun observers()
    {
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

    private val rvListener = object : PortfolioAdapter.ClickListener {

        override fun onItemClick(portfolioItem: PortfolioItem, position: Int) {
            findNavController().navigate(R.id.action_nav_home_to_portfolioViewFragment)
        }

    }


}