package com.techxform.tradintro.feature_main.presentation.profile

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.core.utils.Contants.IMAGE_URL
import com.techxform.tradintro.databinding.UpdateProfileFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.EditUserProfileReq
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
                if (data.userImage?.isNotEmpty() == true)
                    Glide.with(requireContext())
                        .load(IMAGE_URL + data.userImage)
                        .into(binding.roundedimage);
                else {
                    Glide.with(requireContext())
                        .load(R.drawable.profile)
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
        binding.cameraIv.setOnClickListener {
            Toast.makeText(requireContext(), "upload image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isEnableDisable(isEnable: Boolean) {
        var color = ContextCompat.getColor(requireContext(),R.color.grey)
        if(isEnable)
            color = ContextCompat.getColor(requireContext(),R.color.black)

        with(binding){
            userNameET.isEnabled = isEnable
            userLastNameET.isEnabled = isEnable
            userPhoneET.isEnabled = isEnable
            roundedimage.isEnabled = isEnable

            userNameET.setTextColor(color)
            userLastNameET.setTextColor(color)
            userPhoneET.setTextColor(color)
        }

    }


}