package com.techxform.tradintro.feature_main.presentation.landing

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarMenu
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.core.utils.PreferenceHelper
import com.techxform.tradintro.core.utils.PreferenceHelper.fcmToken
import com.techxform.tradintro.core.utils.PreferenceHelper.isFcmTokenSync
import com.techxform.tradintro.core.utils.UserDetailsSingleton
import com.techxform.tradintro.databinding.FragmentLandingBinding
import com.techxform.tradintro.feature_main.data.remote.FcmTokenRegReq
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.data.remote.dto.LogOutRequest
import com.techxform.tradintro.feature_main.data.remote.dto.Result
import com.techxform.tradintro.feature_main.domain.model.DrawerItem
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import com.techxform.tradintro.feature_main.presentation.SplashScreenActivity
import com.techxform.tradintro.feature_main.presentation.notification.NotificationFragment
import com.techxform.tradintro.feature_main.presentation.notification.NotificationFragment.Companion.ALERT_TYPE
import com.techxform.tradintro.feature_main.presentation.notification.NotificationFragment.Companion.NEWS_TYPE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class LandingFragment : BaseFragment<FragmentLandingBinding>(FragmentLandingBinding::inflate),
    Toolbar.OnMenuItemClickListener {

    private lateinit var navController: NavController
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var repository: ApiRepository
    private lateinit var pref: SharedPreferences

    private lateinit var viewModel: LandingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        bottomNavSetup()
        viewModel.userDetails(requireContext())
        pref = PreferenceHelper.customPreference(requireContext())
        if (!pref.fcmToken.isNullOrEmpty() && !pref.isFcmTokenSync) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { t1 ->
                sendRegistrationToServer(t1.result)
            }
        }
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.logOutLiveData.observe(viewLifecycleOwner) {
            if (it.status) {
                try {
                    PreferenceHelper.customPreference(requireContext()).edit().clear().apply()
                } catch (ignore: Exception) {

                }
                requireContext().showShortToast("Logout successfully")
                startActivity(Intent(requireContext(), SplashScreenActivity::class.java))
            }
        }
        viewModel.logOutErrorLiveData.observe(viewLifecycleOwner) {
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        requireContext().showShortToast(getString(R.string.no_internet_error))
                    )
                }
                Failure.ServerError -> {
                    requireContext().showShortToast(getString(R.string.server_error))
                }
                else -> {
                }
            }
        }

        viewModel.userDetailLiveData.observe(viewLifecycleOwner) {
            (binding.bottomNav.menu as NavigationBarMenu).visibleItems[3].isVisible =
                it.data.treeLevel != 1
            drawerSetup()
            binding.drawerLayout.close()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun IMEI() {
        var myIMEI: String? = null
        try {
            val tm =
                requireActivity().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            val IMEI = tm.imei
            if (IMEI != null) {

                Log.d("testing", IMEI)
                myIMEI = IMEI
//                show_IMEI.setText(myIMEI)
//                show_IMEI.setOnClickListener{
//
//                    Toast.makeText(this, myIMEI,Toast.LENGTH_SHORT ).show()
//
//                }
            }
        } catch (ex: Exception) {
            requireContext().showShortToast(ex.toString())

        }

        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.READ_PHONE_STATE
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    android.Manifest.permission.READ_PHONE_STATE
                )
            ) {

            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.READ_PHONE_STATE),
                    2
                )

            }
        }
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

        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
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
        override fun onClick(item: DrawerItem) {
            when (item.name) {
                getString(R.string.my_profile_lbl) -> navController.navigate(R.id.nav_profile)
                getString(R.string.my_referrals_lbl) -> navController.navigate(R.id.myReferalFragment)
                getString(R.string.my_skill_board_lbl) -> navController.navigate(R.id.mySkillsFragment)
                getString(R.string.recharge_summary_lbl) -> navController.navigate(R.id.rechargeFragment)
                getString(R.string.recharge_trade_money_lbl) -> navController.navigate(R.id.rechargeTradeMoneyFragment)
                getString(R.string.notification_lbl) -> {
                    navController.navigateUp()
                    navController.navigate(
                        R.id.notificationFragment,
                        NotificationFragment.navBundle(NEWS_TYPE)
                    )
                }
                getString(R.string.reports_lbl) -> navController.navigate(R.id.reportFragment)
                getString(R.string.alerts_lbl) -> {
                    navController.navigateUp()
                    navController.navigate(
                        R.id.notificationFragment,
                        NotificationFragment.navBundle(ALERT_TYPE)
                    )
                }
                getString(R.string.change_password_lbl) ->
                    navController.navigate(R.id.changePasswordFragment)
                getString(R.string.logout_lbl) -> showLogoutConformationDialog()


            }
            binding.drawerLayout.close()

        }
    }

    private fun showLogoutConformationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Logout Account")
        //set message for alert dialog
        builder.setMessage("Are you sure want to logout the account?")
        builder.setIcon(android.R.drawable.ic_menu_close_clear_cancel)
        builder.setPositiveButton("Logout") { dialogInterface, _ ->
            dialogInterface.dismiss()
            viewModel.logOut(LogOutRequest("990719377109589", "mobile "))
        }
        //performing negative action
        builder.setNegativeButton("Cancel") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
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
        if (UserDetailsSingleton.userDetailsResponse.treeLevel != 1)
            list.add(DrawerItem(getString(R.string.alerts_lbl), R.drawable.ic_alerts))
        list.add(DrawerItem(getString(R.string.change_password_lbl), R.drawable.change_pass))
        list.add(DrawerItem(getString(R.string.logout_lbl), R.drawable.ic_logout))
        return list
    }

    fun redirectToNotification(type: String = "") {
        val navGraph = navController.navInflater.inflate(R.navigation.bottom_nav_graph)
        when (type.lowercase(Locale.getDefault())) {
            getString(R.string.nav_portfolio).lowercase(Locale.getDefault()) -> navGraph.setStartDestination(
                R.id.nav_home
            )
            getString(R.string.nav_market).lowercase(Locale.getDefault()) -> navGraph.setStartDestination(
                R.id.nav_market
            )
            getString(R.string.nav_watchlist).lowercase(Locale.getDefault()) -> navGraph.setStartDestination(
                R.id.nav_watchlist
            )
            else -> navGraph.setStartDestination(R.id.notificationFragment)
        }
        navController.graph = navGraph
    }

    private fun bottomNavSetup() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.container_fragment) as NavHostFragment
        navController = navHostFragment.navController
        if (requireActivity().intent?.getBooleanExtra(
                SplashScreenActivity.IS_NOTIFICATION,
                false
            ) == true || requireArguments().getBoolean(SplashScreenActivity.IS_NOTIFICATION, false)
        ) {
            val type =
                (requireActivity().intent?.getStringExtra(SplashScreenActivity.NOTIFICATION_TYPE)
                    ?: requireArguments().getString(SplashScreenActivity.NOTIFICATION_TYPE)) as String
                    ?: ""
            redirectToNotification(type)
        }


        binding.bottomNav.setupWithNavController(navController)

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    navController.popBackStack(R.id.nav_home1, true)
                    navController.navigate(R.id.nav_home1)
                }
                R.id.nav_portfolio -> {
                    navController.navigate(R.id.nav_portfoliolist)
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
//
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
                R.id.action_wallet -> {
                    navController.navigate(R.id.walletFragment)
                    true
                }
                R.id.action_notification -> {
                    navController.navigate(
                        R.id.notificationFragment, NotificationFragment.navBundle(
                            NEWS_TYPE
                        )
                    )
                    true
                }
                else -> super.onOptionsItemSelected(item)

            }
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[LandingViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    private fun sendRegistrationToServer(token: String) {
        val device = (Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES::class.java.fields[Build.VERSION.SDK_INT].name)

        /*The Firebase installations service (FIS) provides a Firebase installation ID (FID) for each installed instance of a Firebase app.
        FIDs provide better privacy properties compared to non-resettable, device-scoped hardware IDs. For more information, see the firebase.installations API reference.*/
        FirebaseInstallations.getInstance().id.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Installations", "Installation ID: " + task.result)
                val req = FcmTokenRegReq(token, task.result, device)
                scope.launch() {
                    when (repository.fcmTokenRegistration(req)) {
                        is Result.Success -> {
                            pref.isFcmTokenSync = true
                        }
                        is Result.Error -> {
                            Log.e("Error", "FCM registration Error")
                        }
                    }
                }
            } else {
                Log.e("Installations", "Unable to get Installation ID")
            }
        }
    }

}