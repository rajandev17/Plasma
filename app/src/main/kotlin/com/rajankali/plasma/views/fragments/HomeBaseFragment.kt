package com.rajankali.plasma.views.fragments

import androidx.navigation.Navigation
import com.rajankali.plasma.R

abstract class HomeBaseFragment : BaseFragment(){
    protected val homeNavController by lazy { Navigation.findNavController(requireActivity(), R.id.plasma_host_fragment) }
}