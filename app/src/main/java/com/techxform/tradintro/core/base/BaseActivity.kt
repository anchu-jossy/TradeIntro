package com.techxform.tradintro.core.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB :ViewBinding>(private  val inflate: (LayoutInflater)->VB) :AppCompatActivity(){
lateinit var binding:VB
private set

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding=inflate(layoutInflater)
        setContentView(binding.root)
    }

}