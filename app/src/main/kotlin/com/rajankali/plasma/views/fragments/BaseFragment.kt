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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.rajankali.plasma.ui.PlasmaTheme
import com.rajankali.plasma.views.activities.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class BaseFragment : Fragment() {

    protected val uiScope = CoroutineScope(Dispatchers.Main)
    protected val ioScope = CoroutineScope(Dispatchers.IO)

    @Composable
    open fun setContent() {}

    open val layoutId: Int = -1

    protected val mainActivity by lazy { (requireActivity() as MainActivity) }
    protected val loggedUserId by lazy { mainActivity.loggedInUserId }
    protected val loginState by lazy { mainActivity.loginState }

    protected val navController by lazy { Navigation.findNavController(requireView()) }

    protected fun fullScreen() {
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    protected fun exitFullScreen() {
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            onArgumentsReady(it)
        }
        return if (layoutId == -1) {
            ComposeView(requireContext()).apply {
                setContent {
                    PlasmaTheme {
                        Surface(color = MaterialTheme.colors.surface) {
                            Box {
                                setContent()
                            }
                        }
                    }
                }
            }
        } else {
            inflater.inflate(layoutId, container, false)
        }
    }

    protected fun updateUser(userId: Long) {
        (requireActivity() as MainActivity).loggedInUserId = userId
    }

    protected fun <T : View> view(id: Int): T? {
        return view?.findViewById<T>(id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    open fun initViews() {}
    open fun initObservers() {}
    open fun onArgumentsReady(bundle: Bundle) {}

    override fun onStop() {
        super.onStop()
        uiScope.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        ioScope.cancel()
    }
}
