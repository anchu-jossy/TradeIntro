package com.techxform.tradintro.feature_main.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.core.utils.Contants.IMAGE_URL
import com.techxform.tradintro.databinding.UpdateProfileFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.EditUserProfileReq
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateProfileFragment : BaseFragment<UpdateProfileFragmentBinding>(UpdateProfileFragmentBinding::inflate) {

    companion object {
        fun newInstance() = UpdateProfileFragment()
    }

    private lateinit var viewModel: UpdateProfileViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[UpdateProfileViewModel::class.java]
        viewModel.userDetails()
        isEnableDisable(false)
        viewModel.userDetailLiveData.observe(viewLifecycleOwner) { it ->
            binding.sellBtn.visibility = View.GONE
            isEnableDisable(false)
            it.data.let { data ->
                binding.userDetail = data
                data.userImage?.let { image->
                    Glide.with(requireContext())
                        .load(IMAGE_URL+image)
                        .into(binding.roundedimage);
                }


            }
        }
        binding.editBtn.setOnClickListener {
            isEnableDisable(true)
            binding.sellBtn.visibility = View.VISIBLE
        }
        binding.sellBtn.setOnClickListener {
            viewModel.editUser(EditUserProfileReq(null, binding.userNameET.text.toString(),binding.userLastNameET.text.toString(),
            binding.userPhoneET.text.toString()))
        }

    }

    private fun isEnableDisable(isEnable:Boolean)
    {
        binding.userNameET.isEnabled = isEnable
        binding.userLastNameET.isEnabled = isEnable
        binding.userPhoneET.isEnabled = isEnable
        binding.roundedimage.isEnabled = isEnable
    }

}