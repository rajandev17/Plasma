package com.rajankali.core.extensions

import android.animation.ValueAnimator
import android.view.View

fun View.visible() = visibility == View.VISIBLE
fun View.invisible() = visibility == View.INVISIBLE
fun View.gone() = visibility == View.GONE


fun ValueAnimator.intValue(): Int{
    return animatedValue as Int
}