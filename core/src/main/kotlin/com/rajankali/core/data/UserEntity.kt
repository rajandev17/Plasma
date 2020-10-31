package com.rajankali.core.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    var name: String,
    var password: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}