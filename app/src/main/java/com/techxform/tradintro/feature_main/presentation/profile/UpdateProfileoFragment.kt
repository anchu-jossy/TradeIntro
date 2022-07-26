package com.techxform.tradintro.feature_main.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.UpdateProfileFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateProfileoFragment : BaseFragment<UpdateProfileFragmentBinding>(UpdateProfileFragmentBinding::inflate) {

    companion object {
        fun newInstance() = UpdateProfileoFragment()
    }

    private lateinit var viewModel: UpdateProfileViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[UpdateProfileViewModel::class.java]
        viewModel.userDetails()
        viewModel.userDetailLiveData.observe(viewLifecycleOwner) { it ->

            it.data?.let { data ->
                binding.userDetail = data
               data.userImage?.let { image->
                   Glide.with(requireContext())
                       .load(image)
                       .into(binding.roundedimage);
               } 


            }
        }

    }

}