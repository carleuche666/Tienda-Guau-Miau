package com.vivitasol.tiendaguaumiau.views

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vivitasol.tiendaguaumiau.R
import com.vivitasol.tiendaguaumiau.viewmodels.LoginViewModel
import com.vivitasol.tiendaguaumiau.viewmodels.LoginViewModelFactory

@Composable
fun LoginView(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(LocalContext.current))
) {
    val uiState by loginViewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Efecto para la vibración cuando hay un error
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
            }
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    if (uiState.loginSuccess) {
        LaunchedEffect(Unit) { onLoginSuccess() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.icono_login),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "¡Bienvenido a Guau & Miau!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))
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

        AnimatedVisibility(visible = uiState.isLoading, enter = fadeIn(), exit = fadeOut()) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(12.dp))
                    Text("Iniciando Sesión...", color = Color.White)
                }
            }
        }

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
