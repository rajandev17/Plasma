package com.rajankali.core.storage

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.clear
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Singleton
class PlasmaPrefs constructor(private val plasmaStore: DataStore<Preferences>) {

    companion object{
        private val LOGGED_IN_USER_ID = preferencesKey<Long>("logged_in_user_id")
    }

    suspend fun setLoggedInUserId(id: Long){
        plasmaStore.edit {
            it[LOGGED_IN_USER_ID] = id
        }
    }

    suspend fun clear() {
        plasmaStore.edit {
            it.clear()
        }
    }

    suspend fun userId():Long = plasmaStore.data.first()[LOGGED_IN_USER_ID]?:-1

    fun loggedInUserIdLiveData() : LiveData<Long?> = plasmaStore.data.map { it[LOGGED_IN_USER_ID] }.asLiveData()
}