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

package com.rajankali.plasma.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rajankali.core.data.Movie
import com.rajankali.plasma.data.repo.MovieRepo
import com.rajankali.plasma.enums.PageState
import kotlinx.coroutines.launch

class WatchListViewModel @ViewModelInject constructor(private val movieRepo: MovieRepo) : ViewModel() {

    private val _watchListLiveData = MutableLiveData<List<Movie>>()

    val watchListLiveData: LiveData<List<Movie>>
        get() = _watchListLiveData

    private val _pageState = mutableStateOf(PageState.IDLE)
    val pageState: State<PageState>
        get() = _pageState

    private var loggedInUserId = -1L

    fun fetchWatchList(userId: Long) = viewModelScope.launch {
        _pageState.value = PageState.LOADING
        loggedInUserId = userId
        val result = movieRepo.fetchWatchList(loggedInUserId)
        if (result.isEmpty()) {
            _pageState.value = PageState.EMPTY
        } else {
            _pageState.value = PageState.DATA
        }
        _watchListLiveData.postValue(result)
    }

    fun removeFromWatchList(movieId: Int) = viewModelScope.launch {
        val result = movieRepo.removeMovieFromWatchList(movieId, loggedInUserId)
        if (result > 0) {
            fetchWatchList(loggedInUserId)
        }
    }
}
