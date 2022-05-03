package com.techxform.tradintro.feature_main.presentation.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.FragmentLandingBinding


class LandingFragment : BaseFragment<FragmentLandingBinding>(FragmentLandingBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.container_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.nav_home)
                }
                R.id.nav_market -> {
                    navController.navigate(R.id.nav_market)
                }
                R.id.nav_portfolio -> {
                    navController.navigate(R.id.nav_portfolio)
                }
                R.id.nav_watchlist -> {
                    navController.navigate(R.id.nav_watchlist)
                }
                R.id.nav_trade -> {
                    navController.navigate(R.id.nav_trade)
                }
                else -> {
                    navController.navigate(R.id.nav_home)
                }
            }
            it.isChecked = true
            return@setOnItemSelectedListener true
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LandingFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}