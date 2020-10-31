package com.rajankali.plasma.views.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.viewinterop.AndroidView
import com.rajankali.plasma.composable.LoadingView
import com.rajankali.plasma.composable.ToolBar
import com.rajankali.plasma.enums.WebRequest

class PlasmaWebView: BaseFragment() {

    private lateinit var webRequest: WebRequest

    override fun onArgumentsReady(bundle: Bundle) {
        webRequest = WebRequest[PlasmaWebViewArgs.fromBundle(bundle).webRequestKey]
    }

    private val progressState = mutableStateOf(true)

    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    override fun setContent(){
        val context = ContextAmbient.current
        val webView = remember { WebView(context).apply {
            settings.domStorageEnabled = true
            settings.allowContentAccess = true
            settings.allowFileAccess = true
            settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            settings.javaScriptEnabled = true
        }}.apply {
            webChromeClient = object : WebChromeClient(){
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    if(newProgress > 90) {
                        progressState.value = false
                    }
                }
            }
            loadUrl(webRequest.url)
        }
        Scaffold(topBar = { ToolBar(title = webRequest.title){ mainActivity.onBackPressed() } }) {
            if(progressState.value){
                LoadingView()
            }else{
                AndroidView( { webView }, modifier = Modifier.fillMaxSize())
            }
        }
    }
}