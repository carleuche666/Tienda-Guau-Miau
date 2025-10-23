package com.vivitasol.carcasamvvm.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.vivitasol.carcasamvvm.R
import com.vivitasol.carcasamvvm.navigation.Route
import com.vivitasol.carcasamvvm.viewmodels.ProfileViewModel
import com.vivitasol.carcasamvvm.viewmodels.ProfileViewModelFactory
import com.vivitasol.carcasamvvm.viewmodels.USER_PHOTO_TARGET
import com.vivitasol.carcasamvvm.viewmodels.petPhotoTarget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Option2View(navController: NavController, viewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(LocalContext.current))) {
    val uiState by viewModel.uiState.collectAsState()

    // Correctly handle navigation as a side-effect
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
                    if (uiState.isEditing) {
                        Icon(Icons.Default.Save, contentDescription = "Guardar")
                    } else {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
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
                    modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        val painter = rememberAsyncImagePainter(model = profile.photoUri, placeholder = painterResource(id = R.drawable.ic_launcher_foreground))
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

                    // --- User Info Section ---
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

                    // --- Pets Section ---
                    item {
                        Text("Mis Mascotas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
                    }

                    items(profile.pets) { pet ->
                        PetInfoRow(
                            petName = pet.name,
                            petType = pet.type,
                            photoUri = pet.photoUri,
                            isEditing = uiState.isEditing,
                            onNameChange = { newName -> viewModel.onPetNameChange(pet.id, newName) },
                            onPhotoClick = { viewModel.setPhotoTarget(petPhotoTarget(pet.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoCard(fullName: String, email: String, phone: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
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
        Icon(imageVector = icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.bodySmall)
            Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun PetInfoRow(
    petName: String,
    petType: String,
    photoUri: String?,
    isEditing: Boolean,
    onNameChange: (String) -> Unit,
    onPhotoClick: () -> Unit
) {
    val painter = rememberAsyncImagePainter(model = photoUri, placeholder = painterResource(id = R.drawable.ic_launcher_foreground))
    if (isEditing) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Image(
                painter = painter,
                contentDescription = "Foto de $petName",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clickable(onClick = onPhotoClick),
                contentScale = ContentScale.Crop
            )
            OutlinedTextField(value = petName, onValueChange = onNameChange, label = { Text("Nombre Mascota") }, modifier = Modifier.weight(1f))
        }
    } else {
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painter, contentDescription = "Foto de $petName", modifier = Modifier.size(60.dp).clip(CircleShape).background(MaterialTheme.colorScheme.secondaryContainer), contentScale = ContentScale.Crop)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = petName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(text = petType, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}