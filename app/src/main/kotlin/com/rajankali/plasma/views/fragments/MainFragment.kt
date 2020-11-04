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

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.offsetPx
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.rajankali.core.R as coreR
import com.rajankali.core.extensions.matchParent
import com.rajankali.plasma.R
import com.rajankali.plasma.composable.Body
import com.rajankali.plasma.composable.CardButton
import com.rajankali.plasma.composable.CenteredCaption
import com.rajankali.plasma.composable.GradientText
import com.rajankali.plasma.composable.OnSurfaceTint
import com.rajankali.plasma.composable.Title
import com.rajankali.plasma.composable.columnSpacer
import com.rajankali.plasma.composable.rowSpacer
import com.rajankali.plasma.utils.navigateSafely
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainFragment : BaseFragment() {
    private val alphaAnimator = ValueAnimator.ofInt(0, 1)
    private val translateAnimator = ValueAnimator.ofFloat(0F, -400F)
    private val alphaState = mutableStateOf(0F)
    private val titleOffsetState = mutableStateOf(0F)
    private var canAnimate = true
    private var isOnBoarding: Boolean = true
    private var titleState = mutableStateOf("P")

    override fun onArgumentsReady(bundle: Bundle) {
        isOnBoarding = MainFragmentArgs.fromBundle(bundle).isOnboarding
        canAnimate = isOnBoarding && canAnimate
    }

    @Composable
    override fun setContent() {
        Box(modifier = Modifier.matchParent()) {
            if (!isOnBoarding) {
                Image(asset = vectorResource(id = R.drawable.ic_baseline_close_24),
                        modifier = Modifier.align(Alignment.TopStart)
                                .size(56.dp)
                                .clickable {
                                    mainActivity.onBackPressed()
                                },
                        colorFilter = OnSurfaceTint(),
                        contentScale = ContentScale.Inside)
            }
            Column(
                modifier = Modifier.matchParent().offsetPx(y = titleOffsetState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GradientText(titleState.value)
            }
            Column(Modifier.fillMaxWidth().align(Alignment.Center).offset(y = 50.dp).drawOpacity(alphaState.value)
                .padding(start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                // Text(text = "Welcome to Plasma", style = MaterialTheme.typography.h5)
                // columnSpacer(value = 20)
                Body(text = "Almost There!")
                columnSpacer(value = 10)
                CenteredCaption(text = "Login/Register to access your watchlist")
                columnSpacer(value = 20)
                LoginActions()
                columnSpacer(value = 20)
                CardButton(text = "Explore Plasma",
                    onClick = {
                        navController.navigateSafely(MainFragmentDirections.actionMainFragmentToHomeFragment())
                    })
                columnSpacer(value = 30)
                CenteredCaption(text = "Plasma is a movie search platform backed by The Movie Database Open API where you can see trending shows, watch list them and explore details about them!")
            }
        }
    }

    @Composable
    fun LoginActions() {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            CardButton(text = "Login", icon = coreR.drawable.ic_baseline_login_24,
                modifier = Modifier.weight(1F),
                onClick = {
                    navController.navigateSafely(MainFragmentDirections.actionMainFragmentToLoginFragment())
                })
            rowSpacer(value = 16)
            CardButton(
                text = "Register",
                icon = coreR.drawable.ic_baseline_how_to_reg_24,
                modifier = Modifier.weight(1F),
                onClick = {
                    val mainFragmentDirections =
                        MainFragmentDirections.actionMainFragmentToRegisterFragment()
                    navController.navigateSafely(mainFragmentDirections)
                })
        }
    }

    override fun initViews() {
        if (!canAnimate) {
            titleOffsetState.value = -400F
            alphaState.value = 1F
            titleState.value = "Plasma"
            return
        }
        ioScope.launch {
            delay(200)
            "lasma".forEach {
                delay(100)
                withContext(Dispatchers.Main) {
                    titleState.value = "${titleState.value}$it"
                }
            }
            withContext(Dispatchers.Main) {
                alphaAnimator.startDelay = 200
                alphaAnimator.setDuration(400).start()
                translateAnimator.setDuration(600).start()
            }
        }
        canAnimate = false
        alphaAnimator.repeatCount = 0
        alphaAnimator.interpolator = DecelerateInterpolator()
        alphaAnimator.addUpdateListener {
            val alpha = it.animatedValue as Int
            alphaState.value = alpha.toFloat()
        }
        translateAnimator.addUpdateListener {
            titleOffsetState.value = it.animatedValue as Float
        }
    }

    override fun initObservers() {
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Box(modifier = Modifier.matchParent()) {
        Column(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
                .padding(16.dp)
                .background(color = MaterialTheme.colors.surface),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to Plasma",
                color = MaterialTheme.colors.onSurface,
                fontFamily = FontFamily.Cursive,
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = TextUnit.Sp(30)
            )
            columnSpacer(value = 16)
            CardButton(text = "Login", icon = coreR.drawable.ic_baseline_login_24, onClick = {})
            columnSpacer(value = 16)
            CardButton(
                text = "Register",
                icon = coreR.drawable.ic_baseline_how_to_reg_24,
                onClick = {})
            columnSpacer(value = 100)
        }
    }
}
