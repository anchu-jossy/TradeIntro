package com.techxform.tradintro.core.utils

import android.graphics.Color
import android.graphics.Color.red
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
        setTextColor(Color.RED)
    else setTextColor(Color.GREEN)

}