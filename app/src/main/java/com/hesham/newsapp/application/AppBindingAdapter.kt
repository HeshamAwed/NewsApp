package com.hesham.newsapp.application

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.CachePolicy
import com.hesham.newsapp.R

@BindingAdapter(value = ["imageUrl"], requireAll = false)
fun setImageUrl(imageView: ImageView, url: String?) {

    imageView.load(url) {
        placeholder(R.drawable.loading)
        error(R.drawable.bg_default_image)
        memoryCachePolicy(CachePolicy.ENABLED)
    }
}

@BindingAdapter(value = ["isLoading"])
fun ContentLoadingProgressBar.show(isLoading: Boolean?) {
    if (isLoading == true) show() else hide()
}

@BindingAdapter("visibleGone")
fun bindViewGone(view: View, b: Boolean) {
    if (b) view.visibility = View.VISIBLE
    else view.visibility = View.GONE
}

@BindingAdapter("isFavorite")
fun bindIsFavorite(view: ImageButton, b: Boolean) {
    if (b) view.setImageResource(R.drawable.ic_favorite)
    else view.setImageResource(R.drawable.ic_favorite_border)
}
