package com.vivitasol.carcasamvvm.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets // <-- Import Added
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vivitasol.carcasamvvm.viewmodels.ProfileViewModel
import com.vivitasol.carcasamvvm.viewmodels.ProfileViewModelFactory

@Composable
fun Option2View(navController: NavController) {
    val viewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(LocalContext.current))
    val userProfile by viewModel.userProfile.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Mi Perfil",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            ProfileInfoCard(userProfile.fullName, userProfile.email, userProfile.phone)
        }

        if (userProfile.pets.isNotEmpty()) {
            item {
                Text(
                    text = "Mis Mascotas",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            items(userProfile.pets) {
                PetInfoRow(petName = it.name, petType = it.type)
            }
        }
    }
}

@Composable
fun ProfileInfoCard(fullName: String, email: String, phone: String) {
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
fun ProfileInfoRow(icon: ImageVector, label: String, value: String) {
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
fun PetInfoRow(petName: String, petType: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Pets, contentDescription = "Mascota", modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = petName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = petType, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}