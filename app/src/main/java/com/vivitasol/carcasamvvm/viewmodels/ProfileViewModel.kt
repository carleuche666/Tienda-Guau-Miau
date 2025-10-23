package com.vivitasol.carcasamvvm.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.data.UserProfile
import com.vivitasol.carcasamvvm.data.UserSessionPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val USER_PHOTO_TARGET = "user_photo"
fun petPhotoTarget(petId: Int) = "pet_photo_$petId"

data class ProfileState(
    val userProfile: UserProfile? = null,
    val isEditing: Boolean = false,
    val isLoading: Boolean = true,
    val photoTarget: String? = null
)

class ProfileViewModel(private val context: Context) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val profile = UserSessionPrefs.getUserProfileFlow(context).first()
            _uiState.update { it.copy(userProfile = profile, isLoading = false) }
        }
    }

    fun setPhotoTarget(target: String?) = _uiState.update { it.copy(photoTarget = target) }

    fun updatePhoto(uri: String) {
        val target = _uiState.value.photoTarget
        if (target == null) return

        _uiState.update { state ->
            val newProfile = if (target == USER_PHOTO_TARGET) {
                state.userProfile?.copy(photoUri = uri)
            } else if (target.startsWith("pet_photo_")) {
                val petId = target.removePrefix("pet_photo_").toInt()
                val updatedPets = state.userProfile?.pets?.map {
                    if (it.id == petId) it.copy(photoUri = uri) else it
                } ?: emptyList()
                state.userProfile?.copy(pets = updatedPets)
            } else {
                state.userProfile
            }
            state.copy(userProfile = newProfile)
        }
        setPhotoTarget(null) // Reset target
    }

    fun toggleEditMode() = _uiState.update { it.copy(isEditing = !it.isEditing) }
    fun onFullNameChange(newName: String) = _uiState.update { state -> state.copy(userProfile = state.userProfile?.copy(fullName = newName)) }
    fun onPhoneChange(newPhone: String) = _uiState.update { state -> state.copy(userProfile = state.userProfile?.copy(phone = newPhone)) }
    fun onPetNameChange(petId: Int, newName: String) {
        _uiState.update { state ->
            val updatedPets = state.userProfile?.pets?.map { if (it.id == petId) it.copy(name = newName) else it } ?: emptyList()
            state.copy(userProfile = state.userProfile?.copy(pets = updatedPets))
        }
    }

    fun saveChanges() {
        viewModelScope.launch {
            _uiState.value.userProfile?.let {
                val password = UserSessionPrefs.getCredentialsFlow(context).first().second
                UserSessionPrefs.saveUserProfile(context, it, password)
            }
            _uiState.update { it.copy(isEditing = false) }
        }
    }
}

class ProfileViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}