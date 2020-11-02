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

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideEmphasis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rajankali.plasma.ui.titleTextStyle

@Composable
fun Body(text: String, modifier: Modifier = Modifier){
    ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.high) {
        Text(text = text, modifier = modifier, style = MaterialTheme.typography.body1)
    }
}

@Composable
fun TextButton(text: String, modifier: Modifier = Modifier){
    Text(text = text, modifier = modifier, style = MaterialTheme.typography.button)
}

@Composable
fun Link(text: String, modifier: Modifier = Modifier){
    Text(text = text, color = MaterialTheme.colors.primaryVariant,
            modifier = modifier,
            style = MaterialTheme.typography.button, textDecoration = TextDecoration.Underline)
}

@Composable
fun Body2(text: String, modifier: Modifier = Modifier){
    Text(text = text, modifier = modifier, style = MaterialTheme.typography.body2)
}

@Composable
fun CenteredBody(text: String, modifier: Modifier = Modifier){
    ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.high) {
        Text(text = text, modifier = modifier, style = MaterialTheme.typography.body1, textAlign = TextAlign.Center)
    }
}

@Composable
fun Caption(text: String, modifier: Modifier = Modifier){
    ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.medium) {
        Text(text = text,modifier = modifier, style = MaterialTheme.typography.caption)
    }
}

@Composable
fun CenteredCaption(text: String, modifier: Modifier = Modifier){
    ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.medium) {
        Text(text = text,modifier = modifier, style = MaterialTheme.typography.caption, textAlign = TextAlign.Center)
    }
}

@Composable
fun Title(textSate: MutableState<String> = mutableStateOf(""), modifier: Modifier = Modifier){
    Text(text = textSate.value, modifier = modifier,
        style = titleTextStyle, color = MaterialTheme.colors.primary)
}

@Composable
fun H6(text: String,  modifier: Modifier = Modifier){
    Text(text = text, style = MaterialTheme.typography.h6, modifier = modifier)
}

@Composable
fun H1(text: String,  modifier: Modifier = Modifier){
    Text(text = text, style = MaterialTheme.typography.h1, modifier = modifier)
}

@Composable
fun H2(text: String,  modifier: Modifier = Modifier){
    Text(text = text, style = MaterialTheme.typography.h2, modifier = modifier)
}

@Composable
fun ErrorText(text: String, modifier: Modifier = Modifier){
    Text(text = text, modifier = modifier.fillMaxWidth().padding(horizontal = 48.dp),
        style = MaterialTheme.typography.body2,
        textAlign = TextAlign.Center, color = MaterialTheme.colors.error.copy(alpha = 0.7F))
}

@Composable
fun EmptyText(text: String, modifier: Modifier = Modifier){
    Text(text = text, modifier = modifier.fillMaxWidth().padding(horizontal = 48.dp),
            style = MaterialTheme.typography.body2,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center)
}