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

import com.rajankali.core.data.Movie
import com.rajankali.core.data.RecentSearch
import com.rajankali.core.database.dao.MovieDao
import com.rajankali.core.database.dao.RecentSearchDao
import com.rajankali.core.network.MovieApi
import com.rajankali.core.network.handleApi
import com.rajankali.core.utils.now
import com.rajankali.plasma.enums.MediaType
import com.rajankali.plasma.enums.TimeWindow
import javax.inject.Inject

class MovieRepo @Inject constructor(private val movieApi: MovieApi,
                                    private val movieDao: MovieDao,
                                    private val recentSearchDao: RecentSearchDao) : MovieRepoContract {

    override suspend fun fetchTrendingMovies(page: Int,mediaType: MediaType, timeWindow: TimeWindow) =
            handleApi( { movieApi.trendingMovies(page = page, mediaType = mediaType.type,timeWindow =  timeWindow.window) })

    override suspend fun search(query: String, page: Int) =
            handleApi( { movieApi.search(query, page) })

    override suspend fun addMovieToWatchList(movie: Movie, userId: Long): Long {
        return movieDao.addToWatchList(movie.apply {
            this.userId = userId
        })
    }

    override suspend fun isAddedToWatchList(movieId: Int, userId: Long): Boolean {
        return movieDao.isAddedToWatchList(userId = userId, movieId = movieId) != null
    }

    override suspend fun removeMovieFromWatchList(movieId: Int, userId: Long): Int {
        return movieDao.removeFromWatchList(userId, movieId)
    }

    override suspend fun fetchWatchList(userId: Long): List<Movie> {
        return movieDao.fetchWatchList(userId)
    }

    override suspend fun addSearchTerm(term: String): Long {
        val recentSearches = recentSearches()
        if(recentSearches.contains(term)){
            return -1
        }
        if(recentSearches.size == 10){
            recentSearchDao.removeSearch(recentSearches[9])
        }
        return recentSearchDao.insertSearch(RecentSearch(term, now()))
    }

    override suspend fun recentSearches(): List<String> {
        return recentSearchDao.recentSearches()
    }

    override suspend fun cast(movie: Movie) = handleApi( { movieApi.credits(movie.mediaType, movie.id) } )
}