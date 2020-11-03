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

package com.rajankali.plasma.data.model

import com.rajankali.plasma.enums.MediaType
import com.rajankali.plasma.enums.TimeWindow
import kotlinx.coroutines.ExperimentalCoroutinesApi

data class MovieRequest @ExperimentalCoroutinesApi constructor(val mediaType: MediaType, val timeWindow: TimeWindow)

@ExperimentalCoroutinesApi
enum class TrendingMovieRequest(val type: Int, val movieRequest: MovieRequest, val title: String) {
    MOVIE_TODAY(1, MovieRequest(MediaType.MOVIE, TimeWindow.DAY), "Trending in Movies"),
    TV_TODAY(2, MovieRequest(MediaType.TV, TimeWindow.DAY), "Trending in TV"),
    MOVIE_THIS_WEEK(3, MovieRequest(MediaType.MOVIE, TimeWindow.WEEK), "Trending in Movies this week"),
    TV_THIS_WEEK(4, MovieRequest(MediaType.TV, TimeWindow.WEEK), "Trending in TV this week"),
    ALL_WEEK(5, MovieRequest(MediaType.ALL, TimeWindow.WEEK), "Trending");

    companion object {
        operator fun get(type: Int): TrendingMovieRequest {
            return when (type) {
                MOVIE_TODAY.type -> MOVIE_TODAY
                TV_TODAY.type -> TV_TODAY
                MOVIE_THIS_WEEK.type -> MOVIE_THIS_WEEK
                TV_THIS_WEEK.type -> TV_THIS_WEEK
                else -> ALL_WEEK
            }
        }
    }
}
