package com.techxform.tradintro.feature_main.presentation.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.FragmentSplashBinding


class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController(this).navigate(R.id.action_splashFragment_to_landingFragment)

        }, UI_ANIMATION_DELAY.toLong())
    }



    companion object {
        private const val UI_ANIMATION_DELAY = 2000
    }


}