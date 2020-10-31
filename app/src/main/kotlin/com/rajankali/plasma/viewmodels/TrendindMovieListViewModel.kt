package com.rajankali.plasma.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rajankali.core.data.Movie
import com.rajankali.core.network.Failure
import com.rajankali.core.network.Success
import com.rajankali.plasma.data.model.LatestData
import com.rajankali.plasma.data.model.MovieRequest
import com.rajankali.plasma.data.repo.MovieRepo
import com.rajankali.plasma.enums.MediaType
import com.rajankali.plasma.enums.PageState
import com.rajankali.plasma.enums.TimeWindow
import com.rajankali.plasma.utils.toData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class TrendindMovieListViewModel @ViewModelInject constructor(private val movieRepo: MovieRepo): ViewModel() {

    private val _trendingShowsStateFlow = MutableStateFlow(LatestData<Movie>(emptyList()))
    val trendingShowStateFlow: StateFlow<LatestData<Movie>>
        get() = _trendingShowsStateFlow

    private val _pageStateLiveData = MutableLiveData<PageState>()
    val pageStateLiveData: LiveData<PageState>
        get() = _pageStateLiveData

    private var page = 1
    private var totalPages = 1
    private var movieRequest: MovieRequest = MovieRequest(MediaType.ALL, TimeWindow.WEEK)

    fun fetchMovies(movieRequest: MovieRequest) = viewModelScope.launch {
        this@TrendindMovieListViewModel.movieRequest = movieRequest
        if(page == 1){
            _pageStateLiveData.postValue(PageState.LOADING)
        }
        when(val result = movieRepo.fetchTrendingMovies(page = page, movieRequest.mediaType, movieRequest.timeWindow)){
            is Success -> {
                totalPages = result.data.totalPages
                if(page == 1) {
                    _pageStateLiveData.postValue(PageState.DATA)
                }
                page++
                _trendingShowsStateFlow.value = result.data.toData(_trendingShowsStateFlow.value.data)
            }
            is Failure -> {
                _pageStateLiveData.postValue(PageState.ERROR)
            }
        }
    }

    fun nextPage(){
        if(page <= totalPages){
            fetchMovies(movieRequest)
        }
    }
}