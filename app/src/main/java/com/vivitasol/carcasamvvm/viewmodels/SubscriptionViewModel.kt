package com.vivitasol.carcasamvvm.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SubscriptionUiState(
    val suscripcion: Boolean = false // preferencia persistida
)

class SubscriptionViewModel : ViewModel() {
    private val _ui = MutableStateFlow(SubscriptionUiState())
    val ui: StateFlow<SubscriptionUiState> = _ui

    fun setSuscripcion(enabled: Boolean) {
        _ui.value = _ui.value.copy(suscripcion = enabled)
    }
}
