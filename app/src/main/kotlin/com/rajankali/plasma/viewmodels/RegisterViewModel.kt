/*
 * MIT License
 *
 * Copyright (c) 2020 rajandev17
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

class RegisterViewModel @ViewModelInject constructor(
    private val userRepo: UserRepoContract,
    private val plasmaPrefs: PlasmaPrefs
) : ViewModel() {

    private val _registrationResultLiveData = MutableLiveData<Long>()

    private val _userNameErrorLiveData = MutableLiveData<String?>()
    private val _passwordErrorLiveData = MutableLiveData<String?>()

    val userNameErrorLiveData: LiveData<String?>
    get() = _userNameErrorLiveData

    val passwordErrorLiveData: LiveData<String?>
    get() = _passwordErrorLiveData

    val registrationResultLiveData: LiveData<Long>
        get() = _registrationResultLiveData

    fun register(username: String, password: String) = viewModelScope.launch {
        _userNameErrorLiveData.postValue(null)
        _passwordErrorLiveData.postValue(null)
        if (username.isEmpty()) {
            _userNameErrorLiveData.postValue("Username cannot be empty!")
            return@launch
        }
        if (password.isEmpty()) {
            _passwordErrorLiveData.postValue("Password cannot be empty!")
            return@launch
        }
        if (username.length < 4 || username.length > 12) {
            _userNameErrorLiveData.postValue("Username should be in between 4 to 12 chars")
            return@launch
        }
        if (password.length < 4 || password.length > 12) {
            _passwordErrorLiveData.postValue("Password should be in between 4 to 12 chars")
            return@launch
        }
        val user = User(username, password)
        if (userRepo.userExists(user)) {
            _userNameErrorLiveData.postValue("Username already exists!")
            return@launch
        }
        val id = userRepo.insertUser(user = user)
        if (id > 0L) {
            if (id != -1L) {
                plasmaPrefs.setLoggedInUserId(id)
            }
            _registrationResultLiveData.postValue(id)
        }
    }
}
