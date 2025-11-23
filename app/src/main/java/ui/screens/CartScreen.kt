package ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import navigation.Screen
import ui.componentes.CustomBottomBar
import ui.componentes.CustomTopBar
import viewmodel.ProductoViewModel

@Composable
fun CartScreen(
    onNavigate: (Screen) -> Unit,
    viewModel: ProductoViewModel
) {
    val carrito by viewModel.carrito.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var compraRealizada by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CustomTopBar(
                titulo = "Carrito de Compras",
                colorFondo = Color.Gray
            )
        },
        bottomBar = {
            CustomBottomBar(
                cartCount = carrito.size,
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
            if (carrito.isEmpty() && !compraRealizada) {
                Text("Tu carrito está vacío")
            } else {
                carrito.forEach { producto ->
                    AnimatedVisibility(
                        visible = carrito.contains(producto),
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(producto.nombre)
                                    Text(
                                        producto.precio,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                                Button(
                                    onClick = {
                                        viewModel.eliminarDelCarrito(producto)
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                "${producto.nombre} eliminado del carrito"
                                            )
                                        }
                                    }
                                ) {
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
                    viewModel.limpiarCarrito()
                    scope.launch {
                        snackbarHostState.showSnackbar("Carrito vaciado")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Vaciar carrito", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                compraRealizada = true
                        scope.launch {
                    kotlinx.coroutines.delay(500)
                    viewModel.limpiarCarrito()
                        }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (compraRealizada) Color.Green else MaterialTheme.colorScheme.primary
        )

        ) {
                Text("Finalizar Compra")
            }
            AnimatedVisibility(
                visible = compraRealizada,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
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