package com.vivitasol.tiendaguaumiau.views


import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vivitasol.tiendaguaumiau.viewmodels.RegisterViewModel
import com.vivitasol.tiendaguaumiau.viewmodels.RegisterViewModelFactory
import com.vivitasol.tiendaguaumiau.views.composables.CommonAlertDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterView(
    onRegisterSuccess: () -> Unit,
    onBackToLoginClick: () -> Unit,
    registerViewModel: RegisterViewModel = viewModel(factory = RegisterViewModelFactory(LocalContext.current))
) {
    val uiState by registerViewModel.uiState.collectAsState()
    val context = LocalContext.current

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

    val petTypes = listOf("Gato", "Perro", "Ave", "Otro")

    if (uiState.registrationSuccess) {
        CommonAlertDialog(
            title = "¡Registro Exitoso!",
            text = "Tu cuenta ha sido creada. Ahora puedes iniciar sesión.",
            onDismiss = {
                registerViewModel.reset()
                onRegisterSuccess()
            }
        )
    }

    if (uiState.errorMessage != null) {
        CommonAlertDialog(
            title = "Error de Registro",
            text = uiState.errorMessage!!,
            onDismiss = { registerViewModel.dismissError() }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { Text("Registro Guau&Miau", style = MaterialTheme.typography.headlineMedium) }

        item { OutlinedTextField(value = uiState.fullName, onValueChange = registerViewModel::onFullNameChange, label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(value = uiState.email, onValueChange = registerViewModel::onEmailChange, label = { Text("Email") }, modifier = Modifier.fillMaxWidth()) }
        item { 
            OutlinedTextField(value = uiState.password, onValueChange = registerViewModel::onPasswordChange, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            Text(
                text = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un caracter especial (@, #, $, %)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        item { OutlinedTextField(value = uiState.confirmPassword, onValueChange = registerViewModel::onConfirmPasswordChange, label = { Text("Confirmar Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(value = uiState.phone, onValueChange = registerViewModel::onPhoneChange, label = { Text("Teléfono (opcional)") }, modifier = Modifier.fillMaxWidth()) }


        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { Button(onClick = { registerViewModel.register() }, modifier = Modifier.fillMaxWidth()) { Text("Registrarse") } }
        item { TextButton(onClick = onBackToLoginClick) { Text("¿Ya tienes cuenta? Inicia sesión") } }
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}
