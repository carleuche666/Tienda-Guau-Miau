package com.vivitasol.carcasamvvm.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vivitasol.carcasamvvm.viewmodels.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterView(
    onRegisterSuccess: () -> Unit,
    registerViewModel: RegisterViewModel = viewModel()
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
        item { Text("Registro Guau&Miau", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium) }

        // --- User fields ---
        item { OutlinedTextField(value = uiState.fullName, onValueChange = registerViewModel::onFullNameChange, label = { Text("Nombre Completo") }, isError = uiState.fullNameError != null, supportingText = { uiState.fullNameError?.let { Text(it) } }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(value = uiState.email, onValueChange = registerViewModel::onEmailChange, label = { Text("Email") }, isError = uiState.emailError != null, supportingText = { uiState.emailError?.let { Text(it) } }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(value = uiState.password, onValueChange = registerViewModel::onPasswordChange, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), isError = uiState.passwordError != null, supportingText = { uiState.passwordError?.let { Text(it) } }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(value = uiState.confirmPassword, onValueChange = registerViewModel::onConfirmPasswordChange, label = { Text("Confirmar Password") }, visualTransformation = PasswordVisualTransformation(), isError = uiState.confirmPasswordError != null, supportingText = { uiState.confirmPasswordError?.let { Text(it) } }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(value = uiState.phone, onValueChange = registerViewModel::onPhoneChange, label = { Text("Teléfono (opcional)") }, isError = uiState.phoneError != null, supportingText = { uiState.phoneError?.let { Text(it) } }, modifier = Modifier.fillMaxWidth()) }

        // --- Pets section ---
        item { Text("Mascotas", style = androidx.compose.material3.MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp)) }
        items(uiState.pets) { pet ->
            var isDropdownExpanded by remember { mutableStateOf(false) }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = pet.name, onValueChange = { registerViewModel.onPetNameChange(pet.id, it) }, label = { Text("Nombre Mascota") }, modifier = Modifier.weight(1f))
                ExposedDropdownMenuBox(expanded = isDropdownExpanded, onExpandedChange = { isDropdownExpanded = it }) {
                    OutlinedTextField(value = pet.type, onValueChange = {}, readOnly = true, label = { Text("Tipo") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) }, modifier = Modifier.menuAnchor().weight(1f))
                    ExposedDropdownMenu(expanded = isDropdownExpanded, onDismissRequest = { isDropdownExpanded = false }) {
                        petTypes.forEach { type -> DropdownMenuItem(text = { Text(type) }, onClick = { registerViewModel.onPetTypeChange(pet.id, type); isDropdownExpanded = false }) }
                    }
                }
                IconButton(onClick = { registerViewModel.removePet(pet.id) }) { Icon(Icons.Default.Delete, contentDescription = "Eliminar") }
            }
        }

        item { uiState.petsError?.let { Text(it, color = androidx.compose.material3.MaterialTheme.colorScheme.error) } }
        item { Button(onClick = { registerViewModel.addPet() }) { Text("Añadir Mascota") } }

        // --- Submit button ---
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { Button(onClick = { registerViewModel.register() }, modifier = Modifier.fillMaxWidth()) { Text("Registrarse") } }
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}