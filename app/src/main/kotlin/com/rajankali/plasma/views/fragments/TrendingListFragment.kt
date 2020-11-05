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
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.onActive
import androidx.fragment.app.viewModels
import com.rajankali.plasma.compose.layout.LazyGridFor
import com.rajankali.plasma.compose.layout.MovieCard
import com.rajankali.plasma.compose.layout.ToolBar
import com.rajankali.plasma.compose.layout.handleState
import com.rajankali.plasma.data.model.LatestData
import com.rajankali.plasma.data.model.TrendingMovieRequest
import com.rajankali.plasma.utils.navigateSafely
import com.rajankali.plasma.viewmodels.TrendindMovieListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class TrendingListFragment : BaseFragment() {

    private val trendingMovieListViewModel: TrendindMovieListViewModel by viewModels()

    private var trendingMovieRequest = TrendingMovieRequest.ALL_WEEK

    override fun onArgumentsReady(bundle: Bundle) {
        trendingMovieRequest =
            TrendingMovieRequest[TrendingListFragmentArgs.fromBundle(bundle).requestType]
    }

    override fun initViews() {
        trendingMovieListViewModel.fetchMovies(trendingMovieRequest.movieRequest)
    }

    @Composable
    override fun setContent() {
        Scaffold(topBar = {
            ToolBar(title = trendingMovieRequest.title) {
                mainActivity.onBackPressed()
            }
        }) {
            val movieStateFlow = trendingMovieListViewModel.trendingShowStateFlow.collectAsState(
                initial = LatestData(emptyList())
            )
            val lastIndex = movieStateFlow.value.data.lastIndex
            handleState(pageStateLiveData = trendingMovieListViewModel.pageStateLiveData) {
                LazyGridFor(movieStateFlow.value.data) { movie, index ->
                    onActive {
                        if (index == lastIndex)
                            trendingMovieListViewModel.nextPage()
                    }
                    MovieCard(movie = movie) {
                        navController.navigateSafely(
                            TrendingListFragmentDirections.actionTrendingListFragmentToMovieDetailFragment(
                                movie
                            )
                        )
                    }
                }
            }
        }
    }
}
