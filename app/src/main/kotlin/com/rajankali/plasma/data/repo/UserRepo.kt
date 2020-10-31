package com.rajankali.plasma.data.repo

import com.rajankali.core.database.dao.UserDao
import com.rajankali.plasma.data.mapper.UserMapper
import com.rajankali.plasma.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepo @Inject constructor(private val userDao: UserDao, private val userMapper: UserMapper): UserRepoContract {

    override suspend fun insertUser(user: User) =
        withContext(Dispatchers.IO){
            userDao.insertUser(userMapper.toEntity(user))
        }

    override suspend fun isValidUser(user: User) = withContext(Dispatchers.IO){
         val loggedInUser =userDao.login(user.name, user.password)
        loggedInUser?.id?:-1
    }

    override suspend fun userExists(user: User) = withContext(Dispatchers.IO){
        userDao.isUserExists(user.name) != null
    }

    override suspend fun fetchUser(id: Long) = withContext(Dispatchers.IO){
        userDao.fetchUser(id)?.let {
            userMapper.toDomain(it)
        }
    }
}