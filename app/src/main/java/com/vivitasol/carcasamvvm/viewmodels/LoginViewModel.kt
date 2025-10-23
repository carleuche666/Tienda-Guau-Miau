package com.vivitasol.carcasamvvm.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.data.UserSessionPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginState(
    val email: String = "",
    val emailError: String? = null,
    val password:  String = "",
    val passwordError: String? = null,
    val loginSuccess: Boolean = false
)

class LoginViewModel(private val context: Context): ViewModel() {
    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun login() {
        _uiState.update { it.copy(emailError = null, passwordError = null) }

        val email = _uiState.value.email
        val password = _uiState.value.password

        var hasError = false
        if (email.isBlank()) {
            _uiState.update { it.copy(emailError = "Email cannot be empty") }
            hasError = true
        }

        if (password.isBlank()) {
            _uiState.update { it.copy(passwordError = "Password cannot be empty") }
            hasError = true
        }

        if (!hasError) {
            // TODO: Add actual login logic (e.g., check credentials)
            viewModelScope.launch {
                UserSessionPrefs.setIsLoggedIn(context, true)
                _uiState.update { it.copy(loginSuccess = true) }
            }
        }
    }
}

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}