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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideEmphasis
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.rajankali.core.utils.greetingBasedOnTime
import com.rajankali.plasma.compose.layout.Body2
import com.rajankali.plasma.compose.layout.CardButton
import com.rajankali.plasma.compose.layout.ColumnLine
import com.rajankali.plasma.compose.layout.Dialog
import com.rajankali.plasma.compose.layout.H6
import com.rajankali.plasma.compose.layout.Link
import com.rajankali.plasma.compose.layout.Toast
import com.rajankali.plasma.compose.layout.columnSpacer
import com.rajankali.plasma.enums.WebRequest
import com.rajankali.plasma.utils.navigateSafely
import com.rajankali.plasma.viewmodels.MoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MoreFragment : HomeBaseFragment() {

    private val moreViewModel: MoreViewModel by viewModels()
    private val dialogState = mutableStateOf(false)

    @Composable
    override fun setContent() {
        Scaffold(topBar = { UserGreeting() }, modifier = Modifier.padding(16.dp)) {
            Column(Modifier.fillMaxWidth()) {
                columnSpacer(value = 16)
                H6(text = "About Plasma")
                ColumnLine()
                ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.medium) {
                    Body2(text = "A Benchmark Android Application using latest Android components, which displays trending movies/tv shows, search movies and TV Shows and also add them watchlist based on logged in credentials , also view entire cast of Movies/TV shows.")
                }
                columnSpacer(value = 8)
                WebButton(webRequest = WebRequest.PLASMA)

                columnSpacer(value = 16)
                H6(text = "About Me")
                ColumnLine()
                listOf(WebRequest.LINKED_IN, WebRequest.STACK_OVERFLOW, WebRequest.GITHUB, WebRequest.MEDIUM).forEach {
                    columnSpacer(value = 8)
                    WebButton(webRequest = it)
                }
            }
        }
        Dialog(state = dialogState, title = "Logout", desc = "Are you sure you want to Logout, it will clear all your existing data including WatchList?", pText = "Confirm") {
            moreViewModel.clearPrefs()
        }
    }

    override fun initViews() {
        moreViewModel.fetchUser(loggedUserId)
    }

    @Composable
    fun UserGreeting() {
        val greetingState = moreViewModel.greetingLiveData.observeAsState()
        Card(Modifier.fillMaxWidth().height(60.dp), elevation = 4.dp, shape = MaterialTheme.shapes.medium) {
            Box(modifier = Modifier.padding(16.dp, 0.dp)) {
                Text(
                    text = "${greetingBasedOnTime()} ${greetingState.value?.capitalize(Locale.ENGLISH) ?: "User"}",
                    modifier = Modifier.align(
                        Alignment.CenterStart
                    ),
                    style = MaterialTheme.typography.h6
                )
                LoginStateButton()
            }
        }
        if (moreViewModel.logoutLiveData.observeAsState().value == true) {
            Toast(message = "Logged Out Successfully!")
        }
    }

    @Composable
    fun BoxScope.LoginStateButton() {
        CardButton(
            text = if (loginState.value) "Logout" else "Login",
            modifier = Modifier.width(80.dp).align(Alignment.CenterEnd), height = 30) {
            if (loginState.value) {
                dialogState.value = true
            } else {
                homeNavController.navigateSafely(HomeFragmentDirections.actionHomeFragmentToMainFragment().setIsOnboarding(false))
            }
            }
    }

    @Composable
    fun WebButton(webRequest: WebRequest) {
        Link(text = webRequest.title, modifier = Modifier.clickable {
            if (webRequest == WebRequest.LINKED_IN) {
                mainActivity.openURL(webRequest.url)
            } else {
                homeNavController.navigateSafely(HomeFragmentDirections.actionHomeFragmentToPlasmaWebView().setWebRequestKey(webRequest.key))
            }
        })
    }

    override fun initObservers() {
        moreViewModel.logoutLiveData.observe(viewLifecycleOwner) {
            if (it == true) {
                updateUser(-1L)
                homeNavController.navigateSafely(HomeFragmentDirections.actionHomeFragmentToMainFragmentClearStack())
            }
        }
    }
}
