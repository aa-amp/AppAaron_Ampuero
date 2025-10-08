package ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import navigation.Screen
import ui.screens.*
import viewmodel.DuenoViewModel
import viewmodel.ProductoViewModel
import viewmodel.UsuarioViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val productoViewModel: ProductoViewModel = viewModel()
    val duenoViewModel: DuenoViewModel = viewModel()


    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen { screen -> navController.navigate(screen.route) }
        }
        composable(Screen.Product.route) {
            ProductScreen(
                onNavigate = { screen -> navController.navigate(screen.route) },
                viewModel = productoViewModel
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigate = { screen -> navController.navigate(screen.route) },
                viewModel = usuarioViewModel
            )
        }

        composable(Screen.Registro.route) {
            RegistroScreen(
                onNavigate = { screen -> navController.navigate(screen.route) },
                viewModel = usuarioViewModel
            )
        }
        composable(Screen.Cart.route) {

            CartScreen(
                onNavigate = { screen -> navController.navigate(screen.route) },
                viewModel = productoViewModel
            )
        }

        composable(Screen.Resumen.route) {
            ResumenScreen(viewModel = usuarioViewModel)
        }

        composable(Screen.Dueno.route) {
            DuenoScreen { screen -> navController.navigate(screen.route) }
        }

        composable(Screen.AgregarProducto.route) {
            AgregarProductoScreen{screen -> navController.navigate(screen.route)}
        }
        composable(Screen.GestionarProductos.route) {
            GestionarProductosScreen{screen -> navController.navigate(screen.route)}
        }
        composable(Screen.PerfilDueno.route) {
            PerfilDuenoScreen(viewModel = duenoViewModel)
        }
    }
}