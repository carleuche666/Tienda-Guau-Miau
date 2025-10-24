package com.vivitasol.carcasamvvm.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vivitasol.carcasamvvm.model.Pet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

val Context.dataStore by preferencesDataStore(name = "guau_miau_prefs")

data class UserProfile(
    val fullName: String,
    val email: String,
    val phone: String,
    val pets: List<Pet>,
    val photoUri: String? = null
)

object UserSessionPrefs {
    private val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    private val KEY_SUSCRIPCION = booleanPreferencesKey("suscripcion")
    private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
    private val KEY_USER_PASSWORD = stringPreferencesKey("user_password")
    private val KEY_USER_FULL_NAME = stringPreferencesKey("user_full_name")
    private val KEY_USER_PHONE = stringPreferencesKey("user_phone")
    private val KEY_USER_PETS = stringPreferencesKey("user_pets_json")
    private val KEY_USER_PHOTO_URI = stringPreferencesKey("user_photo_uri")

    // --- Session --- //
    fun getIsLoggedInFlow(context: Context): Flow<Boolean> = context.dataStore.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { it[KEY_IS_LOGGED_IN] ?: false }

    suspend fun setIsLoggedIn(context: Context, value: Boolean) = context.dataStore.edit { it[KEY_IS_LOGGED_IN] = value }

    // --- Profile --- //
    suspend fun saveUserProfile(context: Context, profile: UserProfile, pass: String) {
        val petsJson = JSONArray(profile.pets.map { pet ->
            JSONObject()
                .put("id", pet.id)
                .put("name", pet.name)
                .put("type", pet.type)
                .putOpt("photoUri", pet.photoUri)
        })
        context.dataStore.edit {
            it[KEY_USER_FULL_NAME] = profile.fullName
            it[KEY_USER_EMAIL] = profile.email
            it[KEY_USER_PHONE] = profile.phone
            it[KEY_USER_PASSWORD] = pass
            it[KEY_USER_PETS] = petsJson.toString()
            if (profile.photoUri != null) {
                it[KEY_USER_PHOTO_URI] = profile.photoUri
            } else {
                it.remove(KEY_USER_PHOTO_URI)
            }
        }
    }

    fun getCredentialsFlow(context: Context): Flow<Pair<String, String>> = context.dataStore.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { Pair(it[KEY_USER_EMAIL] ?: "", it[KEY_USER_PASSWORD] ?: "") }

    fun getUserProfileFlow(context: Context): Flow<UserProfile> = context.dataStore.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map {
            val petsJson = it[KEY_USER_PETS]
            val pets = if (petsJson != null) {
                try {
                    val jsonArray = JSONArray(petsJson)
                    List(jsonArray.length()) { i ->
                        val petJson = jsonArray.getJSONObject(i)
                        val uriString = petJson.optString("photoUri")
                        Pet(
                            id = petJson.optInt("id", i), // <-- FIX: Safely read the ID
                            name = petJson.getString("name"),
                            type = petJson.getString("type"),
                            photoUri = if (uriString.isNullOrBlank() || uriString == "null") null else uriString
                        )
                    }
                } catch (e: Exception) {
                    emptyList() // Si falla el parseo, devuelve lista vacía
                }
            } else emptyList()
            
            val userPhotoUriString = it[KEY_USER_PHOTO_URI]
            UserProfile(
                fullName = it[KEY_USER_FULL_NAME] ?: "",
                email = it[KEY_USER_EMAIL] ?: "",
                phone = it[KEY_USER_PHONE] ?: "",
                pets = pets,
                photoUri = if (userPhotoUriString.isNullOrBlank() || userPhotoUriString == "null") null else userPhotoUriString
            )
        }

    // --- Subscription --- //
    fun suscripcionFlow(context: Context): Flow<Boolean> = context.dataStore.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { it[KEY_SUSCRIPCION] ?: false }

    suspend fun setSuscripcion(context: Context, value: Boolean) = context.dataStore.edit { it[KEY_SUSCRIPCION] = value }
}