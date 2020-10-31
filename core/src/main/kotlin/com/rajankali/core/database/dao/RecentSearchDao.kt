package com.rajankali.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rajankali.core.data.RecentSearch

@Dao
interface RecentSearchDao {
    @Insert
    suspend fun insertSearch(recentSearch: RecentSearch): Long

    @Query("SELECT term FROM recent_search ORDER BY timeStamp DESC")
    suspend fun recentSearches(): List<String>

    @Query("DELETE from recent_search WHERE term = :query")
    suspend fun removeSearch(query: String): Int
}