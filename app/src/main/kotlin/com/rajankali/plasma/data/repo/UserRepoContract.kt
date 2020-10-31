package com.rajankali.plasma.data.repo

import com.rajankali.core.data.UserEntity
import com.rajankali.plasma.data.model.User

interface UserRepoContract {

    suspend fun insertUser(user: User) : Long

    suspend fun isValidUser(user: User): Long

    suspend fun userExists(user: User): Boolean

    suspend fun fetchUser(id :Long): User?
}