package com.rajankali.plasma.di

import com.rajankali.plasma.data.repo.MovieRepo
import com.rajankali.plasma.data.repo.MovieRepoContract
import com.rajankali.plasma.data.repo.UserRepo
import com.rajankali.plasma.data.repo.UserRepoContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module
abstract class RepoModule {
    @Binds
    abstract fun bindsUserRepo(userRepo: UserRepo): UserRepoContract

    @Binds
    abstract fun bindsMovieRepo(movieRepo: MovieRepo): MovieRepoContract
}