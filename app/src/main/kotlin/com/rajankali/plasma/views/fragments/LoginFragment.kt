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

package com.rajankali.plasma.views.fragments

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.rajankali.core.extensions.matchParent
import com.rajankali.core.extensions.toast
import com.rajankali.plasma.compose.layout.CardButton
import com.rajankali.plasma.compose.layout.CenteredCaption
import com.rajankali.plasma.compose.layout.GradientText
import com.rajankali.plasma.compose.layout.H6
import com.rajankali.plasma.compose.layout.columnSpacer
import com.rajankali.plasma.utils.navigateSafely
import com.rajankali.plasma.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private val loginViewModel: LoginViewModel by viewModels()

    @Composable
    override fun setContent() {
        Box(Modifier.matchParent()) {
            Column(
                modifier = Modifier.fillMaxWidth().align(Alignment.Center).padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    GradientText(name = "Plasma", 110F)
                    columnSpacer(value = 40)
                    H6(text = "Login to Plasma")
                    columnSpacer(value = 20)
                    CenteredCaption(text = "By Logging in to Plasma, you will be able to access your events.", Modifier.padding(20.dp, 0.dp))
                    columnSpacer(value = 30)
                }
                val userNameErrorState = loginViewModel.userNameErrorLiveData.observeAsState()
                val passwordErrorState = loginViewModel.passwordErrorLiveData.observeAsState()
                val username = remember { mutableStateOf(TextFieldValue("rajan")) }

                OutlinedTextField(value = username.value,
                    onValueChange = { username.value = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    isErrorValue = userNameErrorState.value != null,
                    errorColor = MaterialTheme.colors.error)
                Text(
                    text = userNameErrorState.value ?: "",
                    textAlign = TextAlign.Start,
                    fontSize = TextUnit(12),
                    modifier = Modifier.height(userNameErrorState.value?.let { 20.dp } ?: 0.dp),
                    color = MaterialTheme.colors.error)
                columnSpacer(value = 16)

                val password = remember { mutableStateOf(TextFieldValue("chandana")) }
                OutlinedTextField(value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    isErrorValue = passwordErrorState.value != null,
                    errorColor = MaterialTheme.colors.error
                )
                Text(
                    text = passwordErrorState.value ?: "",
                    textAlign = TextAlign.Start,
                    fontSize = TextUnit(12),
                    modifier = Modifier.height(passwordErrorState.value?.let { 20.dp } ?: 0.dp),
                    color = MaterialTheme.colors.error)
                columnSpacer(value = 30)

                CardButton(text = "Login", onClick = {
                    loginViewModel.login(username = username.value.text, password = password.value.text)
                })
            }
        }
        loginViewModel.loginResultLiveData.observe(viewLifecycleOwner) {
            if (it != -1L) {
                updateUser(userId = it)
                navController.navigateSafely(LoginFragmentDirections.actionLoginFragmentToHomeFragment().setLoggedInUserId(it))
            } else {
                toast("Invalid Credentials!")
            }
        }
    }
}
