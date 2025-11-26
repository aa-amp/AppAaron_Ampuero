package ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import navigation.Screen
import ui.componentes.CustomBottomBar
import ui.componentes.CustomTopBar
import viewmodel.CarritoViewModel
import viewmodel.ProductoViewModel
import model.Producto

@Composable
fun CartScreen(
    onNavigate: (Screen) -> Unit
) {
    val owner = LocalViewModelStoreOwner.current
    val productoViewModel: ProductoViewModel = if (owner != null) viewModel(owner) else viewModel()
    val carritoViewModel: CarritoViewModel = if (owner != null) viewModel(owner) else viewModel()
    val carritoLocal by productoViewModel.carrito.collectAsState()
    val carritoApi by carritoViewModel.carrito.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var compraRealizada by remember { mutableStateOf(false) }
    var sincronizando by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { CustomTopBar(titulo = "Carrito de Compras", colorFondo = Color.Gray) },
        bottomBar = {
            CustomBottomBar(
                cartCount = if (carritoApi.isNotEmpty()) carritoApi.size else carritoLocal.size,
                onNavigate = onNavigate,
                showHome = true,
                showProducts = true
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        scope.launch {
                            sincronizando = true
                            val tres = carritoViewModel.fetchThreePostsAsProductos()
                            tres.forEach { p -> productoViewModel.agregarAlCarrito(p) }
                            sincronizando = false
                            snackbarHostState.showSnackbar("${tres.size} productos cargados al carrito local")
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Cargar 3 productos") }

                Button(
                    onClick = {
                        scope.launch {
                            sincronizando = true
                            productoViewModel.limpiarCarrito()
                            carritoViewModel.clearCarrito()
                            sincronizando = false
                            snackbarHostState.showSnackbar("Carrito local y API vaciados")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Vaciar carrito", color = Color.White)
                }
            }

            if (sincronizando) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

            val apiAsProductos = carritoApi.map { cm -> Producto(cm.nombre, 0, "$${cm.precio}") }
            val localNamesNormalized = carritoLocal.map { it.nombre.trim().lowercase() }.toSet()
            val apiFiltered = apiAsProductos.filter { apiItem ->
                localNamesNormalized.none { it == apiItem.nombre.trim().lowercase() }
            }
            val displayList: List<Producto> = carritoLocal + apiFiltered

            if (displayList.isEmpty() && !compraRealizada) {
                Text("Tu carrito está vacío")
            } else {
                displayList.forEachIndexed { index, producto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = producto.nombre,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = producto.precio,
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(horizontalAlignment = Alignment.End) {
                                Button(onClick = {
                                    scope.launch {
                                        val matchedApi = carritoApi.find { it.nombre.trim().lowercase() == producto.nombre.trim().lowercase() }
                                        if (matchedApi != null) {
                                            carritoViewModel.deleteCarritoItem(matchedApi.id)
                                            snackbarHostState.showSnackbar("${matchedApi.nombre} eliminado (API)")
                                        } else {
                                            productoViewModel.eliminarDelCarrito(producto)
                                            snackbarHostState.showSnackbar("${producto.nombre} eliminado (local)")
                                        }
                                    }
                                }) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    compraRealizada = true
                    scope.launch {
                        sincronizando = true
                        productoViewModel.limpiarCarrito()
                        carritoViewModel.clearCarrito()
                        sincronizando = false
                        snackbarHostState.showSnackbar("Compra realizada y carrito vaciado")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = if (compraRealizada) Color.Green else MaterialTheme.colorScheme.primary)
            ) {
                Text("Finalizar Compra")
            }

            if (compraRealizada) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFDFF6DD))
                ) {
                    Text(
                        text = "Compra realizada, se enviará a su dirección registrada.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
        }
    }
}