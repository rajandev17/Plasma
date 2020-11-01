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
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideEmphasis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.WithConstraints
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.VerticalGradient
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.rajankali.core.data.Cast
import com.rajankali.core.data.Movie
import com.rajankali.core.extensions.toast
import com.rajankali.plasma.R
import com.rajankali.plasma.composable.*
import com.rajankali.plasma.viewmodels.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MovieDetailFragment : BaseFragment() {

    private lateinit var movie: Movie

    private val movieDetailViewModel: MovieDetailViewModel by viewModels()
    private val animateState = mutableStateOf(2)

    override fun onArgumentsReady(bundle: Bundle) {
        movie = MovieDetailFragmentArgs.fromBundle(bundle).movie
    }

    override fun onStop() {
        super.onStop()
        exitFullScreen()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
    }

    override fun initViews() {
        movieDetailViewModel.isAddedToWatchList(movie)
        movieDetailViewModel.cast(movie)
        ioScope.launch {
            var plus = true
            while (isActive) {
                delay(32)
                animateState.value = animateState.value + 1 * if (plus) 1 else -1
                plus = !plus
            }
        }
    }

    @Composable
    override fun setContent() {
        Column {
            MovieDetailView(movie)
            H6(text = "Cast", modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
            Divider(modifier = Modifier.preferredHeight((0.8).dp).fillMaxWidth().padding(horizontal = 12.dp), color = MaterialTheme.colors.onSurface.copy(alpha = 0.3F))
            handleState(pageStateLiveData = movieDetailViewModel.pageStateLiveData) {
                CastGrid(movieDetailViewModel.castLiveData)
            }
        }
    }

    @Composable
    private fun CastGrid(castLiveData: LiveData<List<Cast>>) {
        val castListState = castLiveData.observeAsState(emptyList())
        LazyGridFor(items = castListState.value) { cast, _ ->
            GridItem(cast = cast) {

            }
        }
    }

    @Composable
    fun MovieDetailView(movie: Movie) {
        val plamsaSurfaceGradient = backgroundGradient().reversed()
        Box {
            if (animateState.value > 0) {
                kenBunsView(url = movie.posterImage, modifier = Modifier.fillMaxWidth().aspectRatio(4 / 3F))
            }
            WatchListButton()
            WithConstraints {
                Box(modifier = Modifier.fillMaxWidth().aspectRatio(4 / 3F)
                        .background(VerticalGradient(plamsaSurfaceGradient, 0F, constraints.maxHeight.toFloat(), TileMode.Clamp))
                ) {
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                        MovieInfo(movie = movie)
                    }
                }
            }
        }
    }

    @Composable
    fun MovieInfo(movie: Movie) {
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
                    maxLines = 4,
                    modifier = padding
            )
        }
        Spacer(Modifier.preferredHeight(8.dp))
        MovieMetadata(movie = movie, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.preferredHeight(16.dp))
    }

    @Composable
    fun WatchListButton() {
        val watchListState = movieDetailViewModel.watchListLiveData.observeAsState(initial = false)
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Image(asset = vectorResource(
                    id = if (watchListState.value) R.drawable.ic_baseline_bookmark_active_24 else R.drawable.ic_baseline_bookmark_in_active_24),
                    colorFilter = ColorFilter.tint(if (watchListState.value) MaterialTheme.colors.primary else Color.White),
            modifier = Modifier.size(56.dp)
                    .padding(8.dp).background(Color.Black.copy(alpha = 0.6F), shape = CircleShape)
                    .align(Alignment.TopEnd).clickable {
                        if (watchListState.value) {
                            toast("Removed from WatchList")
                            movieDetailViewModel.removeFromWatchList(movie)
                        } else {
                            toast("Added to WatchList")
                            movieDetailViewModel.addToWatchList(movie)
                        }
                    }, contentScale = ContentScale.Inside)

            Image(asset = vectorResource(id = R.drawable.ic_baseline_arrow_back_24),
                    modifier = Modifier.size(56.dp)
                            .padding(8.dp).background(Color.Black.copy(alpha = 0.6F), shape = CircleShape)
                            .align(Alignment.TopStart).clickable {
                                mainActivity.onBackPressed()
                            }, contentScale = ContentScale.Inside)
        }
    }

    @Composable
    fun backgroundGradient(): List<Color> {
        return listOf(
                MaterialTheme.colors.surface,
                MaterialTheme.colors.surface.copy(alpha = 0.9F),
                MaterialTheme.colors.surface.copy(alpha = 0.8F),
                MaterialTheme.colors.surface.copy(alpha = 0.7F),
                Color.Transparent)
    }

}