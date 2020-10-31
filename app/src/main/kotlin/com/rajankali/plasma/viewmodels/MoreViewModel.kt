package com.rajankali.plasma.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rajankali.core.storage.PlasmaPrefs
import com.rajankali.plasma.data.repo.UserRepoContract
import kotlinx.coroutines.launch

class MoreViewModel @ViewModelInject constructor(private val userRepo: UserRepoContract,
                                                 private val plasmaPrefs: PlasmaPrefs) : ViewModel(){

    private val _greetingLiveData = MutableLiveData<String>()
    private val _logoutLiveData = MutableLiveData<Boolean>()

    val logoutLiveData: LiveData<Boolean>
        get() = _logoutLiveData

    val greetingLiveData: LiveData<String>
        get() = _greetingLiveData

    init {
        _greetingLiveData.postValue("User")
    }

    fun fetchUser(userId: Long) = viewModelScope.launch{
        _greetingLiveData.postValue(userRepo.fetchUser(userId)?.name?:"User")
    }

    fun clearPrefs() = viewModelScope.launch{
        plasmaPrefs.clear()
        _logoutLiveData.postValue(true)
    }
}