package com.rajankali.plasma.views.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import com.rajankali.plasma.R
import com.rajankali.plasma.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    var loggedInUserId: Long = -1L
        set(value) {
            field = value
            loginState.value = value != -1L
        }

    private val isLoggedIn
        get() = loggedInUserId != -1L

    val loginState = mutableStateOf(isLoggedIn)

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityViewModel.loggedInLiveData.observe(this) {
            loggedInUserId = it
        }
        mainActivityViewModel.verifyLogin()
    }

    fun openURL(url: String){
        startActivity(Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        })
    }

}


