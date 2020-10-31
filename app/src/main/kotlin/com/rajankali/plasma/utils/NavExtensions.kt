package com.rajankali.plasma.utils

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.navigateSafely(directions: NavDirections){
    try {
        navigate(directions)
    }catch (e: Exception){
        Log.d("NavigationError", "${e.printStackTrace()}")
    }
}