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
import com.rajankali.core.data.Cast
import com.rajankali.core.data.Movie
import com.rajankali.core.network.Failure
import com.rajankali.core.network.Success
import com.rajankali.core.storage.PlasmaPrefs
import com.rajankali.plasma.data.repo.MovieRepo
import com.rajankali.plasma.enums.PageState
import kotlinx.coroutines.launch

class MovieDetailViewModel @ViewModelInject constructor(
    private val movieRepo: MovieRepo,
    private val plasmaPrefs: PlasmaPrefs
) : ViewModel() {

    private val _watchListLiveData = MutableLiveData<Boolean>()
    val watchListLiveData: LiveData<Boolean> get() = _watchListLiveData

    private val _pageState = mutableStateOf(PageState.IDLE)
    val pageState: State<PageState> get() = _pageState

    private val _castLiveData = MutableLiveData<List<Cast>>()
    val castLiveData: LiveData<List<Cast>> get() = _castLiveData

    fun isAddedToWatchList(movie: Movie) = viewModelScope.launch {
        _watchListLiveData.postValue(movieRepo.isAddedToWatchList(movie.id, plasmaPrefs.userId()))
    }

    fun addToWatchList(movie: Movie) = viewModelScope.launch {
        val result = movieRepo.addMovieToWatchList(movie, plasmaPrefs.userId())
        if (result > -1) {
            _watchListLiveData.postValue(true)
        }
    }

    fun removeFromWatchList(movie: Movie) = viewModelScope.launch {
        if (movieRepo.removeMovieFromWatchList(movie.id, plasmaPrefs.userId()) > 0)
            _watchListLiveData.postValue(false)
    }

    fun cast(movie: Movie) = viewModelScope.launch {
        _pageState.value = PageState.LOADING
        when (val result = movieRepo.cast(movie = movie)) {
            is Success -> {
                _pageState.value = PageState.DATA
                _castLiveData.postValue(result.data.cast)
            }
            is Failure -> {
                _pageState.value = PageState.ERROR
            }
        }
    }
}
