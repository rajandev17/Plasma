package com.rajankali.core.storage

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.room.Room
import com.rajankali.core.database.PlasmaDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object StorageModule {

    @Provides
    @Singleton
    fun providesPlasmaDB(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, PlasmaDB::class.java, "plasma_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providesUserDao(plasmaDB: PlasmaDB) = plasmaDB.userDao

    @Provides
    @Singleton
    fun providesMovieDao(plasmaDB: PlasmaDB) = plasmaDB.movieDao


    @Provides
    @Singleton
    fun providesRecentSearchDao(plasmaDB: PlasmaDB) = plasmaDB.recentSearchDao

    @Provides
    @Singleton
    fun providesPlasmaPrefs(@ApplicationContext context: Context) = PlasmaPrefs(context.createDataStore(name = "plasma_prefs"))
}