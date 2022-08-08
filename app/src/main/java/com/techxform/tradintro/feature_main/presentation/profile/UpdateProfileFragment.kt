package com.techxform.tradintro.feature_main.presentation.profile

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.core.utils.Contants.IMAGE_URL
import com.techxform.tradintro.core.utils.Contants.delete
import com.techxform.tradintro.core.utils.Contants.update
import com.techxform.tradintro.databinding.UpdateProfileFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.EditUserProfileReq
import com.techxform.tradintro.feature_main.presentation.splash.SplashFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateProfileFragment :
    BaseFragment<UpdateProfileFragmentBinding>(UpdateProfileFragmentBinding::inflate) {

    companion object {
        fun newInstance() = UpdateProfileFragment()
        private const val UI_ANIMATION_DELAY = 2000

    }

    private lateinit var viewModel: UpdateProfileViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[UpdateProfileViewModel::class.java]
        viewModel.userDetails()
        isEnableDisable(false)
        viewModel.userDetailLiveData.observe(viewLifecycleOwner) { it ->
            binding.sellBtn.text = getString(R.string.delete_acc)
            isEnableDisable(false)
            it.data.let { data ->
                binding.userDetail = data
                data.userImage?.let { image ->
                    Glide.with(requireContext())
                        .load(IMAGE_URL + image)
                        .into(binding.roundedimage);
                }


            }
        }
        viewModel.deleteAccountLiveData.observe(viewLifecycleOwner){
            sequenceOf( Toast.makeText(requireContext(),getString(R.string.acc_delete),Toast.LENGTH_LONG).show())
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigateUp()
                findNavController().navigate(R.id.loginFragment)
            }, UI_ANIMATION_DELAY.toLong())

        }
        binding.editBtn.setOnClickListener {
            isEnableDisable(true)
            binding.sellBtn.text = getString(R.string.save)
        }
        binding.sellBtn.setOnClickListener {
            when (binding.sellBtn.text) {
                getString(R.string.save) ->

                    viewModel.editUser(
                        EditUserProfileReq(
                            null,
                            binding.userNameET.text.toString(),
                            binding.userLastNameET.text.toString(),
                            binding.userPhoneET.text.toString()
                        ),

                    )


                getString(R.string.delete_acc)
                ->
                    viewModel.deleteUser()


            }

        }

    }

    private fun isEnableDisable(isEnable: Boolean) {
        binding.userNameET.isEnabled = isEnable
        binding.userLastNameET.isEnabled = isEnable
        binding.userPhoneET.isEnabled = isEnable
        binding.roundedimage.isEnabled = isEnable
    }


}