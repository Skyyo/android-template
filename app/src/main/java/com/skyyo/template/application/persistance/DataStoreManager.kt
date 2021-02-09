package com.skyyo.template.application.persistance

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun setTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = "Bearer $accessToken"
            preferences[PreferencesKeys.REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun getAccessToken(): String? =
        dataStore.data.first()[PreferencesKeys.ACCESS_TOKEN]

    suspend fun getRefreshToken(): String? =
        dataStore.data.first()[PreferencesKeys.REFRESH_TOKEN]

    suspend fun clearData() {
        dataStore.edit { preferences -> preferences.clear() }
    }

    private object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey("accessToken")
        val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
    }
}
