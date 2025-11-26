package ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import ui.screens.*
import navigation.Screen
import viewmodel.ProductoViewModel
import viewmodel.UsuarioViewModel
import viewmodel.CarritoViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val productoViewModel: ProductoViewModel = viewModel()
    val carritoViewModel: CarritoViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(onNavigate = { screen -> navController.navigate(screen.route) })
        }
        composable(Screen.Product.route) {
            ProductScreen(onNavigate = { screen -> navController.navigate(screen.route) })
        }
        composable(Screen.Registro.route) {
            RegistroScreen(
                onNavigate = { screen -> navController.navigate(screen.route) },
                viewModel = usuarioViewModel
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(onNavigate = { screen -> navController.navigate(screen.route) }, viewModel = usuarioViewModel)
        }
        composable(Screen.Cart.route) {
            CartScreen(onNavigate = { screen -> navController.navigate(screen.route) })
        }
        composable(Screen.Resumen.route) {
            ResumenScreen(viewModel = usuarioViewModel)
        }
        composable(Screen.Dueno.route) {
            DuenoScreen(onNavigate = { screen -> navController.navigate(screen.route) })
        }
        composable(Screen.AgregarProducto.route) {
            CrearProductoScreen(viewModel = productoViewModel, onDone = { navController.popBackStack() })
        }
        composable(Screen.GestionarProductos.route) {
            GestionarProductosScreen(onNavigate = { screen -> navController.navigate(screen.route) })
        }
        composable(Screen.PerfilDueno.route) {
            PerfilDuenoScreen(onNavigate = { route -> navController.navigate(route.route) })
        }
        composable(Screen.UsuarioDatos.route) {
            UsuariosDatosScreen()
        }
    }
}