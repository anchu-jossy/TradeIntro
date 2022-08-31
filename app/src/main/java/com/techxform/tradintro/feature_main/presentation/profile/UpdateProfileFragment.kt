package com.techxform.tradintro.feature_main.presentation.profile

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.core.utils.Contants.IMAGE_URL
import com.techxform.tradintro.databinding.UpdateProfileFragmentBinding
import com.techxform.tradintro.feature_main.data.remote.dto.EditUserProfileReq
import com.techxform.tradintro.feature_main.data.remote.dto.Failure
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


@AndroidEntryPoint
class UpdateProfileFragment :
    BaseFragment<UpdateProfileFragmentBinding>(UpdateProfileFragmentBinding::inflate) {

    companion object {
        fun newInstance() = UpdateProfileFragment()
        private const val UI_ANIMATION_DELAY = 2000
        const val REQUEST_CODE = 24


    }

    private lateinit var viewModel: UpdateProfileViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[UpdateProfileViewModel::class.java]
        viewModel.userDetails()
        isEnableDisable(false)
        viewModel.userDetailLiveData.observe(viewLifecycleOwner) { it ->
            binding.deleteButton.text = getString(R.string.delete_acc)
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
        viewModel.portfolioErrorLiveData.observe(viewLifecycleOwner) {
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
                    val errorMsg = (it as Failure.FeatureFailure).message
                    requireContext().showShortToast("Error: $errorMsg")

                }
            }
        }
        viewModel.deleteAccountLiveData.observe(viewLifecycleOwner) {
            sequenceOf(
                requireContext().showShortToast(getString(R.string.acc_delete))
            )
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigateUp()
                findNavController().navigate(R.id.loginFragment)
            }, UI_ANIMATION_DELAY.toLong())

        }
        binding.editBtn.setOnClickListener {
            isEnableDisable(true)
            binding.deleteButton.text = getString(R.string.save)
        }
        binding.deleteButton.setOnClickListener {
            when (binding.deleteButton.text) {
                getString(R.string.save) -> {
                    binding.roundedimage.isDrawingCacheEnabled = true;
                    val bitmap = (binding.roundedimage.drawable as BitmapDrawable).bitmap


                    viewModel.editUser(
                        EditUserProfileReq(
                            persistImage(bitmap, "profileimage"),
                            binding.userNameET.text.toString(),
                            binding.userLastNameET.text.toString(),
                            binding.userPhoneET.text.toString()
                        ),

                        )

                }
                getString(R.string.delete_acc)
                ->
                    viewModel.deleteUser()


            }

        }
        binding.cameraIv.setOnClickListener {

            val checkSelfPermission = ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {

                //Requests permissions to be granted to this application at runtime
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
                )
            } else {
                val galleryIntent = Intent(Intent.ACTION_PICK)
                galleryIntent.type = "image/*"


                val cameraIntent = Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE
                )


                val chooser = Intent.createChooser(galleryIntent, "select gallery or camera")
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
                startActivityForResult(chooser, REQUEST_CODE)
            }
        }


    }

    private fun persistImage(bitmap: Bitmap, name: String): File {
        val filesDir: File = requireContext().filesDir
        val imageFile = File(filesDir, "$name.jpg")
        val os: OutputStream
        try {
            os = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Error writing bitmap", e)
        }
        return imageFile

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Companion.REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            val image = data?.extras?.get("data") ?: data?.data
      Glide.with(requireContext())
                .load(image)
                .into(binding.roundedimage);


        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    private fun isEnableDisable(isEnable: Boolean) {
        var color = ContextCompat.getColor(requireContext(), R.color.grey)
        if (isEnable)
            color = ContextCompat.getColor(requireContext(), R.color.black)

        with(binding) {

            cameraIv.isVisible = isEnable

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