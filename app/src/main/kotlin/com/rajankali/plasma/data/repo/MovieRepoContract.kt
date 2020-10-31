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