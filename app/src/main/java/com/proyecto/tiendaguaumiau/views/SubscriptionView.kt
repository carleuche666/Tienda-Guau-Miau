package com.vivitasol.tiendaguaumiau.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vivitasol.tiendaguaumiau.data.UserSessionPrefs
import com.vivitasol.tiendaguaumiau.viewmodels.SubscriptionViewModel
import com.vivitasol.tiendaguaumiau.views.composables.CommonAlertDialog

@Composable
fun SubscriptionView(vm: SubscriptionViewModel = viewModel()) {
    val ui = vm.ui.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        UserSessionPrefs.suscripcionFlow(context).collect { saved ->
            if (ui.suscripcion != saved) vm.updateSubscriptionState(saved)
        }
    }
    LaunchedEffect(ui.suscripcion) {
        UserSessionPrefs.setSuscripcion(context, ui.suscripcion)
    }

    if (ui.alertMessage != null) {
        CommonAlertDialog(
            title = "Suscripción",
            text = ui.alertMessage!!,
            onDismiss = { vm.dismissAlert() }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "¡Únete a Nuestro Club!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Como miembro del Club Guau&Miau, recibirás acceso exclusivo a:",
            style = MaterialTheme.typography.bodyLarge
        )
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text("✓ Cupones de descuento especiales")
            Text("✓ Novedades sobre nuestros últimos productos")
            Text("✓ Consejos para el cuidado de tu mascota")
        }
        
        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Sí, ¡quiero unirme al club!", style = MaterialTheme.typography.titleMedium)
            Switch(
                checked = ui.suscripcion,
                onCheckedChange = { vm.toggleSubscription(it) }
            )
        }
    }
}