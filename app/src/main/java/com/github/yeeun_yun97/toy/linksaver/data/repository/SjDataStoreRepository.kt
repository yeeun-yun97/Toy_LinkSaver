package com.github.yeeun_yun97.toy.linksaver.data.repository

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SjDataStoreRepository
private constructor() {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val PASSWORD_KEY = "password"

    companion object {
        private lateinit var repo: SjDataStoreRepository

        fun getInstance(): SjDataStoreRepository {
            if (!this::repo.isInitialized) {
                repo = SjDataStoreRepository()
            }
            return repo
        }
    }

    fun getPassword(context: Context): Flow<String> {
        val EXAMPLE_COUNTER = stringPreferencesKey(PASSWORD_KEY)
        val exampleCounterFlow: Flow<String> = context.dataStore.data
            .map { preferences ->
                preferences[EXAMPLE_COUNTER] ?: "0"
            }
        return exampleCounterFlow
    }

    fun setPassword(context: Context, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val EXAMPLE_COUNTER = stringPreferencesKey(PASSWORD_KEY)
            context.dataStore.edit { settings ->
                settings[EXAMPLE_COUNTER] = password
            }
        }
    }


}