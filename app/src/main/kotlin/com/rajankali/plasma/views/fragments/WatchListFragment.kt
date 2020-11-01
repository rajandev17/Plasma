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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.viewModels
import com.rajankali.plasma.composable.*
import com.rajankali.plasma.utils.navigateSafely
import com.rajankali.plasma.viewmodels.WatchListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchListFragment: HomeBaseFragment() {

    private val watchLoginViewModel: WatchListViewModel by viewModels()

    @Composable
    override fun setContent(){
        handleState(pageStateLiveData = watchLoginViewModel.pageStateLiveData,
                emptyMessage = "Nothing in here Yet!\n Start adding Movie/Show to Watchlist so you can access them here Later") {
            WatchList(watchListViewModel = watchLoginViewModel)
        }
    }

    override fun initViews() {
        watchLoginViewModel.fetchWatchList(loggedUserId)
    }

    @Composable
    fun WatchList(watchListViewModel: WatchListViewModel){
        val watchListState = watchListViewModel.watchListLiveData.observeAsState(initial = emptyList())
        LazyGridFor(items = watchListState.value) { movie, _ ->
            GridItem(movie = movie){
                homeNavController.navigateSafely(HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment(movie))
            }
        }
    }
}