package com.vivitasol.tiendaguaumiau.views

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vivitasol.tiendaguaumiau.data.repository.PetRepository
import com.vivitasol.tiendaguaumiau.viewmodels.PetViewModel
import com.vivitasol.tiendaguaumiau.viewmodels.PetViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetApiTestView(userEmail: String) {
    val petViewModel: PetViewModel = viewModel(factory = PetViewModelFactory(PetRepository(), userEmail))
    val uiState by petViewModel.uiState.collectAsState()

    var showAddPetDialog by remember { mutableStateOf(false) }
    var newPetName by remember { mutableStateOf("") }
    var newPetType by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mascotas") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddPetDialog = true }) {
                Text("  Añadir  ")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.errorMessage != null) {
                Text("Error: ${uiState.errorMessage}", color = MaterialTheme.colorScheme.error)
            } else {
                if (uiState.pets.isEmpty()) {
                    Text("No tienes mascotas registradas.")
                } else {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(uiState.pets) { pet ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(pet.name, fontWeight = FontWeight.Bold)
                                        Text(pet.type)
                                    }
                                    IconButton(onClick = { pet.id?.let { petViewModel.deletePet(it) } }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (showAddPetDialog) {
                AlertDialog(
                    onDismissRequest = { showAddPetDialog = false },
                    title = { Text("Añadir Mascota") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = newPetName,
                                onValueChange = { newPetName = it },
                                label = { Text("Nombre") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = newPetType,
                                onValueChange = { newPetType = it },
                                label = { Text("Tipo") }
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (newPetName.isNotBlank() && newPetType.isNotBlank()) {
                                    petViewModel.createPet(newPetName, newPetType)
                                    showAddPetDialog = false
                                    newPetName = ""
                                    newPetType = ""
                                }
                            }
                        ) {
                            Text("Guardar")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showAddPetDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}