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
import com.rajankali.plasma.compose.layout.LoadingView
import com.rajankali.plasma.compose.layout.ToolBar
import com.rajankali.plasma.enums.WebRequest

class PlasmaWebView : BaseFragment() {

    private lateinit var webRequest: WebRequest

    override fun onArgumentsReady(bundle: Bundle) {
        webRequest = WebRequest[PlasmaWebViewArgs.fromBundle(bundle).webRequestKey]
    }

    private val progressState = mutableStateOf(true)

    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    override fun setContent() {
        val context = ContextAmbient.current
        val webView = remember { WebView(context).apply {
            settings.domStorageEnabled = true
            settings.allowContentAccess = true
            settings.allowFileAccess = true
            settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            settings.javaScriptEnabled = true
        } }.apply {
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    if (newProgress > 90) {
                        progressState.value = false
                    }
                }
            }
            loadUrl(webRequest.url)
        }
        Scaffold(topBar = { ToolBar(title = webRequest.title) { mainActivity.onBackPressed() } }) {
            if (progressState.value) {
                LoadingView()
            } else {
                AndroidView({ webView }, modifier = Modifier.fillMaxSize())
            }
        }
    }
}
