package com.techxform.tradintro.feature_main.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.OriginalHomeFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OriginalHomeFragment :
    BaseFragment<OriginalHomeFragmentBinding>(OriginalHomeFragmentBinding::inflate) {


    private lateinit var viewModel: OriginalHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[OriginalHomeViewModel::class.java]

        observers()

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner, onBackPressedCallback
        )
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (getFragment() is OriginalHomeFragment) {
                showCloseDialog()
            } else findNavController().navigateUp()
        }
    }

    private fun showCloseDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Exit TradIntro")
        //set message for alert dialog
        builder.setMessage("Are you sure want to close the application?")
        builder.setIcon(android.R.drawable.ic_menu_close_clear_cancel)
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            dialogInterface.dismiss()
            activity?.finish()
        }
        //performing negative action
        builder.setNegativeButton("Not now") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            cardContainerPortfolio.setOnClickListener {
                findNavController().navigate(R.id.nav_portfoliolist)
            }
            cardContainerMarket.setOnClickListener {
                findNavController().navigate(R.id.nav_market)
            }
            cardContainerProfile.setOnClickListener {
                findNavController().navigate(R.id.nav_profile)
            }
            cardContainerWallet.setOnClickListener {
                findNavController().navigate(R.id.walletFragment)
            }

        }
        viewModel.userDashboard()

        // binding.myPortfolioLbl.text="Welcome "+ PreferenceHelper.customPreference(requireContext()).userFullName

        /*       val face: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.open_sans)
               val searchText = binding.searchView as TextView
               searchText.typeface = face*/

    }

    private fun observers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.progressOverlay.isVisible = it
        }

        viewModel.userDashboardLiveData.observe(viewLifecycleOwner) {
            binding.userDashboard = it.data
        }

        viewModel.userDashboardErrorLiveData.observe(viewLifecycleOwner) {
            when (it) {
                Failure.NetworkConnection -> {
                    sequenceOf(
                        requireContext().showShortToast(getString(R.string.no_internet_error))
                    )
                }
                else -> {}
            }
        }
    }


}