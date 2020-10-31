package com.rajankali.plasma.views.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.rajankali.core.extensions.matchParent
import com.rajankali.core.storage.PlasmaPrefs
import com.rajankali.plasma.composable.Title
import com.rajankali.plasma.utils.navigateSafely
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment: BaseFragment() {

    @Inject
    lateinit var plasmaPrefs: PlasmaPrefs

    @Composable
    override fun setContent(){
        Column(modifier = Modifier.matchParent().background(MaterialTheme.colors.surface),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
            Title(mutableStateOf("P"))
        }
        lifecycleScope.launch {
            delay(1000)
            val loggedInUser = withContext(Dispatchers.IO) { plasmaPrefs.userId() }
            if(loggedInUser != -1L) {
                navController.navigateSafely(
                    SplashFragmentDirections.actionSplashFragmentToHomeFragment()
                        .setLoggedInUserId(loggedInUser)
                )
            }else{
                navController.navigateSafely(SplashFragmentDirections.actionSplashFragmentToMainFragment())
            }
        }
    }
}