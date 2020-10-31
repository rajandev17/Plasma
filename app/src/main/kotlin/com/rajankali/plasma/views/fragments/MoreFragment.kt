package com.rajankali.plasma.views.fragments

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.rajankali.core.utils.greetingBasedOnTime
import com.rajankali.plasma.composable.*
import com.rajankali.plasma.enums.WebRequest
import com.rajankali.plasma.utils.navigateSafely
import com.rajankali.plasma.viewmodels.MoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MoreFragment: HomeBaseFragment() {

    private val moreViewModel: MoreViewModel by viewModels()
    private val dialogState = mutableStateOf(false)

    @Composable
    override fun setContent(){
        Scaffold(topBar = { UserGreeting() }, modifier = Modifier.padding(16.dp)) {
            Column(Modifier.fillMaxWidth()) {
                columnSpacer(value = 16)
                H6(text = "About Me")
                LazyGridFor(listOf(WebRequest.LINKED_IN, WebRequest.STACK_OVERFLOW, WebRequest.GITHUB, WebRequest.MEDIUM), rows = 2) { webRequest, _ ->
                    columnSpacer(value = 16)
                    WebButton(webRequest = webRequest)
                }
            }
        }
        Dialog(state = dialogState, title = "Logout", desc = "Are you sure you want to Logout, it will clear all your existing data including WatchList?", pText = "Confirm"){
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
                    text = "${greetingBasedOnTime()} ${greetingState.value?.capitalize(Locale.ENGLISH)?:"User"}",
                    modifier = Modifier.align(
                        Alignment.CenterStart
                    ),
                    style = MaterialTheme.typography.h6
                )
                LoginStateButton()
            }
        }
        if(moreViewModel.logoutLiveData.observeAsState().value == true){
            Toast(message = "Logged Out Successfully!")
        }
    }

    @Composable
    fun BoxScope.LoginStateButton(){
        CardButton(
            text = if(loginState.value) "Logout" else "Login",
            modifier = Modifier.width(80.dp).align(Alignment.CenterEnd), height = 30){
              if(loginState.value) {
                  dialogState.value = true
              }else{
                  homeNavController.navigateSafely(HomeFragmentDirections.actionHomeFragmentToMainFragment().setIsOnboarding(false))
              }
            }
    }

    @Composable
    fun WebButton(webRequest: WebRequest){
        CardButton(webRequest.title){
            if(webRequest == WebRequest.LINKED_IN){
                mainActivity.openURL(webRequest.url)
            }else {
                homeNavController.navigateSafely(HomeFragmentDirections.actionHomeFragmentToPlasmaWebView().setWebRequestKey(webRequest.key))
            }
        }
    }

    override fun initObservers() {
        moreViewModel.logoutLiveData.observe(viewLifecycleOwner){
            if(it == true){
                updateUser(-1L)
                homeNavController.navigateSafely(HomeFragmentDirections.actionHomeFragmentToMainFragmentClearStack())
            }
        }
    }
}