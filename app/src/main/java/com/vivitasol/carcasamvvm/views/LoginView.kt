package com.vivitasol.carcasamvvm.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        LaunchedEffect(Unit) { onLoginSuccess() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { loginViewModel.onEmailChange(it) },
                label = { Text("Email") },
                enabled = !uiState.isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { loginViewModel.onPasswordChange(it) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                enabled = !uiState.isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { loginViewModel.login() }, enabled = !uiState.isLoading) {
                Text("Login")
            }
            TextButton(onClick = onRegisterClick, enabled = !uiState.isLoading) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }

        // --- Loading Overlay ---
        AnimatedVisibility(visible = uiState.isLoading, enter = fadeIn(), exit = fadeOut()) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(12.dp))
                    Text("Iniciando Sesión...", color = Color.White)
                }
            }
        }

        // --- Error Dialog ---
        if (uiState.errorMessage != null) {
            AlertDialog(
                onDismissRequest = { loginViewModel.dismissError() },
                title = { Text("Error de Inicio de Sesión") },
                text = { Text(uiState.errorMessage!!) },
                confirmButton = { TextButton(onClick = { loginViewModel.dismissError() }) { Text("Aceptar") } }
            )
        }
    }
}