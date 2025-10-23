package com.vivitasol.carcasamvvm.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore by preferencesDataStore(name = "guau_miau_prefs")

object UserSessionPrefs {
    private val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    private val KEY_SUSCRIPCION = booleanPreferencesKey("suscripcion")
    private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
    private val KEY_USER_PASSWORD = stringPreferencesKey("user_password")

    // --- Session --- //
    fun getIsLoggedInFlow(context: Context): Flow<Boolean> =
        context.dataStore.data
            .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
            .map { it[KEY_IS_LOGGED_IN] ?: false }

    suspend fun setIsLoggedIn(context: Context, value: Boolean) {
        context.dataStore.edit { it[KEY_IS_LOGGED_IN] = value }
    }

    // --- Credentials --- //
    suspend fun saveUserCredentials(context: Context, email: String, pass: String) {
        context.dataStore.edit {
            it[KEY_USER_EMAIL] = email
            it[KEY_USER_PASSWORD] = pass
        }
    }

    fun getCredentialsFlow(context: Context): Flow<Pair<String, String>> {
        return context.dataStore.data
            .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
            .map {
                val email = it[KEY_USER_EMAIL] ?: ""
                val pass = it[KEY_USER_PASSWORD] ?: ""
                Pair(email, pass)
            }
    }

    // --- Subscription --- //
    fun suscripcionFlow(context: Context): Flow<Boolean> =
        context.dataStore.data
            .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
            .map { it[KEY_SUSCRIPCION] ?: false }

    suspend fun setSuscripcion(context: Context, value: Boolean) {
        context.dataStore.edit { it[KEY_SUSCRIPCION] = value }
    }
}