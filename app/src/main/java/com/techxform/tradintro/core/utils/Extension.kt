package com.techxform.tradintro.core.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun ImageView.loadImage(url: String?) {
    if (url != null) {
        Glide.with(this)
            .load(Contants.IMAGE_BASE_URL + url)
            .into(this)
    }
}