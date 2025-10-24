package com.vivitasol.carcasamvvm.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ContactState(
    val name: String = "",
    val email: String = "", // Mantenemos el email para saber quién contacta
    val subject: String = "",
    val message: String = ""
)

data class ContactErrors(
    val name: String? = null,
    val email: String? = null,
    val subject: String? = null,
    val message: String? = null
)

class ContactViewModel : ViewModel() {
    private val _state = MutableStateFlow(ContactState())
    val state: StateFlow<ContactState> = _state

    private val _errors = MutableStateFlow(ContactErrors())
    val errors: StateFlow<ContactErrors> = _errors

    val subjects = listOf("Consulta sobre un producto", "Problema con mi pedido", "Sugerencias", "Otro")

    fun onNameChange(v: String) { _state.value = _state.value.copy(name = v) }
    fun onEmailChange(v: String) { _state.value = _state.value.copy(email = v) }
    fun onSubjectChange(v: String) { _state.value = _state.value.copy(subject = v) }
    fun onMessageChange(v: String) { _state.value = _state.value.copy(message = v) }

    fun validate(): Boolean {
        val s = _state.value
        
        val nameErr = if (s.name.isBlank()) "Nombre es obligatorio" else null
        val emailErr = when {
            s.email.isBlank() -> "Email es obligatorio"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(s.email).matches() -> "Email inválido"
            else -> null
        }
        val subjectErr = if (s.subject.isBlank()) "Debes seleccionar un asunto" else null
        val messageErr = if (s.message.isBlank()) "El mensaje no puede estar vacío" else null

        _errors.value = ContactErrors(nameErr, emailErr, subjectErr, messageErr)
        
        return listOf(nameErr, emailErr, subjectErr, messageErr).all { it == null }
    }

    fun reset() {
        _state.value = ContactState()
        _errors.value = ContactErrors()
    }
}
