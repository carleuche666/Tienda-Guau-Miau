package com.vivitasol.carcasamvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vivitasol.carcasamvvm.navigation.Route
import com.vivitasol.carcasamvvm.views.LoginView
import com.vivitasol.carcasamvvm.views.MenuShellView
import com.vivitasol.carcasamvvm.views.RegisterView
import com.vivitasol.carcasamvvm.views.SplashView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = MaterialTheme.colorScheme.background) {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Route.Splash.route
                ) {
                    composable(Route.Splash.route) {
                        SplashView(navController = navController)
                    }
                    composable(Route.Login.route) {
                        LoginView(
                            onLoginSuccess = {
                                navController.navigate(Route.MenuShell.route) {
                                    popUpTo(Route.Login.route) { inclusive = true }
                                }
                            },
                            onRegisterClick = { navController.navigate(Route.Register.route) }
                        )
                    }
                    composable(Route.Register.route) {
                        RegisterView(
                            onRegisterSuccess = {
                                navController.navigate(Route.Login.route) {
                                    popUpTo(Route.Register.route) { inclusive = true }
                                }
                            },
                            onBackToLoginClick = { navController.navigateUp() } // Added this line
                        )
                    }
                    composable(Route.MenuShell.route) {
                        MenuShellView(navController = navController)
                    }
                }
            }
        }
    }
}