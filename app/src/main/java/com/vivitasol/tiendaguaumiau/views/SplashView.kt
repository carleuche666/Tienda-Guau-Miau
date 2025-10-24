package com.vivitasol.tiendaguaumiau.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vivitasol.tiendaguaumiau.navigation.Route
import com.vivitasol.tiendaguaumiau.viewmodels.SplashState
import com.vivitasol.tiendaguaumiau.viewmodels.SplashViewModel
import com.vivitasol.tiendaguaumiau.viewmodels.SplashViewModelFactory

@Composable
fun SplashView(navController: NavController) {
    val context = LocalContext.current
    val viewModel: SplashViewModel = viewModel(factory = SplashViewModelFactory(context))
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is SplashState.Finished) {
            val isLoggedIn = (uiState as SplashState.Finished).isLoggedIn
            val destination = if (isLoggedIn) Route.MenuShell.route else Route.Login.route

            navController.navigate(destination) {
                popUpTo(Route.Splash.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}