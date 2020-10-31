package com.rajankali.plasma.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rajankali.core.storage.PlasmaPrefs
import kotlinx.coroutines.launch


class MainActivityViewModel @ViewModelInject constructor(private val plasmaPrefs: PlasmaPrefs): ViewModel() {

    private val _loggedInLiveData = MutableLiveData<Long>()
    val loggedInLiveData: LiveData<Long>
        get() = _loggedInLiveData

    fun verifyLogin() = viewModelScope.launch {
        _loggedInLiveData.postValue(plasmaPrefs.userId())
    }
}