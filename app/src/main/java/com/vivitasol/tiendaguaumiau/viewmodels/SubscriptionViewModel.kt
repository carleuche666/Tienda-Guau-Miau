package com.vivitasol.tiendaguaumiau.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SubscriptionUiState(
    val suscripcion: Boolean = false,
    val alertMessage: String? = null
)

class SubscriptionViewModel : ViewModel() {
    private val _ui = MutableStateFlow(SubscriptionUiState())
    val ui: StateFlow<SubscriptionUiState> = _ui

    fun toggleSubscription(enabled: Boolean) {
        val message = if (enabled) "¡Gracias por unirte!" else "Suscripción cancelada."
        _ui.value = _ui.value.copy(suscripcion = enabled, alertMessage = message)
    }

    fun updateSubscriptionState(enabled: Boolean) {
        _ui.value = _ui.value.copy(suscripcion = enabled)
    }

    fun dismissAlert() {
        _ui.value = _ui.value.copy(alertMessage = null)
    }
}
