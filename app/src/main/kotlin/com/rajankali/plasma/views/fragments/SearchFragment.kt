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

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideEmphasis
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onActive
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.rajankali.plasma.R
import com.rajankali.plasma.compose.layout.Chip
import com.rajankali.plasma.compose.layout.GridItem
import com.rajankali.plasma.compose.layout.IconText
import com.rajankali.plasma.compose.layout.LazyGridFor
import com.rajankali.plasma.compose.layout.columnSpacer
import com.rajankali.plasma.compose.layout.WithPageState
import com.rajankali.plasma.data.model.LatestData
import com.rajankali.plasma.utils.navigateSafely
import com.rajankali.plasma.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalLayout
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : HomeBaseFragment() {

    private val searchViewModel: SearchViewModel by viewModels()

    private var delayJob: CompletableJob = Job()
    private val searchState = mutableStateOf(TextFieldValue(""))

    @Composable
    override fun setContent() {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Card(elevation = 8.dp, shape = MaterialTheme.shapes.small, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                Box(Modifier.fillMaxSize()) {
                    ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.medium) {
                        val hint = if (searchState.value.text.isEmpty()) "Search Movies and TV Shows" else ""
                        Text(text = hint,
                                modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp),
                                textAlign = TextAlign.Start,
                                style = MaterialTheme.typography.body2)
                    }
                    TextField(value = searchState.value,
                            onValueChange = {
                                searchState.value = it
                                delayJob.cancel()
                                if (it.text.isEmpty()) {
                                    searchViewModel.idle()
                                } else {
                                    delayJob = Job()
                                    lifecycleScope.launch(Dispatchers.IO + delayJob) {
                                        val term = it.text
                                        delay(1000)
                                        searchViewModel.search(term)
                                        delayJob.cancel()
                                    }
                                }
                            },
                            textStyle = MaterialTheme.typography.body2,
                            backgroundColor = Color.Transparent,
                            modifier = Modifier.fillMaxSize().background(Color.Transparent)
                    )
                }
            }
            columnSpacer(value = 10)
            WithPageState(pageState = searchViewModel.pageState, IdleView = { IdleSearch {
                searchState.value = TextFieldValue(it, TextRange(it.length))
            } }, emptyMessage = "No Search results found matching the query, Try again with different query") {
                SearchResults(searchViewModel)
            }
        }
    }

    @Composable
    private fun SearchResults(searchViewModel: SearchViewModel) {
        val searchFlowState = searchViewModel.searchShowStateFlow.collectAsState(initial = LatestData(emptyList()))
        LazyGridFor(items = searchFlowState.value.data, hPadding = 0) { movie, index ->
            if (index == searchFlowState.value.data.lastIndex) {
                onActive {
                    searchViewModel.nextPage()
                }
            }
            GridItem(movie) {
                homeNavController.navigateSafely(HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment(movie))
            }
        }
    }

    @Composable
    fun IdleSearch(onClick: (term: String) -> Unit) {
        Column(Modifier.fillMaxWidth()) {
            val recentSearchState = searchViewModel.recentSearchLiveData.observeAsState(initial = emptyList())
            if (recentSearchState.value.isNotEmpty()) {
                columnSpacer(value = 8)
                IconText(text = "Recent Searches", icon = R.drawable.ic_baseline_recent_24)
            }
            columnSpacer(value = 8)
            FlowRow {
                recentSearchState.value.forEach {
                    Chip(text = it, icon = R.drawable.ic_baseline_trending_up_24) {
                        onClick(it)
                        searchViewModel.search(it)
                    }
                }
            }
        }
    }
}
