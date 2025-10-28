package com.vivitasol.tiendaguaumiau.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vivitasol.tiendaguaumiau.viewmodels.ContactViewModel
import com.vivitasol.tiendaguaumiau.views.composables.CommonAlertDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactView(vm: ContactViewModel = viewModel()) {
    val state by vm.state.collectAsState()
    val errors by vm.errors.collectAsState()

    var subjectMenuExpanded by remember { mutableStateOf(false) }

    if (state.showSuccessDialog) {
        CommonAlertDialog(
            title = "¡Mensaje Enviado!",
            text = "Gracias por contactarnos. Te responderemos a la brevedad.",
            onDismiss = {
                vm.dismissDialog()
                vm.resetForm()
            }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Contáctanos", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("¿Tienes alguna duda o sugerencia? Estamos aquí para ayudarte.", style = MaterialTheme.typography.bodyLarge)
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Phone, contentDescription = "Teléfono", tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.size(16.dp))
                        Text("+56 9 1234 5678", style = MaterialTheme.typography.bodyLarge)
                    }
                    Divider()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Email, contentDescription = "Email", tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.size(16.dp))
                        Text("guau&miau@contacto.cl", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }

        item { Text("O envíanos un mensaje", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 8.dp)) }

        item { OutlinedTextField(value = state.name, onValueChange = vm::onNameChange, label = { Text("Tu Nombre") }, isError = errors.name != null, supportingText = { errors.name?.let { Text(it) } }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(value = state.email, onValueChange = vm::onEmailChange, label = { Text("Tu Email") }, isError = errors.email != null, supportingText = { errors.email?.let { Text(it) } }, modifier = Modifier.fillMaxWidth()) }

        item {
            ExposedDropdownMenuBox(expanded = subjectMenuExpanded, onExpandedChange = { subjectMenuExpanded = it }) {
                OutlinedTextField(
                    value = state.subject,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Asunto") },
                    isError = errors.subject != null,
                    supportingText = { errors.subject?.let { Text(it) } },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = subjectMenuExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = subjectMenuExpanded, onDismissRequest = { subjectMenuExpanded = false }) {
                    vm.subjects.forEach { subject ->
                        DropdownMenuItem(text = { Text(subject) }, onClick = {
                            vm.onSubjectChange(subject)
                            subjectMenuExpanded = false
                        })
                    }
                }
            }
        }
        
        item { OutlinedTextField(value = state.message, onValueChange = vm::onMessageChange, label = { Text("Tu Mensaje") }, isError = errors.message != null, supportingText = { errors.message?.let { Text(it) } }, modifier = Modifier.fillMaxWidth().height(150.dp)) }

        item {
            Button(
                onClick = { vm.submit() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar Mensaje")
            }
        }
    }
}