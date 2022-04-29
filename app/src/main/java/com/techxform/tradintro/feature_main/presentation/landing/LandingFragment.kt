package com.techxform.tradintro.feature_main.presentation.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.techxform.tradintro.R
import com.techxform.tradintro.base.BaseFragment


class LandingFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_landing, container, false)

        val bottomNav = v.findViewById<BottomNavigationView>(R.id.bottom_nav)


        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.container_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNav.setupWithNavController(navController)


        bottomNav.setOnItemSelectedListener {
            when(it.itemId)
            {
                R.id.nav_home -> { navController.navigate(R.id.nav_home) }
                R.id.nav_market -> { navController.navigate(R.id.nav_market)}
                R.id.nav_portfolio -> { navController.navigate(R.id.nav_portfolio)}
                R.id.nav_watchlist -> { navController.navigate(R.id.nav_watchlist)}
                R.id.nav_trade -> { navController.navigate(R.id.nav_trade)}
                else -> { navController.navigate(R.id.nav_home) }
            }
            it.isChecked = true
            return@setOnItemSelectedListener true
        }

        return v
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