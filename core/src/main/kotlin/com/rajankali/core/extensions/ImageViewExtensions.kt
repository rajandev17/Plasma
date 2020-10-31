package com.rajankali.core.extensions

import android.widget.ImageView
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.rajankali.core.utils.GlideApp

fun ImageView.load(url: String?){
    url?.let {
        if(it.trim().isNotEmpty()){
            GlideApp.with(this).load(it).into(this)
        }
    }
}

fun ImageView.loadCircular(url: String?){
    url?.let {
        if(it.trim().isNotEmpty()){
            GlideApp.with(this).load(it).apply(RequestOptions().transform(CircleCrop())).into(this)
        }
    }
}