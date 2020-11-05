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
import com.rajankali.core.network.Failure
import com.rajankali.core.network.Success
import com.rajankali.plasma.data.model.LatestData
import com.rajankali.plasma.data.repo.MovieRepoContract
import com.rajankali.plasma.enums.PageState
import com.rajankali.plasma.utils.toData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class SearchViewModel @ViewModelInject constructor(private val movieRepo: MovieRepoContract) : ViewModel() {

    private val _searchShowsStateFlow = MutableStateFlow(LatestData<Movie>(emptyList()))
    val searchShowStateFlow: StateFlow<LatestData<Movie>>
        get() = _searchShowsStateFlow

    private val _pageState = mutableStateOf(PageState.IDLE)
    val pageState: State<PageState>
        get() = _pageState

    private val _recentSearchLiveData = MutableLiveData<List<String>>()
    val recentSearchLiveData: LiveData<List<String>>
        get() = _recentSearchLiveData

    private var page = 1
    private var totalPages = 1
    private var searchQuery = ""

    init {
        viewModelScope.launch {
            _recentSearchLiveData.postValue(movieRepo.recentSearches())
        }
    }

    fun search(query: String) = viewModelScope.launch {
        if (searchQuery != query) {
            _searchShowsStateFlow.value = LatestData(emptyList())
            page = 1
            totalPages = 1
        }
        searchQuery = query
        if (movieRepo.addSearchTerm(query) != -1L) {
            _recentSearchLiveData.postValue(movieRepo.recentSearches())
        }
        if (page == 1) {
            _pageState.value = PageState.LOADING
        }
        when (val result = movieRepo.search(page = page, query = query)) {
            is Success -> {
                totalPages = result.data.totalPages
                if (page == 1) {
                    if (result.data.results.isNotEmpty()) {
                        _pageState.value = PageState.DATA
                    } else {
                        _pageState.value = PageState.EMPTY
                    }
                }
                page++
                _searchShowsStateFlow.value = result.data.toData(_searchShowsStateFlow.value.data.filter { it.isValidMedia })
            }
            is Failure -> {
                _pageState.value = PageState.ERROR
            }
        }
    }

    fun nextPage() {
        if (page <= totalPages) {
            search(searchQuery)
        }
    }

    fun idle() {
        _searchShowsStateFlow.value = LatestData(emptyList())
        page = 1
        totalPages = 1
        _pageState.value = PageState.IDLE
    }
}
