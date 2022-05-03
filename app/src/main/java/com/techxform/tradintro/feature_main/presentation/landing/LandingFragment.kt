package com.techxform.tradintro.feature_main.presentation.landing

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment


class LandingFragment : BaseFragment(){

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_landing, container, false)

        bottomNav = v.findViewById(R.id.bottom_nav)
        manageBottomNavVisiblity()




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

    private fun manageBottomNavVisiblity()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view?.setWindowInsetsAnimationCallback(object : WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
                override fun onProgress(
                    p0: WindowInsets,
                    p1: MutableList<WindowInsetsAnimation>
                ): WindowInsets {
                    return  p0
                }

                override fun onEnd(animation: WindowInsetsAnimation) {
                    super.onEnd(animation)
                    val showingKeyboard = view!!.rootWindowInsets.isVisible(WindowInsets.Type.ime())
                    // now use the boolean for something
                    bottomNav.isVisible = !showingKeyboard
                }
            })
        }else {
            val view = requireActivity().window.decorView
            ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
                val showingKeyboard = insets.isVisible(WindowInsetsCompat.Type.ime())
                bottomNav.isVisible = !showingKeyboard
                insets
            }
        }
    }
}