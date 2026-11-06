package com.tecsup.authfirebaseappleon

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Nombres de las rutas (pantallas) de la app
object Destinations {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
}

// Composable raíz: crea el NavController que controla la navegación
@Composable
fun AuthApp() {
    val navController = rememberNavController()
    AuthNavGraph(navController)
}

// Grafo de navegación: define qué pantalla se muestra en cada ruta
@Composable
fun AuthNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        // La app siempre inicia en el Login
        startDestination = Destinations.LOGIN
    ) {
        // ---------- Pantalla de Login ----------
        composable(Destinations.LOGIN) {
            LoginScreen(
                // Botón "Regístrate aquí" -> va al Registro
                onNavigateToRegister = {
                    navController.navigate(Destinations.REGISTER)
                },
                // Login correcto -> va al Home y elimina el Login de la pila (no se puede volver con atrás)
                onLoginSuccess = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // ---------- Pantalla de Registro ----------
        composable(Destinations.REGISTER) {
            RegisterScreen(
                // Usuario creado -> entra directo al Home
                onRegisterSuccess = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.LOGIN) { inclusive = true }
                    }
                },
                // "← Volver al login" -> regresa a la pantalla anterior
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // ---------- Pantalla de Bienvenida / Mis Cursos ----------
        composable(Destinations.HOME) {
            HomeScreen(
                // Cerrar sesión -> vuelve al Login y limpia la pila
                onLogout = {
                    navController.navigate(Destinations.LOGIN) {
                        popUpTo(Destinations.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}
