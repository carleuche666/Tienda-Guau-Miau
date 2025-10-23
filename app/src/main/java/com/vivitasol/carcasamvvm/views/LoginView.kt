package com.vivitasol.carcasamvvm.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vivitasol.carcasamvvm.viewmodels.LoginViewModel
import com.vivitasol.carcasamvvm.viewmodels.LoginViewModelFactory

@Composable
fun LoginView(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(LocalContext.current))
) {
    val uiState by loginViewModel.uiState.collectAsState()

    if (uiState.loginSuccess) {
        LaunchedEffect(Unit) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { loginViewModel.onEmailChange(it) },
            label = { Text("Email") },
            isError = uiState.emailError != null,
            trailingIcon = {
                if (uiState.emailError != null) {
                    Icon(Icons.Filled.Error, "error", tint = androidx.compose.material3.MaterialTheme.colorScheme.error)
                }
            },
            supportingText = { if (uiState.emailError != null) Text(uiState.emailError!!) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { loginViewModel.onPasswordChange(it) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            isError = uiState.passwordError != null,
            trailingIcon = {
                if (uiState.passwordError != null) {
                    Icon(Icons.Filled.Error, "error", tint = androidx.compose.material3.MaterialTheme.colorScheme.error)
                }
            },
            supportingText = { if (uiState.passwordError != null) Text(uiState.passwordError!!) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { loginViewModel.login() }) {
            Text("Login")
        }
        TextButton(onClick = onRegisterClick) {
            Text("Don't have an account? Register")
        }
    }
}