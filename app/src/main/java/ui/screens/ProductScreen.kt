package ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import navigation.Screen
import ui.componentes.CustomBottomBar
import ui.componentes.CustomTopBar
import viewmodel.ProductoViewModel
import model.Producto
import model.ProductoModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProductScreen(
    onNavigate: (Screen) -> Unit
) {
    val owner = LocalViewModelStoreOwner.current
    val viewModel: ProductoViewModel = if (owner != null) viewModel(owner) else viewModel()
    val productosLocal by viewModel.productosLocal.collectAsState()
    val productosApi by viewModel.productosApi.collectAsState()
    val carrito by viewModel.carrito.collectAsState()
    val cartCount = carrito.size
    var aviso by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var mostrarProductosApi by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { CustomTopBar(titulo = "Productos", colorFondo = Color.Gray) },
        bottomBar = {
            CustomBottomBar(
                cartCount = cartCount,
                onNavigate = onNavigate,
                showHome = true,
                showCart = true
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        mostrarProductosApi = false
                        scope.launch { snackbarHostState.showSnackbar("Mostrando productos locales") }
                    }) {
                        Text("Productos Locales")
                    }
                    Button(onClick = {
                        mostrarProductosApi = true
                        viewModel.getProductos()
                        scope.launch { snackbarHostState.showSnackbar("Cargando productos desde API") }
                    }) {
                        Text("Productos API")
                    }
                }

                if (mostrarProductosApi) {
                    val existentes = productosApi.filter { it.id > 0 }
                    if (existentes.isEmpty()) {
                        Text("No hay productos cargados desde la API", modifier = Modifier.padding(16.dp))
                    } else {
                        val gridState = rememberLazyGridState()
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            state = gridState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(existentes.size) { idx ->
                                val producto = existentes[idx]
                                ProductoApiItem(
                                    producto = producto,
                                    onAddToCart = {
                                        val productoLocal = Producto(
                                            nombre = producto.nombre,
                                            imagenRes = if (producto.imagenRes != 0) producto.imagenRes else com.example.appaaron_ampuero.R.drawable.`product_placeholder`,
                                            precio = "$${producto.precio}"
                                        )
                                        viewModel.agregarAlCarrito(productoLocal)
                                        aviso = "${producto.nombre} agregado al carrito"
                                    }
                                )
                            }
                        }
                    }
                } else {
                    val gridState = rememberLazyGridState()
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        state = gridState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(productosLocal.size) { idx ->
                            val producto = productosLocal[idx]
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
                }
            }

            AnimatedVisibility(
                visible = aviso != null,
                enter = androidx.compose.animation.slideInHorizontally(initialOffsetX = { it }) + androidx.compose.animation.fadeIn(),
                exit = androidx.compose.animation.slideOutHorizontally(targetOffsetX = { it }) + androidx.compose.animation.fadeOut(),
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
        }
    }

    LaunchedEffect(aviso) {
        if (aviso != null) {
            delay(1000)
            aviso = null
        }
    }
}

@Composable
fun ProductoApiItem(producto: ProductoModel, onAddToCart: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageRes = if (producto.imagenRes != 0) producto.imagenRes else com.example.appaaron_ampuero.R.drawable.`product_placeholder`
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(producto.nombre, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "$${producto.precio}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onAddToCart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Comprar")
            }
        }
    }
}