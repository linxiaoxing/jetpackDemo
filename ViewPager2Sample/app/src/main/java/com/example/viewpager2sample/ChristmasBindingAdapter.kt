package com.example.viewpager2sample

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter

internal object ChristmasBindingAdapter {

    @JvmStatic
    @BindingAdapter("android:src")
    fun setDrawable(
        imageView: ImageView?,
        drawable: Drawable?
    ) {
        if (imageView == null) return
        drawable?.let {
            imageView.setImageDrawable(drawable)
        }
    }
}