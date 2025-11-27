package com.vivitasol.tiendaguaumiau.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.vivitasol.tiendaguaumiau.data.UserSessionPrefs
import com.vivitasol.tiendaguaumiau.navigation.Route
import com.vivitasol.tiendaguaumiau.viewmodels.ProfileViewModel
import com.vivitasol.tiendaguaumiau.viewmodels.ProfileViewModelFactory
import com.vivitasol.tiendaguaumiau.views.composables.LoadingOverlay
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuShellView(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val innerNavController = rememberNavController()
    val context = LocalContext.current
    var isLoggingOut by remember { mutableStateOf(false) }

    val profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(context))
    // Obtenemos el estado del ViewModel del perfil
    val profileUiState by profileViewModel.uiState.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menú", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
                Divider()

                NavigationDrawerItem(
                    label = { Text("Productos") },
                    selected = currentInnerRoute(innerNavController) == Route.Option1.route,
                    onClick = { innerNavController.navigate(Route.Option1.route) { launchSingleTop = true }; scope.launch { drawerState.close() } }
                )
                NavigationDrawerItem(
                    label = { Text("Mi Perfil") },
                    selected = currentInnerRoute(innerNavController) == Route.Option2.route,
                    onClick = { innerNavController.navigate(Route.Option2.route) { launchSingleTop = true }; scope.launch { drawerState.close() } }
                )
                NavigationDrawerItem(
                    label = { Text("Mis Mascotas") },
                    selected = currentInnerRoute(innerNavController) == "pet_api_test",
                    onClick = {
                        innerNavController.navigate("pet_api_test") { launchSingleTop = true }
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Contáctanos") },
                    selected = currentInnerRoute(innerNavController) == Route.Option3.route,
                    onClick = { innerNavController.navigate(Route.Option3.route) { launchSingleTop = true }; scope.launch { drawerState.close() } }
                )
                NavigationDrawerItem(
                    label = { Text("Nosotros") },
                    selected = currentInnerRoute(innerNavController) == Route.AboutUs.route,
                    onClick = { innerNavController.navigate(Route.AboutUs.route) { launchSingleTop = true }; scope.launch { drawerState.close() } }
                )
                NavigationDrawerItem(
                    label = { Text("Suscripción") },
                    selected = currentInnerRoute(innerNavController) == Route.Option4.route,
                    onClick = { innerNavController.navigate(Route.Option4.route) { launchSingleTop = true }; scope.launch { drawerState.close() } }
                )



                Divider(modifier = Modifier.padding(vertical = 8.dp))

                NavigationDrawerItem(
                    label = { Text("Cerrar Sesión") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            isLoggingOut = true
                            drawerState.close()
                            delay(1500)
                            UserSessionPrefs.setIsLoggedIn(context, false)
                            navController.navigate(Route.Login.route) {
                                popUpTo(Route.MenuShell.route) { inclusive = true }
                            }
                            isLoggingOut = false
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
        ) { innerScaffoldPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                NavHost(
                    navController = innerNavController,
                    startDestination = Route.Option1.route,
                    modifier = Modifier.padding(innerScaffoldPadding)
                ) {
                    composable(Route.Option1.route) { ProductsView(navController = innerNavController) }
                    composable(Route.ProductDetail.route) { backStackEntry ->
                        val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                        if (productId != null) {
                            ProductDetailView(productId = productId) { innerNavController.navigateUp() }
                        }
                    }
                    composable(Route.AboutUs.route) { AboutUsView() }
                    composable(Route.Option2.route) { ProfileView(navController = innerNavController, viewModel = profileViewModel) }
                    composable(Route.Option3.route) { ContactView() }
                    composable(Route.Option4.route) { SubscriptionView() }
                    composable(Route.Option5.route) {
                        CameraView(onPhotoTaken = {
                            profileViewModel.updatePhoto(it)
                            innerNavController.popBackStack()
                        })
                    }

                    composable("pet_api_test") {
                        // Usamos el email del perfil del usuario actual
                        val userEmail = profileUiState.userProfile?.email ?: ""
                        PetApiTestView(userEmail = userEmail)
                    }
                }

                if (isLoggingOut) {
                    LoadingOverlay(isLoading = true, text = "Cerrando Sesión...")
                }
            }
        }
    }
}

@Composable
private fun currentInnerRoute(navController: NavHostController): String? {
    val entry by navController.currentBackStackEntryAsState()
    return entry?.destination?.route
}