package com.vivitasol.tiendaguaumiau.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.vivitasol.tiendaguaumiau.model.Pet
import com.vivitasol.tiendaguaumiau.navigation.Route
import com.vivitasol.tiendaguaumiau.viewmodels.ProfileViewModel
import com.vivitasol.tiendaguaumiau.viewmodels.ProfileViewModelFactory
import com.vivitasol.tiendaguaumiau.viewmodels.USER_PHOTO_TARGET
import com.vivitasol.tiendaguaumiau.viewmodels.petPhotoTarget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(navController: NavController, viewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(LocalContext.current))) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.photoTarget) {
        if (uiState.photoTarget != null) {
            navController.navigate(Route.Option5.route)
        }
    }

    Scaffold(
        floatingActionButton = {
            if (!uiState.isLoading) {
                FloatingActionButton(onClick = {
                    if (uiState.isEditing) viewModel.saveChanges() else viewModel.toggleEditMode()
                }) {
                    if (uiState.isEditing) Icon(Icons.Default.Save, contentDescription = "Guardar")
                    else Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            uiState.userProfile?.let { profile ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { Spacer(Modifier.height(1.dp)) }

                    item {
                        val painter = rememberAsyncImagePainter(model = profile.photoUri)
                        Image(
                            painter = painter,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                .clickable { viewModel.setPhotoTarget(USER_PHOTO_TARGET) },
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    if (uiState.isEditing) {
                        item {
                            OutlinedTextField(value = profile.fullName, onValueChange = viewModel::onFullNameChange, label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(value = profile.phone, onValueChange = viewModel::onPhoneChange, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Email: ${profile.email}", style = MaterialTheme.typography.bodySmall)
                        }
                    } else {
                        item {
                            ProfileInfoCard(profile.fullName, profile.email, profile.phone)
                        }
                    }

                }
            }
        }
    }
}

@Composable
private fun ProfileInfoCard(fullName: String, email: String, phone: String) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ProfileInfoRow(icon = Icons.Default.Person, label = "Nombre", value = fullName)
            Divider()
            ProfileInfoRow(icon = Icons.Default.Email, label = "Email", value = email)
            Divider()
            ProfileInfoRow(icon = Icons.Default.Phone, label = "Teléfono", value = phone.ifEmpty { "No especificado" })
        }
    }
}

@Composable
private fun ProfileInfoRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}