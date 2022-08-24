package com.techxform.tradintro.core.utils

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.techxform.tradintro.R

@BindingAdapter("imageUrl")
fun ImageView.loadImage(url: String?) {
    if (url != null) {
        Glide.with(this)
            .load(Contants.IMAGE_BASE_URL + url)
            .into(this)
    }
}

@BindingAdapter("colorText")
fun TextView.color(value: Float) {
    if (value < 0)
        setTextColor(ContextCompat.getColor(context, R.color.red));
    else setTextColor(Color.GREEN)

}


@BindingAdapter("colorTxnType")
fun TextView.color(value: Int) {
    if (value == 1)
        setTextColor(ContextCompat.getColor(context, R.color.dark_pink));
    else setTextColor(ContextCompat.getColor(context, R.color.light_blue_900))

}

@BindingAdapter("inviteColor")
fun TextView.inviteColor(value: Int) {
    when (value) {
        0 -> setTextColor(ContextCompat.getColor(context, R.color.modest_grey));
        1 -> setTextColor(ContextCompat.getColor(context, R.color.green));
        2 -> setTextColor(ContextCompat.getColor(context, R.color.red));

    }
}

@BindingAdapter(value =["userLevel","currentLevel"], requireAll = true)
fun TextView.setLock(userLevel: Int, currentLevel: Int) {
    if (userLevel >= currentLevel)
        setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(context,R.drawable.ic_open_lock), null)
    else
        setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(context,R.drawable.ic_lock), null)
}


@BindingAdapter("enabledColorText")
fun TextView.color(boolean: Boolean) {
    if (boolean)
        setTextColor(ContextCompat.getColor(context, R.color.black))
    else setTextColor(ContextCompat.getColor(context, R.color.grey))
}



