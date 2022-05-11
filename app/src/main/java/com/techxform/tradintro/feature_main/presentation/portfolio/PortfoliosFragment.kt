package com.techxform.tradintro.feature_main.presentation.portfolio

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.PortfolisFragmentBinding
import com.techxform.tradintro.feature_main.presentation.home.HomeViewModel

class PortfoliosFragment :
    BaseFragment<PortfolisFragmentBinding>(PortfolisFragmentBinding::inflate) {


    private lateinit var viewModel: HomeViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.vm = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*       val face: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.open_sans)
               val searchText = binding.searchView as TextView
               searchText.typeface = face*/

        binding.portfolioRv.adapter = PortfolioAdapter(arrayListOf(), rvListener)

    }

    private val rvListener = object : PortfolioAdapter.ClickListener {
        override fun onItemClick(position: Int) {
            findNavController().navigate(R.id.action_nav_home_to_portfolioViewFragment)
        }

    }


}