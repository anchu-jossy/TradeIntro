package com.techxform.tradintro.feature_main.presentation.landing

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.FragmentLandingBinding
import com.techxform.tradintro.feature_main.domain.model.DrawerItem


class LandingFragment : BaseFragment<FragmentLandingBinding>(FragmentLandingBinding::inflate),
    Toolbar.OnMenuItemClickListener {

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        bottomNavSetup()
        drawerSetup()


    }

    private fun drawerSetup() {

        binding.toolbar.setOnMenuItemClickListener(this)
        binding.header.backTv.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        val mDrawerToggle = object : ActionBarDrawerToggle(
            requireActivity(),
            binding.drawerLayout,
            binding.toolbar,
            R.string.open_lbl,
            R.string.close_lbl
        ) {}

        binding.drawerLayout.addDrawerListener(mDrawerToggle)
        mDrawerToggle.isDrawerIndicatorEnabled = false

        mDrawerToggle.setHomeAsUpIndicator(R.drawable.logo_small)
        mDrawerToggle.toolbarNavigationClickListener = View.OnClickListener {
            if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        mDrawerToggle.syncState()
        binding.drawerRv.adapter = DrawerAdapter(createDrawerItems(), listener)
        val appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        navController.navigateUp(appBarConfiguration)
    }

    private val listener = object : DrawerAdapter.ClickListener {
        override fun onClick(position: Int) {

            when (position) {
                0 -> navController.navigate(R.id.rechargeTradeMoneyFragment)
                1 -> navController.navigate(R.id.rechargeTradeMoneyFragment)
                2 -> navController.navigate(R.id.mySkillsFragment)
                3 -> navController.navigate(R.id.rechargeTradeMoneyFragment)
                4 -> navController.navigate(R.id.rechargeTradeMoneyFragment)
                5 -> navController.navigate(R.id.notificationFragment)
                6 -> navController.navigate(R.id.rechargeTradeMoneyFragment)
                7 -> navController.navigate(R.id.rechargeTradeMoneyFragment)
                8 -> navController.navigate(R.id.changePasswordFragment)
                else -> navController.navigate(R.id.rechargeTradeMoneyFragment)
            }
            binding.drawerLayout.close()

        }
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
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.nav_home1)
                }
                R.id.nav_portfolio -> {
                    navController.navigate(R.id.nav_home)
                }
                R.id.nav_market -> {
                    navController.navigate(R.id.nav_market)
                }
                R.id.nav_watchlist -> {
                    navController.navigate(R.id.nav_watchlist)
                }
                R.id.nav_trade -> {
                    navController.navigate(R.id.nav_trade)
                }
                R.id.nav_profile -> {
                    navController.navigate(R.id.nav_profile)
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


    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item != null)
            return when (item.itemId) {
                R.id.action_search -> true
                R.id.action_wallet -> true
                R.id.action_notification -> true
                else -> super.onOptionsItemSelected(item)

            }
        return false
    }

}