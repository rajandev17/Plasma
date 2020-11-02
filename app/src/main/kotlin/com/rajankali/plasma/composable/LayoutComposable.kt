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

import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.AlertDialog
import androidx.compose.material.AmbientElevationOverlay
import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideEmphasis
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.rajankali.plasma.R
import com.rajankali.plasma.enums.PageState
import java.util.Locale

@Composable
fun Chip(text: String, icon: Int = -1, onClick: () -> Unit) {
    Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp).clickable(onClick = { onClick() }),
            verticalAlignment = Alignment.CenterVertically
    ) {
        ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.medium) {
            if (icon != -1) {
                Image(modifier = Modifier.preferredSize(14.dp, 14.dp), asset = vectorResource(id = icon), colorFilter = OnSurfaceTint())
            }
            Spacer(Modifier.preferredWidth(4.dp))
            Body2(text = text)
        }
    }
}

@Composable
fun OnSurfaceTint(): ColorFilter {
    return ColorFilter.tint(MaterialTheme.colors.onSurface)
}

@Composable
fun IconText(text: String, icon: Int = -1, onClick: () -> Unit = {}) {
    Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp).clickable(onClick = { onClick() }),
            verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != -1) {
            Image(modifier = Modifier.preferredSize(16.dp, 16.dp), asset = vectorResource(id = icon), colorFilter = OnSurfaceTint())
        }
        Spacer(Modifier.preferredWidth(8.dp))
        Text(text = text, style = MaterialTheme.typography.subtitle2)
    }
}

@Composable
fun <T> LazyGridFor(
    items: List<T> = listOf(),
    rows: Int = 3,
    hPadding: Int = 8,
    itemContent: @Composable LazyItemScope.(T, Int) -> Unit
) {
    val chunkedList = items.chunked(rows)
    LazyColumnForIndexed(items = chunkedList, modifier = Modifier.padding(horizontal = hPadding.dp)) { index, it ->
        if (index == 0) {
            columnSpacer(value = 8)
        }

        Row {
            it.forEachIndexed { rowIndex, item ->
                Box(modifier = Modifier.weight(1F).align(Alignment.Top).padding(8.dp), alignment = Alignment.Center) {
                    itemContent(item, index * rowIndex + rowIndex + 1)
                }
            }
            repeat(rows - it.size) {
                Box(modifier = Modifier.weight(1F).padding(8.dp)) {}
            }
        }
    }
}

@Composable
fun Dialog(state: MutableState<Boolean>, title: String, desc: String, pText: String, onClick: () -> Unit) {
    if (state.value) {
        val bgColor = MaterialTheme.colors.surface
        AlertDialog(title = { H6(text = title) }, text = { Body2(text = desc) }, buttons = {
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp, end = 16.dp), horizontalArrangement = Arrangement.End) {
                ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.medium) {
                    TextButton(text = "CANCEL", modifier = Modifier.clickable(onClick = { state.value = false }))
                }
                rowSpacer(value = 16)
                TextButton(text = pText.toUpperCase(Locale.ENGLISH), modifier = Modifier.clickable(onClick = {
                    state.value = false
                    onClick()
                }))
            }
        }, onDismissRequest = { state.value = false }, backgroundColor = AmbientElevationOverlay.current?.apply(bgColor, 25.dp) ?: bgColor)
    }
}

@Composable
fun ColumnLine(modifier: Modifier = Modifier) {
    columnSpacer(value = 8)
    Divider(modifier = modifier.preferredHeight((0.8).dp).fillMaxWidth(), color = MaterialTheme.colors.onSurface.copy(alpha = 0.3F))
    columnSpacer(value = 8)
}

@Composable
fun handleState(
    pageStateLiveData: LiveData<PageState>,
    IdleView: (@Composable () -> Unit) = { },
    errorMessage: String = "Oops! Something went wrong, Please try again after some time",
    emptyMessage: String = "Nothing in here Yet!, Please comeback later",
    content: @Composable () -> Unit
) {
    val pageState = pageStateLiveData.observeAsState(initial = PageState.IDLE)
    when (pageState.value) {
        PageState.LOADING -> {
            LoadingView()
        }
        PageState.ERROR -> {
            ErrorView(errorMessage)
        }
        PageState.DATA -> {
            content()
        }
        PageState.EMPTY -> {
            EmptyView(emptyMessage)
        }
        PageState.IDLE -> {
            IdleView.invoke()
        }
    }
}

@Composable
fun ToolBar(title: String, onBackClick: () -> Unit) {
    val bgColor = MaterialTheme.colors.surface
    TopAppBar(title = { H6(text = title) },
            navigationIcon = {
                Image(asset = vectorResource(id = R.drawable.ic_baseline_arrow_back_24), colorFilter = OnSurfaceTint(), modifier = Modifier.offset(x = 12.dp).clickable { onBackClick() })
            }, backgroundColor = AmbientElevationOverlay.current?.apply(bgColor, 25.dp) ?: bgColor)
}
