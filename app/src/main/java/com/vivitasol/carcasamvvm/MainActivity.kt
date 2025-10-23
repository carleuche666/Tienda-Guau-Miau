package com.vivitasol.carcasamvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vivitasol.carcasamvvm.data.UserSessionPrefs
import com.vivitasol.carcasamvvm.navigation.Route
import com.vivitasol.carcasamvvm.views.LoginView
import com.vivitasol.carcasamvvm.views.MenuShellView
import com.vivitasol.carcasamvvm.views.RegisterView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // WARNING: this is a temporary solution for the initial route,
        // a proper implementation should use a splash screen with a viewmodel.
        val isLoggedIn = runBlocking { UserSessionPrefs.getIsLoggedInFlow(this@MainActivity).first() }
        val startDestination = if (isLoggedIn) Route.MenuShell.route else Route.Login.route

        setContent {
            Surface(color = MaterialTheme.colorScheme.background) {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {
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
                            }
                        )
                    }
                    composable(Route.MenuShell.route) {
                        MenuShellView()
                    }
                }
            }
        }
    }
}