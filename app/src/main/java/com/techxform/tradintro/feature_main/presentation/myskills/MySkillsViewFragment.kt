package com.techxform.tradintro.feature_main.presentation.myskills

import android.os.Bundle
import android.text.SpannableString
import android.text.style.BulletSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.MySkillsViewBinding


class MySkillsViewFragment :
    BaseFragment<MySkillsViewBinding>(MySkillsViewBinding::inflate) {


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            imgSemiRectangle.clipToOutline = true
            val backgroundDrawable = DrawableCompat.wrap(imgSemiRectangle.background).mutate()
            DrawableCompat.setTint(
                backgroundDrawable, ContextCompat.getColor(requireContext(), R.color.purple)
            )
            val bulletedList = listOf("Trade on a limit price", "Set the validity order", "set alerts for the stock you own or monitor").toBulletedList()
            textViewDetailList1.text = bulletedList

        }


    }
    private fun List<String>.toBulletedList(): CharSequence {
        return SpannableString(this.joinToString("\n")).apply {
            this@toBulletedList.foldIndexed(0) { index, acc, span ->
                val end = acc + span.length + if (index != this@toBulletedList.size - 1) 1 else 0
                this.setSpan(BulletSpan(16), acc, end, 0)
                end
            }
        }
    }

}


