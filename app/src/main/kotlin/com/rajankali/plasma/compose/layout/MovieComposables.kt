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

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideEmphasis
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.annotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.rajankali.core.data.Cast
import com.rajankali.core.data.Movie
import java.util.Locale

@Composable
fun MovieListItem(movie: Movie, isFirstCard: Boolean = false, onClick: () -> Unit) {
    Card(
            elevation = 4.dp, modifier = Modifier
            .padding(
                    start = 16.dp,
                    bottom = 8.dp,
                    end = 16.dp,
                    top = if (isFirstCard) 16.dp else 8.dp
            ), shape = MaterialTheme.shapes.medium
    ) {
        Column(
                modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClick() }
        ) {
            GlideImage(
                    model = movie.thumbnail,
                    modifier = Modifier.fillMaxWidth().aspectRatio(16 / 9F)
            ) {
                centerCrop()
            }
            columnSpacer(16)
            val padding = Modifier.padding(horizontal = 16.dp)
            Text(
                    text = movie.originalTitle,
                    style = MaterialTheme.typography.h6,
                    modifier = padding
            )
            columnSpacer(8)
            ProvideEmphasis(AmbientEmphasisLevels.current.medium) {
                Text(
                        text = movie.overview,
                        style = MaterialTheme.typography.body2,
                        maxLines = 2,
                        modifier = padding
                )
            }
            Spacer(Modifier.preferredHeight(8.dp))
            MovieMetadata(movie = movie, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.preferredHeight(16.dp))
        }
    }
}

@Composable
fun MovieCard(movie: Movie, isFirstCard: Boolean = false, modifier: Modifier = Modifier, onClick: () -> Unit) {
    rowSpacer(value = if(isFirstCard) 16 else 4)
    Column(modifier = modifier.width(120.dp).padding(vertical = 8.dp)
    ) {
        Card(
                elevation = 4.dp,
                modifier = Modifier.clickable { onClick() },
                shape = MaterialTheme.shapes.medium
        ) {
            GlideImage(
                    model = movie.thumbnail,
                    modifier = Modifier.fillMaxWidth().aspectRatio(3 / 4F)
            ) {
                centerCrop()
            }
        }
        columnSpacer(8)
        val padding = Modifier.padding(horizontal = 8.dp)
        Text(
                text = movie.originalTitle,
                style = MaterialTheme.typography.body2,
                maxLines = 2,
                modifier = padding
        )
        columnSpacer(4)
        ProvideEmphasis(AmbientEmphasisLevels.current.medium) {
            Text(
                    text = movie.date,
                    style = MaterialTheme.typography.overline,
                    modifier = padding
            )
        }
        Spacer(Modifier.preferredHeight(8.dp))
    }
    rowSpacer(value = 4)
}

@Composable
fun GridItem(movie: Movie, onClick: () -> Unit) {
    GridItem(thumbnail = movie.thumbnail, title = movie.originalTitle, desc = movie.date, onClick = onClick)
}

@Composable
fun GridItem(cast: Cast, onClick: () -> Unit){
    GridItem(thumbnail = cast.thumbnail, title = cast.name, desc = cast.character, onClick = onClick)
}

@Composable
fun GridItem(thumbnail: String, title: String, desc: String, onClick: () -> Unit){
    Column(modifier = Modifier.fillMaxWidth()
    ) {
        Card(
                elevation = 4.dp,
                modifier = Modifier.clickable { onClick() },
                shape = MaterialTheme.shapes.medium
        ) {
            GlideImage(
                    model = thumbnail,
                    modifier = Modifier.fillMaxWidth().aspectRatio(3 / 4F)
            ) {
                centerCrop()
            }
        }
        columnSpacer(8)
        val padding = Modifier.padding(horizontal = 8.dp)
        Text(
                text = title,
                style = MaterialTheme.typography.body2,
                maxLines = 2,
                modifier = padding
        )
        if(desc.isNotEmpty()) {
            columnSpacer(4)
            ProvideEmphasis(AmbientEmphasisLevels.current.medium) {
                Text(
                        text = desc,
                        style = MaterialTheme.typography.overline,
                        modifier = padding
                )
            }
        }
        Spacer(Modifier.preferredHeight(8.dp))
    }
}


@Composable
fun MovieMetadata(
        movie: Movie,
        modifier: Modifier = Modifier
) {
    val divider = "  •  "
    val text = annotatedString {
        val tagStyle = MaterialTheme.typography.overline.toSpanStyle().copy(
                background = MaterialTheme.colors.primary.copy(alpha = 0.8f)
        )
        withStyle(tagStyle) {
            append("  ${movie.mediaType.toUpperCase(Locale.ENGLISH)}  ")
        }
        append(divider)
        append(movie.originalLanguage.language())
        append(divider)
        append("${movie.voteAverage}")
    }
    ProvideEmphasis(AmbientEmphasisLevels.current.medium) {
        Text(
                text = text,
                style = MaterialTheme.typography.body2,
                modifier = modifier
        )
    }
}

fun String.language(): String {
    val loc = Locale(this)
    return loc.getDisplayLanguage(loc)
}

