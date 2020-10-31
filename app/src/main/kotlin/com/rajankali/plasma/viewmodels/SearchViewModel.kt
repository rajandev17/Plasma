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
import com.rajankali.plasma.data.repo.MovieRepoContract
import com.rajankali.plasma.enums.PageState
import com.rajankali.plasma.utils.toData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class SearchViewModel @ViewModelInject constructor(private val movieRepo: MovieRepoContract): ViewModel() {

    private val _searchShowsStateFlow = MutableStateFlow(LatestData<Movie>(emptyList()))
    val searchShowStateFlow: StateFlow<LatestData<Movie>>
        get() = _searchShowsStateFlow

    private val _pageStateLiveData = MutableLiveData<PageState>()
    val pageStateLiveData: LiveData<PageState>
        get() = _pageStateLiveData

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
        if(searchQuery != query){
            _searchShowsStateFlow.value = LatestData(emptyList())
            page = 1
            totalPages = 1
        }
         searchQuery = query
         if(movieRepo.addSearchTerm(query) != -1L) {
             _recentSearchLiveData.postValue(movieRepo.recentSearches())
         }
        if(page == 1){
            _pageStateLiveData.postValue(PageState.LOADING)
        }
        when(val result = movieRepo.search(page = page,query = query)){
            is Success -> {
                totalPages = result.data.totalPages
                if(page == 1) {
                    _pageStateLiveData.postValue(PageState.DATA)
                }
                page++
                _searchShowsStateFlow.value = result.data.toData(_searchShowsStateFlow.value.data.filter { it.isValidMedia })
            }
            is Failure -> {
                _pageStateLiveData.postValue(PageState.ERROR)
            }
        }
    }

    fun nextPage(){
        if(page <= totalPages){
            search(searchQuery)
        }
    }

    fun idle() {
        _searchShowsStateFlow.value = LatestData(emptyList())
        page = 1
        totalPages = 1
        _pageStateLiveData.postValue(PageState.IDLE)
    }

}