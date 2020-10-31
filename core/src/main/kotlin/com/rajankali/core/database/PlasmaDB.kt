package com.rajankali.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rajankali.core.data.Movie
import com.rajankali.core.data.RecentSearch
import com.rajankali.core.data.UserEntity
import com.rajankali.core.database.dao.MovieDao
import com.rajankali.core.database.dao.RecentSearchDao
import com.rajankali.core.database.dao.UserDao

@Database(entities = [UserEntity::class, Movie::class, RecentSearch::class], version = 1, exportSchema = false)
abstract class PlasmaDB : RoomDatabase(){
    abstract val userDao: UserDao
    abstract val movieDao: MovieDao
    abstract val recentSearchDao: RecentSearchDao
}