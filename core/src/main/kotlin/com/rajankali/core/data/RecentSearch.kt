package com.rajankali.core.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_search")
data class RecentSearch(val term: String, val timeStamp: Long, @PrimaryKey(autoGenerate = true) val id: Long = 0L)