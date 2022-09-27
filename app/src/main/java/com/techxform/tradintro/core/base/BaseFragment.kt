package com.techxform.tradintro.core.base

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.techxform.tradintro.R
import com.techxform.tradintro.feature_main.data.remote.dto.AlertPriceRequest
import com.techxform.tradintro.feature_main.data.remote.dto.Notifications
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

open class BaseFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getFragment(): Fragment? {
        return parentFragmentManager.fragments[0]
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

    fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.root?.windowToken, 0)
    }


    fun alertPriceSetDialog(
        notifications: Notifications?,
        posCall: (amount: Double) -> Unit,
        negCall: () -> Unit
    ) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.alert_price_lbl)

        val amountEt = EditText(requireContext())
        amountEt.hint = getString(R.string.alert_price_lbl)

        val container = LinearLayout(requireContext())
        container.orientation = LinearLayout.VERTICAL
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(20, 0, 20, 0)

        amountEt.layoutParams = lp
        amountEt.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
        amountEt.setLines(1)
        amountEt.maxLines = 1
        amountEt.setText((notifications?.notificationPrice ?: 0.0f).toString())
        container.addView(amountEt, lp)
        builder.setView(container)
        var posBtn = getString(R.string.set_alert_lbl)
        var negBtn = getString(R.string.dismiss_lbl)

        if (notifications != null) {
            posBtn = getString(R.string.modify_alert_lbl)
            negBtn = getString(R.string.remove_alert_lbl)
        }
        builder.setPositiveButton(
            posBtn
        ) { dialog, p1 ->
            if (amountEt.text.toString().isNullOrEmpty())
                requireContext().showShortToast(getString(R.string.enter_alert_price_lbl))
            else {
                posCall.invoke(amountEt.text.toString().toDouble())
                dialog.dismiss()
            }
        }

        builder.setNegativeButton(
            negBtn
        ) { dialog, p1 ->
            negCall.invoke()
            dialog.dismiss()
        }
        builder.show()

    }


}
