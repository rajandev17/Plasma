package com.rajankali.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rajankali.core.data.Movie

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchList(movie: Movie): Long

    @Query("SELECT * FROM movie WHERE userId = :userId")
    suspend fun fetchWatchList(userId: Long) : List<Movie>

    @Query("DELETE FROM movie WHERE userId =:userId AND id = :movieId")
    suspend fun removeFromWatchList(userId: Long, movieId: Int): Int

    @Query("SELECT * FROM movie WHERE userId = :userId AND id = :movieId LIMIT 1")
    suspend fun isAddedToWatchList(userId: Long, movieId: Int): Movie?
}