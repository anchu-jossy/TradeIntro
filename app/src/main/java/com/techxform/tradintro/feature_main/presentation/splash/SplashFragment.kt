package com.techxform.tradintro.feature_main.presentation.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.core.utils.PreferenceHelper
import com.techxform.tradintro.core.utils.PreferenceHelper.token
import com.techxform.tradintro.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private var isLoggedIn: Boolean = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = PreferenceHelper.customPreference(requireContext())
        isLoggedIn = pref.token!!.isNotEmpty()


//TODO: Refresh Token Functionality

        Handler(Looper.getMainLooper()).postDelayed({
            if (isLoggedIn)
                findNavController(this).navigate(R.id.action_splashFragment_to_loginFragment)
            else findNavController(this).navigate(R.id.action_splashFragment_to_loginFragment)

        }, UI_ANIMATION_DELAY.toLong())
    }



    companion object {
        private const val UI_ANIMATION_DELAY = 2000
    }


}