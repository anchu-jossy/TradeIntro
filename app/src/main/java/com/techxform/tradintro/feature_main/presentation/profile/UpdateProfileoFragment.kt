package com.techxform.tradintro.feature_main.presentation.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.PortfolioFragmentBinding
import com.techxform.tradintro.databinding.UpdateProfileFragmentBinding

class UpdateProfileoFragment : BaseFragment<UpdateProfileFragmentBinding>(UpdateProfileFragmentBinding::inflate) {

    companion object {
        fun newInstance() = UpdateProfileoFragment()
    }




    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}