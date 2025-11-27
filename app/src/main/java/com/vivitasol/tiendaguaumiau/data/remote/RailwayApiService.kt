package com.vivitasol.tiendaguaumiau.data.remote

import com.vivitasol.tiendaguaumiau.data.remote.dto.*
import retrofit2.Call
import retrofit2.http.*

interface RailwayApiService {

    // CORREGIDO: Se añade "/email" para que coincida con el backend
    @GET("api/pets/email/{userEmail}")
    fun getPetsByUser(@Path("userEmail") userEmail: String) : Call<List<PetDto>>
    
    @POST("api/pets")
    fun createPet(@Body pet: CreatePetRequest): Call<PetDto>
    
    @PUT("api/pets/{id}")
    fun updatePet(
        @Path("id") id: Int,
        @Body pet: UpdatePetRequest
    ): Call<PetDto>
    
    @DELETE("api/pets/{id}")
    fun deletePet(@Path("id") id: Int): Call<Void>
    
    // Endpoints de usuario se mantienen por si se implementan en el futuro
    @POST("api/users/register")
    fun registerUser(@Body user: RegisterUserRequest): Call<UserDto>
    
    @POST("api/users/login")
    fun loginUser(@Body credentials: LoginRequest): Call<AuthResponse>
    
    @GET("api/users/{email}")
    fun getUserByEmail(@Path("email") email: String): Call<UserDto>
    
    @PUT("api/users/{email}")
    fun updateUser(
        @Path("email") email: String,
        @Body user: UserDto
    ): Call<UserDto>
}
