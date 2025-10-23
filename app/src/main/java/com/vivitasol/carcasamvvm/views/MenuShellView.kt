package com.vivitasol.carcasamvvm.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.vivitasol.carcasamvvm.data.UserSessionPrefs
import com.vivitasol.carcasamvvm.navigation.Route
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuShellView(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val innerNavController = rememberNavController()
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menú", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))

                NavigationDrawerItem(
                    label = { Text("Productos") },
                    selected = currentInnerRoute(innerNavController) == Route.Option1.route,
                    onClick = {
                        innerNavController.navigate(Route.Option1.route) {
                            popUpTo(Route.Option1.route) { inclusive = false }
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Mi Perfil") },
                    selected = currentInnerRoute(innerNavController) == Route.Option2.route,
                    onClick = {
                        innerNavController.navigate(Route.Option2.route) {
                            popUpTo(Route.Option1.route) { inclusive = false }
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Contáctanos") },
                    selected = currentInnerRoute(innerNavController) == Route.Option3.route,
                    onClick = {
                        innerNavController.navigate(Route.Option3.route) {
                            popUpTo(Route.Option1.route) { inclusive = false }
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Suscripción") },
                    selected = currentInnerRoute(innerNavController) == Route.Option4.route,
                    onClick = {
                        innerNavController.navigate(Route.Option4.route) {
                            popUpTo(Route.Option1.route) { inclusive = false }
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Foto de Mascota") },
                    selected = currentInnerRoute(innerNavController) == Route.Option5.route,
                    onClick = {
                        innerNavController.navigate(Route.Option5.route) {
                            popUpTo(Route.Option1.route) { inclusive = false }
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                NavigationDrawerItem(
                    label = { Text("Cerrar Sesión") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            UserSessionPrefs.setIsLoggedIn(context, false)
                            navController.navigate(Route.Login.route) {
                                popUpTo(Route.MenuShell.route) { inclusive = true }
                            }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Guau&Miau") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = innerNavController,
                startDestination = Route.Option1.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Route.Option1.route) { 
                    Option1View(navController = innerNavController) 
                }
                composable(Route.ProductDetail.route) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                    if (productId != null) {
                        ProductDetailView(productId = productId) { 
                            innerNavController.navigateUp() 
                        }
                    }
                }

                // --- Other composables ---
                composable(Route.Option2.route) { Option2View(navController = innerNavController) }
                composable(Route.Option3.route) { Option3View() }
                
                /*
                // RUTA TEMPORALMENTE DESHABILITADA POR ERROR
                composable(Route.Option2Detail.route) { backStack ->
                    val id = backStack.arguments?.getString("id") ?: "sin-id"
                    // Option2DetailView(id = id, onBack = { innerNavController.navigateUp() })
                }
                */

                composable(Route.Option4.route) { Option4View() }
                composable(Route.Option5.route) { Option5CameraView() }
            }
        }
    }
}

@Composable
private fun currentInnerRoute(navController: NavHostController): String? {
    val entry by navController.currentBackStackEntryAsState()
    return entry?.destination?.route
}