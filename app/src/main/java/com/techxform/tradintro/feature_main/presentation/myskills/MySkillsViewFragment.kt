package com.techxform.tradintro.feature_main.presentation.myskills

import android.os.Bundle
import android.text.SpannableString
import android.text.style.BulletSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import com.techxform.tradintro.R
import com.techxform.tradintro.core.base.BaseFragment
import com.techxform.tradintro.databinding.MySkillsViewBinding
import com.techxform.tradintro.feature_main.data.remote.dto.Levels
import com.techxform.tradintro.feature_main.domain.util.Utils.setVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MySkillsViewFragment :
    BaseFragment<MySkillsViewBinding>(MySkillsViewBinding::inflate) {


    companion object {
        private const val LEVEL: String = "level"
        fun navBundle(levels: Levels) = bundleOf(LEVEL to levels)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            level = requireArguments().getParcelable(LEVEL)
            imgSemiRectangle.clipToOutline = true
            val backgroundDrawable = DrawableCompat.wrap(imgSemiRectangle.background).mutate()
            DrawableCompat.setTint(
                backgroundDrawable, ContextCompat.getColor(requireContext(), R.color.purple)
            )
            //if(level.levelPosition!! <= level.userLevel!!)

            level?.levelPosition?.let { lp ->
                level?.userLevel?.let { ul ->
                    if (lp <= ul) {
                        val bulletedList = listOf(
                            "Trade on a limit price",
                            "Set the validity order",
                            "set alerts for the stock you own or monitor"
                        ).toBulletedList()
                        textViewDetailList1.text = bulletedList
                        textViewDetailList1.setVisible()
                    }
                }

            }



            closeIv.setOnClickListener {
                requireActivity().onBackPressed()
            }

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


