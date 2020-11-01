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

package com.rajankali.plasma.data.repo

import com.rajankali.core.data.CreditResponse
import com.rajankali.core.data.Movie
import com.rajankali.core.data.PagedResponse
import com.rajankali.core.network.ApiResult
import com.rajankali.plasma.enums.MediaType
import com.rajankali.plasma.enums.TimeWindow

interface MovieRepoContract {
    suspend fun fetchTrendingMovies(page: Int, mediaType: MediaType, timeWindow: TimeWindow) : ApiResult<PagedResponse<Movie?>>

    suspend fun addMovieToWatchList(movie: Movie, userId: Long) : Long

    suspend fun removeMovieFromWatchList(movieId: Int, userId: Long): Int

    suspend fun isAddedToWatchList(movieId: Int, userId: Long): Boolean

    suspend fun fetchWatchList(userId: Long): List<Movie>

    suspend fun addSearchTerm(term: String): Long

    suspend fun recentSearches(): List<String>

    suspend fun search(query: String, page: Int) : ApiResult<PagedResponse<Movie?>>

    suspend fun cast(movie: Movie): ApiResult<CreditResponse>
}