package com.example.appaaron_ampuero.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import navigation.Screen
import ui.components.CustomBottomBar
import ui.components.CustomTopBar
import viewmodel.ProductoViewModel
import model.Producto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(onNavigate: (Screen) -> Unit, viewModel: ProductoViewModel = viewModel()) {
    val productos by viewModel.productos.collectAsState()
    val carrito by viewModel.carrito.collectAsState()
    val cartCount = carrito.size

    var aviso by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CustomTopBar(
                titulo = "Productos",
                colorFondo = Color.Gray
            )
        },
        bottomBar = {
            CustomBottomBar(
                cartCount = cartCount,
                onNavigate = onNavigate,
                showHome = true,
                showCart = true
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
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
                                painter = painterResource(id = producto.imagenRes),
                                contentDescription = producto.nombre,
                                modifier = Modifier
                                    .height(120.dp)
                                    .fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(producto.nombre, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                producto.precio,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    viewModel.agregarAlCarrito(producto)
                                    aviso = "${producto.nombre} agregado al carrito"
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Comprar")
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = aviso != null,
                enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text(
                        text = aviso ?: "",
                        modifier = Modifier.padding(12.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            LaunchedEffect(aviso) {
                if (aviso != null) {
                    kotlinx.coroutines.delay(1000)
                    aviso = null
                }
            }
        }
    }
}
