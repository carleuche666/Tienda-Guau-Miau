package com.vivitasol.tiendaguaumiau.data.repository

import android.util.Log
import com.vivitasol.tiendaguaumiau.data.remote.RailwayRetrofitClient
import com.vivitasol.tiendaguaumiau.data.remote.dto.CreatePetRequest
import com.vivitasol.tiendaguaumiau.data.remote.dto.PetDto
import com.vivitasol.tiendaguaumiau.data.remote.dto.UpdatePetRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

class PetRepository {

    companion object {
        private const val TAG = "PetRepository"
    }

    suspend fun getPetsByUser(userEmail: String): Result<List<PetDto>> = withContext(Dispatchers.IO) {
        try {
            val response = RailwayRetrofitClient.api.getPetsByUser(userEmail).execute()
            if (response.isSuccessful) {
                val pets = response.body() ?: emptyList()
                Result.success(pets)
            } else {
                handleErrorResponse<List<PetDto>>(response)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error de red al obtener mascotas", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado", e)
            Result.failure(e)
        }
    }

    suspend fun createPet(pet: CreatePetRequest): Result<PetDto> = withContext(Dispatchers.IO) {
        try {
            val response = RailwayRetrofitClient.api.createPet(pet).execute()
            if (response.isSuccessful) {
                val createdPet = response.body()
                if (createdPet != null) {
                    Result.success(createdPet)
                } else {
                    Result.failure(IOException("Respuesta sin contenido"))
                }
            } else {
                handleErrorResponse<PetDto>(response)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error de red al crear mascota", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado al crear mascota", e)
            Result.failure(e)
        }
    }

    suspend fun updatePet(petId: Int, pet: UpdatePetRequest): Result<PetDto> = withContext(Dispatchers.IO) {
        try {
            val response = RailwayRetrofitClient.api.updatePet(petId, pet).execute()
            if (response.isSuccessful) {
                val updatedPet = response.body()
                if (updatedPet != null) {
                    Result.success(updatedPet)
                } else {
                    Result.failure(IOException("Respuesta vacía"))
                }
            } else {
                handleErrorResponse<PetDto>(response)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error de red al actualizar mascota", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado", e)
            Result.failure(e)
        }
    }

    suspend fun deletePet(petId: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = RailwayRetrofitClient.api.deletePet(petId).execute()
            if (response.isSuccessful || response.code() == 204) {
                Result.success(Unit)
            } else {
                handleErrorResponse<Unit>(response)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error de red al eliminar mascota", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado", e)
            Result.failure(e)
        }
    }

    private fun <T> handleErrorResponse(response: Response<*>): Result<T> {
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
