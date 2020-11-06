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

package com.rajankali.plasma.compose.layout

import android.widget.Toast
import androidx.compose.animation.transition
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideEmphasis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.WithConstraints
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.HorizontalGradient
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.flaviofaria.kenburnsview.KenBurnsView
import com.rajankali.core.extensions.load
import com.rajankali.core.extensions.matchParent
import com.rajankali.plasma.compose.animaton.ProgressColorTransition
import com.rajankali.plasma.compose.animaton.colorPropKey
import com.rajankali.plasma.ui.plamsaGradient

@Suppress("unused")
@Composable
fun KenBurnsView(url: String, modifier: Modifier) {
    val kenBuns =  rememberKenBurnsView()
    AndroidView({ kenBuns }, modifier = modifier) {
        it.load(url = url)
    }
}

@Composable
fun rememberKenBurnsView(): KenBurnsView {
    val context = ContextAmbient.current
    return remember { KenBurnsView(context) }
}

@Composable
fun PlasmaCardView(modifier: Modifier, content: @Composable () -> Unit) = Card(elevation = 4.dp,
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.surface,
        shape = MaterialTheme.shapes.small) {
    Box(alignment = Alignment.CenterStart) {
        content()
    }
}

@Composable
fun CardButton(text: String, icon: Int = -1, modifier: Modifier = Modifier, height: Int = 50, onClick: () -> Unit) =
        PlasmaCardView(modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = onClick).height(height.dp)) {
            WithConstraints {
                Box(modifier.matchParent().background(HorizontalGradient(plamsaGradient, 0F, constraints.maxWidth.toFloat(), TileMode.Clamp))) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.matchParent().padding(16.dp, 0.dp)) {
                        if (icon > 0) {
                            Image(asset = vectorResource(id = icon), modifier = Modifier.size(24.dp),
                                    colorFilter = ColorFilter.tint(Color.White))
                        }
                        Text(text = text, modifier = Modifier.fillMaxWidth(),
                            color = Color.White, textAlign = TextAlign.Center, style = MaterialTheme.typography.button)
                    }
                }
            }

        }

@Suppress("unused")
@Composable
fun rowSpacer(value: Int) = Spacer(modifier = Modifier.preferredWidth(value.dp))

@Composable
fun columnSpacer(value: Int) = Spacer(modifier = Modifier.preferredHeight(value.dp))

@Composable
fun LoadingView(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        val colorState = transition(
            definition = ProgressColorTransition,
            initState = 0,
            toState = 1
        )
        CircularProgressIndicator(modifier = Modifier.wrapContentWidth(CenterHorizontally), color = colorState[colorPropKey])
    }
}

@Composable
fun ErrorView(message: String = "Oops! Something went wrong,\n Please refresh after some time!"){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Image(asset = vectorResource(id = com.rajankali.plasma.R.drawable.ic_round_warning_24),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.error),
        modifier = Modifier.size(96.dp))
        columnSpacer(value = 12)
        ErrorText(message)
    }
}

@Composable
fun EmptyView(message: String = "Nothing in here Yet!, Please comeback later"){
    Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
    ) {
        Image(asset = vectorResource(id = com.rajankali.plasma.R.drawable.ic_empty_inbox_96),
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface.copy(alpha = 0.8F)),
                modifier = Modifier.size(96.dp))
        columnSpacer(value = 12)
        ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.medium) {
            EmptyText(message)
        }
    }
}

@Composable
fun Toast(message: String, length: Int = Toast.LENGTH_SHORT){
    Toast.makeText(ContextAmbient.current, message, length).show()
}
