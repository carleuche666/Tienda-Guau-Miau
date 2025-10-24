package com.vivitasol.carcasamvvm.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vivitasol.carcasamvvm.data.UserSessionPrefs
import com.vivitasol.carcasamvvm.viewmodels.SubscriptionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionView(vm: SubscriptionViewModel = viewModel()) {
    val ui = vm.ui.collectAsState().value
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Cargar preferencia al abrir
    LaunchedEffect(Unit) {
        UserSessionPrefs.suscripcionFlow(context).collect { saved ->
            if (ui.suscripcion != saved) vm.setSuscripcion(saved)
        }
    }
    // Guardar cada cambio
    LaunchedEffect(ui.suscripcion) {
        UserSessionPrefs.setSuscripcion(context, ui.suscripcion)
    }

    var isLoading by rememberSaveable { mutableStateOf(false) }
    var errorMsg by rememberSaveable { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Clase 4: Estado + Persistencia + Animaciones", style = MaterialTheme.typography.titleLarge)
                Text("Demostración: loading overlay y alerta de error; preferencia con DataStore.")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Suscribirme (persistente)")
                    Switch(
                        checked = ui.suscripcion,
                        onCheckedChange = vm::setSuscripcion
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        enabled = !isLoading,
                        onClick = {
                            scope.launch {
                                isLoading = true
                                delay(1400)
                                val fallo = (0..100).random() < 40
                                if (fallo) {
                                    errorMsg = "Error de red simulado. Intenta nuevamente."
                                } else {
                                    snackbarHostState.showSnackbar("Operación exitosa ✅")
                                }
                                isLoading = false
                            }
                        }
                    ) { Text("Simular envío") }

                    OutlinedButton(
                        enabled = !isLoading,
                        onClick = { vm.setSuscripcion(false) }
                    ) { Text("Reset preferencia") }
                }

                Text(
                    "Explicación:\n" +
                            "• isLoading controla el overlay animado.\n" +
                            "• errorMsg activa un AlertDialog.\n" +
                            "• Switch se guarda/lee desde DataStore."
                )
            }

            AnimatedVisibility(visible = isLoading, enter = fadeIn(), exit = fadeOut()) {
                Box(
                    Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.35f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(12.dp))
                        Text("Procesando…", color = Color.White)
                    }
                }
            }

            if (errorMsg != null) {
                AlertDialog(
                    onDismissRequest = { errorMsg = null },
                    title = { Text("Error") },
                    text = { Text(errorMsg!!) },
                    confirmButton = { TextButton(onClick = { errorMsg = null }) { Text("Aceptar") } }
                )
            }
        }
    }
}