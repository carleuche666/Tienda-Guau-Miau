package com.vivitasol.tiendaguaumiau.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vivitasol.tiendaguaumiau.data.remote.dto.CreatePetRequest
import com.vivitasol.tiendaguaumiau.data.remote.dto.PetDto
import com.vivitasol.tiendaguaumiau.data.remote.dto.UpdatePetRequest
import com.vivitasol.tiendaguaumiau.data.repository.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PetUiState(
    val pets: List<PetDto> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class PetViewModel(private val repository: PetRepository, private val userEmail: String) : ViewModel() {

    private val _uiState = MutableStateFlow(PetUiState())
    val uiState: StateFlow<PetUiState> = _uiState.asStateFlow()

    init {
        loadPets()
    }

    fun loadPets() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = repository.getPetsByUser(userEmail)
            result.fold(
                onSuccess = { pets ->
                    _uiState.value = _uiState.value.copy(pets = pets, isLoading = false)
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(errorMessage = error.message, isLoading = false)
                }
            )
        }
    }

    fun createPet(name: String, type: String) {
        viewModelScope.launch {
            val result = repository.createPet(CreatePetRequest(name, type, userEmail))
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(successMessage = "Mascota creada exitosamente")
                    loadPets() // Recargar la lista
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(errorMessage = it.message)
                }
            )
        }
    }

    fun updatePet(petId: Int, name: String, type: String) {
        viewModelScope.launch {
            val result = repository.updatePet(petId, UpdatePetRequest(name, type))
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(successMessage = "Mascota actualizada exitosamente")
                    loadPets() // Recargar la lista
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(errorMessage = it.message)
                }
            )
        }
    }

    fun deletePet(petId: Int) {
        viewModelScope.launch {
            val result = repository.deletePet(petId)
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(successMessage = "Mascota eliminada exitosamente")
                    loadPets() // Recargar la lista
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(errorMessage = it.message)
                }
            )
        }
    }

    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

class PetViewModelFactory(private val repository: PetRepository, private val userEmail: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PetViewModel(repository, userEmail) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
