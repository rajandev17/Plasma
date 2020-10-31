package com.rajankali.plasma.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rajankali.core.storage.PlasmaPrefs
import com.rajankali.plasma.data.model.User
import com.rajankali.plasma.data.repo.UserRepoContract
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(private val userRepo: UserRepoContract, private val plasmaPrefs: PlasmaPrefs) : ViewModel() {

    private val _loginResultLiveData = MutableLiveData<Long>()

    private val _userNameErrorLiveData = MutableLiveData<String?>()
    private val _passwordErrorLiveData = MutableLiveData<String?>()

    val userNameErrorLiveData: LiveData<String?>
        get() = _userNameErrorLiveData

    val passwordErrorLiveData: LiveData<String?>
        get() = _passwordErrorLiveData

    val loginResultLiveData: LiveData<Long>
        get() = _loginResultLiveData

    fun login(username: String, password: String) = viewModelScope.launch{
        _userNameErrorLiveData.postValue(null)
        _passwordErrorLiveData.postValue(null)
        if(username.isEmpty()){
            _userNameErrorLiveData.postValue("Username cannot be empty!")
            return@launch
        }
        if(password.isEmpty()){
            _passwordErrorLiveData.postValue("Password cannot be empty!")
            return@launch
        }
        val user = User(username, password)
        val loggedInUserId = userRepo.isValidUser(user = user)
        if(loggedInUserId != -1L) {
            plasmaPrefs.setLoggedInUserId(loggedInUserId)
        }
        _loginResultLiveData.postValue(loggedInUserId)
    }
}