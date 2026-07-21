package com.example.taskflow.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

private val DARK_MODE = booleanPreferencesKey("dark_mode")

class DataStoreManager(private val context: Context) {

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE] = enabled
        }
    }

    fun getDarkMode(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[DARK_MODE] ?: false
        }
    }
}