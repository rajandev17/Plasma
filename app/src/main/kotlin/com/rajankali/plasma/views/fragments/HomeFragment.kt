package com.rajankali.plasma.views.fragments

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rajankali.plasma.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_home

    override fun initObservers() {
    }

    override fun initViews() {
        val controller = (childFragmentManager.findFragmentById(R.id.homeNavHostFragment) as NavHostFragment).navController
        view<BottomNavigationView>(R.id.homeBottomNav)?.setupWithNavController(controller)
    }
}