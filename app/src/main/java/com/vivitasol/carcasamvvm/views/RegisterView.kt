package com.vivitasol.carcasamvvm.views

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
import com.vivitasol.carcasamvvm.viewmodels.RegisterViewModel
import com.vivitasol.carcasamvvm.viewmodels.RegisterViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterView(
    onRegisterSuccess: () -> Unit,
    onBackToLoginClick: () -> Unit,
    registerViewModel: RegisterViewModel = viewModel(factory = RegisterViewModelFactory(LocalContext.current))
) {
    val uiState by registerViewModel.uiState.collectAsState()
    val petTypes = listOf("Gato", "Perro", "Ave", "Otro")

    if (uiState.registrationSuccess) {
        LaunchedEffect(Unit) { onRegisterSuccess() }
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
        item { OutlinedTextField(value = uiState.password, onValueChange = registerViewModel::onPasswordChange, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(value = uiState.confirmPassword, onValueChange = registerViewModel::onConfirmPasswordChange, label = { Text("Confirmar Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(value = uiState.phone, onValueChange = registerViewModel::onPhoneChange, label = { Text("Teléfono (opcional)") }, modifier = Modifier.fillMaxWidth()) }

        item { Text("Mascotas", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp)) }
        items(uiState.pets) { pet ->
            var isDropdownExpanded by remember { mutableStateOf(false) }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = pet.name, onValueChange = { registerViewModel.onPetNameChange(pet.id, it) }, label = { Text("Nombre Mascota") }, modifier = Modifier.weight(1f))
                ExposedDropdownMenuBox(expanded = isDropdownExpanded, onExpandedChange = { isDropdownExpanded = it }, modifier = Modifier.weight(1f)) {
                    OutlinedTextField(value = pet.type, onValueChange = {}, readOnly = true, label = { Text("Tipo") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) }, modifier = Modifier.menuAnchor())
                    ExposedDropdownMenu(expanded = isDropdownExpanded, onDismissRequest = { isDropdownExpanded = false }) {
                        petTypes.forEach { type -> DropdownMenuItem(text = { Text(type) }, onClick = { registerViewModel.onPetTypeChange(pet.id, type); isDropdownExpanded = false }) }
                    }
                }
                IconButton(onClick = { registerViewModel.removePet(pet.id) }) { Icon(Icons.Default.Delete, contentDescription = "Eliminar") }
            }
        }

        item { uiState.errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
        item { Button(onClick = { registerViewModel.addPet() }) { Text("Añadir Mascota") } }

        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { Button(onClick = { registerViewModel.register() }, modifier = Modifier.fillMaxWidth()) { Text("Registrarse") } }
        item { TextButton(onClick = onBackToLoginClick) { Text("¿Ya tienes cuenta? Inicia sesión") } }
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}