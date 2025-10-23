package com.vivitasol.carcasamvvm.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.data.UserSessionPrefs
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

data class Pet(val id: Int, val name: String = "", val type: String = "")

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
        val newPet = Pet(id = petIdCounter.getAndIncrement())
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

            val errors = mutableListOf<String>()
            if (_uiState.value.fullName.isBlank() || !_uiState.value.fullName.matches("^[a-zA-Z ]+$".toRegex()) || _uiState.value.fullName.length > 50) {
                errors.add("· Nombre inválido")
            }
            if (!_uiState.value.email.endsWith("@duoc.cl")) {
                errors.add("· Solo se aceptan correos @duoc.cl")
            }
            val pass = _uiState.value.password
            if (pass.length < 8 || !pass.contains("[A-Z]".toRegex()) || !pass.contains("[a-z]".toRegex()) || !pass.contains("[0-9]".toRegex()) || !pass.contains("[@#\$%]".toRegex())) {
                errors.add("· La contraseña no cumple los requisitos")
            }
            if (pass != _uiState.value.confirmPassword) {
                errors.add("· Las contraseñas no coinciden")
            }
            if (_uiState.value.phone.isNotEmpty() && !"^[0-9]{9,12}$".toRegex().matches(_uiState.value.phone)) {
                errors.add("· Teléfono inválido")
            }
            if (_uiState.value.pets.any { it.name.isBlank() || it.type.isBlank() }) {
                errors.add("· Toda mascota debe tener nombre y tipo")
            }

            if (errors.isNotEmpty()) {
                _uiState.update { it.copy(isLoading = false, errorMessage = errors.joinToString("\n")) }
            } else {
                UserSessionPrefs.saveUserCredentials(context, _uiState.value.email, _uiState.value.password)
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