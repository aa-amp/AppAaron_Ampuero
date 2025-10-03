package ui.navigation // ejemplo de paquete

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import navigation.Screen
import ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen { screen -> navController.navigate(screen.route) }
        }
        composable(Screen.Product.route) {
            ProductScreen { screen -> navController.navigate(screen.route) }
        }
        composable(Screen.Login.route) {
            LoginScreen { screen -> navController.navigate(screen.route) }
        }
        composable(Screen.Registro.route) {
            RegistroScreen { screen -> navController.navigate(screen.route) }
        }
        composable(Screen.Cart.route) {
            CartScreen { screen -> navController.navigate(screen.route) }
        }
    }
}


