package ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import navigation.Screen
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import ui.components.CustomBottomBar
import ui.components.CustomTopBar
import viewmodel.ProductoViewModel



data class Product(
    val nombre: String,
    val imageRes: Int,
    val precio: String
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(onNavigate: (Screen) -> Unit, viewModel: ProductoViewModel = viewModel()) {
    val productos by viewModel.productos.collectAsState()
    val carrito by viewModel.carrito.collectAsState()
    val cartCount = carrito.size

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(topBar = {
        CustomTopBar(
            titulo = "Productos",
            colorFondo = Color.Gray
        )
    },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            CustomBottomBar(
                cartCount = cartCount,
                onNavigate = onNavigate,
                showHome = true,
                showCart = true
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(productos) { producto ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = producto.imageRes),
                            contentDescription = producto.nombre,
                            modifier = Modifier
                                .height(120.dp)
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(producto.nombre, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(producto.precio, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                viewModel.agregarAlCarrito(producto)
                                scope.launch {
                                    snackbarHostState.showSnackbar("${producto.nombre} agregado al carrito!")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Comprar")
                        }
                    }
                }
            }
        }
    }
}