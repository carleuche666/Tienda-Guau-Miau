package com.vivitasol.tiendaguaumiau.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vivitasol.tiendaguaumiau.data.UserProfile
import com.vivitasol.tiendaguaumiau.data.UserSessionPrefs
import com.vivitasol.tiendaguaumiau.model.Pet
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

data class RegisterState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val phone: String = "",
    val pets: List<Pet> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val registrationSuccess: Boolean = false
)

class RegisterViewModel(private val context: Context) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState.asStateFlow()
    private val petIdCounter = AtomicInteger(0)

    fun onFullNameChange(fullName: String) = _uiState.update { it.copy(fullName = fullName) }
    fun onEmailChange(email: String) = _uiState.update { it.copy(email = email) }
    fun onPasswordChange(password: String) = _uiState.update { it.copy(password = password) }
    fun onConfirmPasswordChange(confirmPassword: String) = _uiState.update { it.copy(confirmPassword = confirmPassword) }
    fun onPhoneChange(phone: String) = _uiState.update { it.copy(phone = phone) }

    fun addPet() {
        val newPet = Pet(id = petIdCounter.getAndIncrement(), "", "")
        _uiState.update { it.copy(pets = it.pets + newPet) }
    }

    fun removePet(petId: Int) = _uiState.update { it.copy(pets = it.pets.filterNot { p -> p.id == petId }) }
    fun onPetNameChange(petId: Int, name: String) = _uiState.update { state -> state.copy(pets = state.pets.map { if (it.id == petId) it.copy(name = name) else it }) }
    fun onPetTypeChange(petId: Int, type: String) = _uiState.update { state -> state.copy(pets = state.pets.map { if (it.id == petId) it.copy(type = type) else it }) }

    fun dismissError() = _uiState.update { it.copy(errorMessage = null) }

    fun register() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            delay(1500) // Simular red

            val state = _uiState.value
            val errors = mutableListOf<String>()

            if (state.fullName.isBlank() || !state.fullName.matches("^[a-zA-Z ]+$".toRegex()) || state.fullName.length > 50) {
                errors.add("· Nombre inválido")
            }
            if (!state.email.endsWith("@duoc.cl")) {
                errors.add("· Solo se aceptan correos @duoc.cl")
            }
            if (state.password.length < 8 || !state.password.contains("[A-Z]".toRegex()) || !state.password.contains("[a-z]".toRegex()) || !state.password.contains("[0-9]".toRegex()) || !state.password.contains("[@#\$%]".toRegex())) {
                errors.add("· La contraseña no cumple los requisitos")
            }
            if (state.password != state.confirmPassword) {
                errors.add("· Las contraseñas no coinciden")
            }
            if (state.phone.isNotEmpty() && !"^[0-9]{9,12}$".toRegex().matches(state.phone)) {
                errors.add("· Teléfono inválido")
            }
            if (state.pets.any { it.name.isBlank() || it.type.isBlank() }) {
                errors.add("· Toda mascota debe tener nombre y tipo")
            }

            if (errors.isNotEmpty()) {
                _uiState.update { it.copy(isLoading = false, errorMessage = errors.joinToString("\n")) }
            } else {
                val userProfile = UserProfile(
                    fullName = state.fullName,
                    email = state.email,
                    phone = state.phone,
                    pets = state.pets
                )
                UserSessionPrefs.saveUserProfile(context, userProfile, state.password)
                _uiState.update { it.copy(isLoading = false, registrationSuccess = true) }
            }
        }
    }
}

class RegisterViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}