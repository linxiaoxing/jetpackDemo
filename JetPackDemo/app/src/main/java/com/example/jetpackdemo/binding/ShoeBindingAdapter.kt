package com.example.jetpackdemo.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.example.jetpackdemo.R
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

// 加载带圆角的头像
@BindingAdapter("imageTransFromUrl")
fun bindImageTransFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .apply(bitmapTransform(RoundedCornersTransformation(20, 0, RoundedCornersTransformation.CornerType.ALL)))
            .into(view)
    }
}

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view:ImageView,imageUrl:String?){
    if(!imageUrl.isNullOrEmpty()){
        Glide.with(view.context)
            .asBitmap()
            .load(imageUrl)
            .placeholder(R.drawable.glide_placeholder)
            .centerCrop()
            .into(view)
    }
}