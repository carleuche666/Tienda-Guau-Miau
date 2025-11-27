package com.vivitasol.tiendaguaumiau.data.repository

import android.util.Log
import com.vivitasol.tiendaguaumiau.data.remote.RailwayRetrofitClient
import com.vivitasol.tiendaguaumiau.data.remote.dto.AuthResponse
import com.vivitasol.tiendaguaumiau.data.remote.dto.LoginRequest
import com.vivitasol.tiendaguaumiau.data.remote.dto.RegisterUserRequest
import com.vivitasol.tiendaguaumiau.data.remote.dto.UserDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

class UserRepository {

    companion object {
        private const val TAG = "UserRepository"
    }

    suspend fun loginUser(credentials: LoginRequest): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val response = RailwayRetrofitClient.api.loginUser(credentials).execute()
            if (response.isSuccessful) {
                val authResponse = response.body()
                if (authResponse != null) {
                    Result.success(authResponse)
                } else {
                    Result.failure(IOException("Respuesta de autenticación vacía"))
                }
            } else {
                handleErrorResponse(response)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error de red durante el login", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado durante el login", e)
            Result.failure(e)
        }
    }

    suspend fun registerUser(user: RegisterUserRequest): Result<UserDto> = withContext(Dispatchers.IO) {
        try {
            val response = RailwayRetrofitClient.api.registerUser(user).execute()
            if (response.isSuccessful) {
                val registeredUser = response.body()
                if (registeredUser != null) {
                    Result.success(registeredUser)
                } else {
                    Result.failure(IOException("Respuesta de registro vacía"))
                }
            } else {
                handleErrorResponse(response)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error de red durante el registro", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado durante el registro", e)
            Result.failure(e)
        }
    }

    private fun <T> handleErrorResponse(response: Response<T>): Result<T> {
        val errorBody = response.errorBody()?.string() ?: "Sin detalles de error"
        val errorMessage = when (response.code()) {
            400 -> "Petición inválida: $errorBody"
            401 -> "No autorizado"
            403 -> "Prohibido"
            404 -> "Recurso no encontrado"
            500 -> "Error interno del servidor"
            else -> "Error HTTP ${response.code()}: $errorBody"
        }
        Log.e(TAG, errorMessage)
        return Result.failure(IOException(errorMessage))
    }
}