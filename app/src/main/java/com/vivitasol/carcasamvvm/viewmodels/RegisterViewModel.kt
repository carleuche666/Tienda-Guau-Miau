package com.vivitasol.carcasamvvm.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.atomic.AtomicInteger

data class Pet(val id: Int, val name: String = "", val type: String = "")

data class RegisterState(
    val fullName: String = "",
    val fullNameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val phone: String = "",
    val phoneError: String? = null,
    val pets: List<Pet> = emptyList(),
    val petsError: String? = null,
    val registrationSuccess: Boolean = false
)

class RegisterViewModel : ViewModel() {
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
    fun onPetNameChange(petId: Int, name: String) = _uiState.update { state ->
        state.copy(pets = state.pets.map { if (it.id == petId) it.copy(name = name) else it })
    }

    fun onPetTypeChange(petId: Int, type: String) = _uiState.update { state ->
        state.copy(pets = state.pets.map { if (it.id == petId) it.copy(type = type) else it })
    }

    fun register() {
        _uiState.update { it.copy(fullNameError = null, emailError = null, passwordError = null, confirmPasswordError = null, phoneError = null, petsError = null) }
        var hasError = false

        if (_uiState.value.fullName.isBlank() || !_uiState.value.fullName.matches("^[a-zA-Z ]+$".toRegex()) || _uiState.value.fullName.length > 50) {
            _uiState.update { it.copy(fullNameError = "Nombre inválido") }; hasError = true
        }
        if (!_uiState.value.email.endsWith("@duoc.cl")) {
            _uiState.update { it.copy(emailError = "Solo correos @duoc.cl") }; hasError = true
        }
        val pass = _uiState.value.password
        if (pass.length < 8 || !pass.contains("[A-Z]".toRegex()) || !pass.contains("[a-z]".toRegex()) || !pass.contains("[0-9]".toRegex()) || !pass.contains("[@#\\$%]".toRegex())) {
            _uiState.update { it.copy(passwordError = "Contraseña no cumple requisitos") }; hasError = true
        }
        if (pass != _uiState.value.confirmPassword) {
            _uiState.update { it.copy(confirmPasswordError = "Contraseñas no coinciden") }; hasError = true
        }
        if (_uiState.value.phone.isNotEmpty() && !"^[0-9]{9,12}$".toRegex().matches(_uiState.value.phone)) {
            _uiState.update { it.copy(phoneError = "Teléfono inválido") }; hasError = true
        }
        if (_uiState.value.pets.any { it.name.isBlank() || it.type.isBlank() }) {
            _uiState.update { it.copy(petsError = "Toda mascota debe tener nombre y tipo") }; hasError = true
        }

        if (!hasError) {
            _uiState.update { it.copy(registrationSuccess = true) }
        }
    }
}