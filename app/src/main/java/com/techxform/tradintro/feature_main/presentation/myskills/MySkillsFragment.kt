package com.techxform.tradintro.feature_main.presentation.myskills

import android.os.Bundle
import android.view.View
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.MySkillsBinding

class MySkillsFragment :
    BaseFragment<MySkillsBinding>(MySkillsBinding::inflate) {


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mySkillsRV.adapter = MySkillsAdapter(arrayListOf(), rvListener)


    }

    private val rvListener = object : MySkillsAdapter.ClickListener {
        override fun onItemClick(position: Int) {
        }

    }


}