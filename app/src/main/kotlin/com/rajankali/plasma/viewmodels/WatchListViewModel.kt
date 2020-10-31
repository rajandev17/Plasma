package com.rajankali.plasma.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rajankali.core.data.Movie
import com.rajankali.plasma.data.repo.MovieRepo
import com.rajankali.plasma.enums.PageState
import kotlinx.coroutines.launch

class WatchListViewModel @ViewModelInject constructor(private val movieRepo: MovieRepo): ViewModel() {

    private val _watchListLiveData = MutableLiveData<List<Movie>>()

    val watchListLiveData: LiveData<List<Movie>>
        get() = _watchListLiveData

    private val _pageStateLiveData = MutableLiveData<PageState>()
    val pageStateLiveData: LiveData<PageState>
        get() = _pageStateLiveData

    private var loggedInUserId = -1L

    fun fetchWatchList(userId: Long)= viewModelScope.launch{
        _pageStateLiveData.postValue(PageState.LOADING)
        loggedInUserId = userId
        val result = movieRepo.fetchWatchList(loggedInUserId)
        if(result.isEmpty()){
            _pageStateLiveData.postValue(PageState.EMPTY)
        }else{
            _pageStateLiveData.postValue(PageState.DATA)
        }
        _watchListLiveData.postValue(result)
    }

    fun removeFromWatchList(movieId: Int) = viewModelScope.launch {
        val result = movieRepo.removeMovieFromWatchList(movieId, loggedInUserId)
        if(result > 0){
            fetchWatchList(loggedInUserId)
        }
    }

}