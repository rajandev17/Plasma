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

import android.util.Log
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRowForIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.rajankali.core.data.Movie
import com.rajankali.plasma.composable.MovieCard
import com.rajankali.plasma.composable.columnSpacer
import com.rajankali.plasma.composable.handleState
import com.rajankali.plasma.utils.navigateSafely
import com.rajankali.plasma.viewmodels.TrendingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TrendingFragment : HomeBaseFragment() {

    private val trendingViewModel: TrendingViewModel by viewModels()

    @Composable
    override fun setContent(){
        handleState(pageStateLiveData = trendingViewModel.pageStateLiveData) {
            val trendingData = trendingViewModel.trendingLiveData.observeAsState(initial = mapOf())
            ScrollableColumn {
                columnSpacer(value = 8)
                trendingData.value.forEach {
                    MovieSection(entry = it)
                }
                Log.d("Trending", trendingData.value.toString())
           }
        }
    }

    @Composable
    fun MovieSection(entry: Map.Entry<String, List<Movie>>){
        columnSpacer(value = 8)
        Text(text = entry.key, modifier = Modifier.padding(start = 16.dp, bottom = 8.dp), style = MaterialTheme.typography.h6)
        LazyRowForIndexed(items = entry.value) { index, movie ->
            MovieCard(movie = movie, isFirstCard = index == 0) {
                homeNavController.navigateSafely(HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment(movie))
            }
        }
    }
}