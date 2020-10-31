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