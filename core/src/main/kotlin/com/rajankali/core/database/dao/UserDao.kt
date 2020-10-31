package com.rajankali.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rajankali.core.data.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity): Long

    @Query("select * from user where name = :username and password = :password LIMIT 1")
    suspend fun login(username: String, password: String) : UserEntity?

    @Query("select * from user where name = :username LIMIT 1")
    suspend fun isUserExists(username: String): UserEntity?

    @Query("select * from user where id = :id LIMIT 1")
    suspend fun fetchUser(id: Long): UserEntity?
}