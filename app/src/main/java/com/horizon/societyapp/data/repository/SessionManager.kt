package com.horizon.societyapp.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "session_prefs")

/**
 * Persists the currently logged-in user's id/email across app restarts,
 * so residents don't have to log in every time they open the app.
 */
class SessionManager(private val context: Context) {

    companion object {
        private val KEY_USER_ID = longPreferencesKey("user_id")
        private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
    }

    val loggedInUserId: Flow<Long?> = context.dataStore.data.map { it[KEY_USER_ID] }
    val loggedInUserEmail: Flow<String?> = context.dataStore.data.map { it[KEY_USER_EMAIL] }

    suspend fun saveSession(userId: Long, email: String) {
        context.dataStore.edit {
            it[KEY_USER_ID] = userId
            it[KEY_USER_EMAIL] = email
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}
