package com.techxform.tradintro.feature_main.presentation.splash

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.core.utils.PreferenceHelper
import com.techxform.tradintro.core.utils.PreferenceHelper.fcmToken
import com.techxform.tradintro.core.utils.PreferenceHelper.token
import com.techxform.tradintro.databinding.FragmentSplashBinding
import com.techxform.tradintro.feature_main.data.remote.FcmTokenRegReq
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private var isLoggedIn: Boolean = false
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    @Inject
    lateinit var repository: ApiRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = PreferenceHelper.customPreference(requireContext())
        isLoggedIn = pref.token!!.isNotEmpty()

        if(pref.fcmToken.isNullOrEmpty())
        {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { t1 ->
                sendRegistrationToServer(t1.result)
            }
        }

//TODO: Refresh Token Functionality

        Handler(Looper.getMainLooper()).postDelayed({
            if (isLoggedIn)
                findNavController(this).navigate(R.id.action_splashFragment_to_loginFragment)
            else findNavController(this).navigate(R.id.action_splashFragment_to_loginFragment)

        }, UI_ANIMATION_DELAY.toLong())
    }

    private fun sendRegistrationToServer(token: String) {
        val device = (Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES::class.java.fields[Build.VERSION.SDK_INT].name)

        FirebaseInstallations.getInstance().id.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Installations", "Installation ID: " + task.result)
                val pref = PreferenceHelper.customPreference(requireContext())
                pref.fcmToken = task.result
                val req = FcmTokenRegReq(token, task.result, device)
                scope.launch() {
                    repository.fcmTokenRegistration(req)
                }
            } else {
                Log.e("Installations", "Unable to get Installation ID")
            }
        }
    }

    companion object {
        private const val UI_ANIMATION_DELAY = 2000
    }


}