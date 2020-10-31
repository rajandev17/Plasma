package com.rajankali.plasma.viewmodels

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

class MovieDetailViewModel @ViewModelInject constructor(private val movieRepo: MovieRepo,
                                                        private val plasmaPrefs: PlasmaPrefs) : ViewModel() {

    private val _watchListLiveData = MutableLiveData<Boolean>()
    val watchListLiveData: LiveData<Boolean> get() = _watchListLiveData

    private val _pageStateLiveData = MutableLiveData<PageState>()
    val pageStateLiveData: LiveData<PageState> get() = _pageStateLiveData

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
        _pageStateLiveData.postValue(PageState.LOADING)
        when(val result = movieRepo.cast(movie = movie)){
            is Success ->{
                _pageStateLiveData.postValue(PageState.DATA)
                _castLiveData.postValue(result.data.cast)
            }
            is Failure ->{
                _pageStateLiveData.postValue(PageState.ERROR)
            }
        }
    }
}