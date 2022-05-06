package com.techxform.tradintro.feature_main.presentation.landing

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.FragmentLandingBinding
import com.techxform.tradintro.feature_main.domain.model.DrawerItem


class LandingFragment : BaseFragment<FragmentLandingBinding>(FragmentLandingBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavSetup()
        drawerSetup()


    }

    private fun drawerSetup() {
        binding.drawerRv.adapter = DrawerAdapter(createDrawerItems())
    }

    private fun createDrawerItems(): ArrayList<DrawerItem> {
        val list = arrayListOf<DrawerItem>()

        list.add(DrawerItem(getString(R.string.my_profile_lbl), R.drawable.ic_my_profile))
        list.add(DrawerItem(getString(R.string.my_referrals_lbl), R.drawable.my_referrals))
        list.add(DrawerItem(getString(R.string.my_skill_board_lbl), R.drawable.my_skill_board))
        list.add(DrawerItem(getString(R.string.recharge_summary_lbl), R.drawable.recharge_summary))
        list.add(
            DrawerItem(
                getString(R.string.recharge_trade_money_lbl),
                R.drawable.recharge_trade_money
            )
        )
        list.add(DrawerItem(getString(R.string.notification_lbl), R.drawable.ic_notification))
        list.add(DrawerItem(getString(R.string.reports_lbl), R.drawable.reports))
        list.add(DrawerItem(getString(R.string.alerts_lbl), R.drawable.ic_alerts))
        list.add(DrawerItem(getString(R.string.change_password_lbl), R.drawable.change_pass))
        list.add(DrawerItem(getString(R.string.logout_lbl), R.drawable.ic_logout))
        return list
    }

    private fun bottomNavSetup() {
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
        manageBottomNavVisiblity()
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LandingFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun manageBottomNavVisiblity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view?.setWindowInsetsAnimationCallback(object :
                WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
                override fun onProgress(
                    p0: WindowInsets,
                    p1: MutableList<WindowInsetsAnimation>
                ): WindowInsets {
                    return p0
                }

                override fun onEnd(animation: WindowInsetsAnimation) {
                    super.onEnd(animation)
                    val showingKeyboard = view!!.rootWindowInsets.isVisible(WindowInsets.Type.ime())
                    // now use the boolean for something
                    binding.bottomNav.isVisible = !showingKeyboard
                }
            })
        } else {
            val view = requireActivity().window.decorView
            ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
                val showingKeyboard = insets.isVisible(WindowInsetsCompat.Type.ime())
                binding.bottomNav.isVisible = !showingKeyboard
                insets
            }
        }
    }
}