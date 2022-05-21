package com.techxform.tradintro.feature_main.presentation.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.OriginalHomeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrginalHomeFragment :
    BaseFragment<OriginalHomeFragmentBinding>(OriginalHomeFragmentBinding::inflate) {


    private lateinit var viewModel: HomeViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.vm = viewModel
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

        /*       val face: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.open_sans)
               val searchText = binding.searchView as TextView
               searchText.typeface = face*/

//        binding.portfolioRv.adapter = PortfolioAdapter(arrayListOf(),rvListener)

    }

    private val rvListener = object : PortfolioAdapter.ClickListener {
        override fun onItemClick(position: Int) {
            findNavController().navigate(R.id.action_nav_home_to_portfolioViewFragment)
        }

    }


}