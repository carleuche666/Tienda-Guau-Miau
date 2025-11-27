package com.vivitasol.tiendaguaumiau.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PetDto(
    @SerializedName("id")
    val id: Int? = null,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("type")
    val type: String,
    
    @SerializedName("userEmail")
    val userEmail: String
)

data class CreatePetRequest(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("type")
    val type: String,
    
    @SerializedName("userEmail")
    val userEmail: String
)

data class UpdatePetRequest(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("type")
    val type: String
)
