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