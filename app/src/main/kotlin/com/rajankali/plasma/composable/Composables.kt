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

package com.rajankali.plasma.composable

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.flaviofaria.kenburnsview.KenBurnsView
import com.rajankali.core.extensions.load
import com.rajankali.core.extensions.matchParent
import com.rajankali.plasma.ui.plamsaGradient

@Suppress("unused")
@Composable
fun kenBunsView(url: String, modifier: Modifier) {
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
        // A pre-defined composable that's capable of rendering a circular progress indicator. It
        // honors the Material Design specification.
        CircularProgressIndicator(modifier = Modifier.wrapContentWidth(CenterHorizontally))
    }
}

@Composable
fun ErrorView(message: String = "Oops! Something went wrong, Please refresh after some time"){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Image(asset = vectorResource(id = com.rajankali.plasma.R.drawable.ic_baseline_warning_108),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.error),
        modifier = Modifier.size(108.dp))
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
        Image(asset = vectorResource(id = com.rajankali.plasma.R.drawable.ic_outline_article_24),
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface.copy(alpha = 0.8F)),
                modifier = Modifier.size(108.dp))
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
