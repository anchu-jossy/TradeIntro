package com.techxform.tradintro.core.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.techxform.tradintro.R

typealias Inflate<T> =(LayoutInflater,ViewGroup?,Boolean)->T
open class BaseFragment<VB: ViewBinding>(private val inflate :Inflate<VB>): Fragment() {

    private var _binding :VB?=null
    protected val binding get()= _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=inflate.invoke(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

fun clearBackstack() {
    val fragmentManager: FragmentManager? = fragmentManager
    // this will clear the back stack and displays no animation on the screen
    // this will clear the back stack and displays no animation on the screen
    fragmentManager?.popBackStack(
        null,
        FragmentManager.POP_BACK_STACK_INCLUSIVE
    )
}
}