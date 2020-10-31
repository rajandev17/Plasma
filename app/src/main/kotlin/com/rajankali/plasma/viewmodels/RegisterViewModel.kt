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

class RegisterViewModel @ViewModelInject constructor(private val userRepo: UserRepoContract,
            private val plasmaPrefs: PlasmaPrefs) : ViewModel() {

    private val _registrationResultLiveData = MutableLiveData<Long>()

    private val _userNameErrorLiveData = MutableLiveData<String?>()
    private val _passwordErrorLiveData = MutableLiveData<String?>()

    val userNameErrorLiveData: LiveData<String?>
    get() = _userNameErrorLiveData

    val passwordErrorLiveData: LiveData<String?>
    get() = _passwordErrorLiveData

    val registrationResultLiveData: LiveData<Long>
        get() = _registrationResultLiveData

    fun register(username: String, password: String) = viewModelScope.launch{
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
        if(username.length < 4 || username.length > 12){
            _userNameErrorLiveData.postValue("Username should be in between 4 to 12 chars")
            return@launch
        }
        if(password.length < 4 || password.length > 12){
            _passwordErrorLiveData.postValue("Password should be in between 4 to 12 chars")
            return@launch
        }
        val user = User(username, password)
        if(userRepo.userExists(user)){
            _userNameErrorLiveData.postValue("Username already exists!")
            return@launch
        }
        val id = userRepo.insertUser(user = user)
        if(id > 0L){
            if(id != -1L) {
                plasmaPrefs.setLoggedInUserId(id)
            }
            _registrationResultLiveData.postValue(id)
        }
    }
}