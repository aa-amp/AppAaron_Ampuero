package ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import navigation.Screen
import ui.componentes.CustomBottomBar
import ui.componentes.CustomTopBar
import viewmodel.ProductoViewModel
import model.ProductoModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionarProductosScreen(
    onNavigate: (Screen) -> Unit,
    productoViewModel: ProductoViewModel = viewModel()
) {
    val productosApi by productoViewModel.productosApi.collectAsState()
    val isLoading by productoViewModel.isLoading.collectAsState()
    val errorMessage by productoViewModel.errorMessage.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var editingProducto by remember { mutableStateOf<ProductoModel?>(null) }
    var showConfirmDelete by remember { mutableStateOf<Pair<Int, String>?>(null) }

    LaunchedEffect(Unit) { productoViewModel.getProductosFromPosts() }

    Scaffold(
        topBar = {
            CustomTopBar(titulo = "Editar o Eliminar Productos", colorFondo = MaterialTheme.colorScheme.primary)
        },
        bottomBar = {
            CustomBottomBar(onNavigate = onNavigate, showHome = true, showAdmin = true)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
            }

            errorMessage?.let { err ->
                Text(text = err, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (productosApi.isEmpty()) {
                Text("No hay productos desde la API", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(items = productosApi, key = { it.id }) { producto ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = producto.descripcion, style = MaterialTheme.typography.bodyMedium)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "Precio: ${producto.precio}", style = MaterialTheme.typography.bodySmall)
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    TextButton(onClick = {
                                        editingProducto = producto.copy()
                                    }) {
                                        Text("Editar")
                                    }
                                    TextButton(onClick = {
                                        showConfirmDelete = producto.id to producto.nombre
                                    }) {
                                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    showConfirmDelete?.let { (id, nombre) ->
        AlertDialog(
            onDismissRequest = { showConfirmDelete = null },
            title = { Text("Eliminar producto") },
            text = { Text("¿Eliminar \"$nombre\"? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmDelete = null
                    scope.launch {
                        productoViewModel.deleteProducto(id)
                        snackbarHostState.showSnackbar("Producto eliminado")
                        productoViewModel.getProductosFromPosts()
                    }
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDelete = null }) { Text("Cancelar") }
            }
        )
    }

    editingProducto?.let { current ->
        var nombre by remember { mutableStateOf(TextFieldValue(current.nombre)) }
        var descripcion by remember { mutableStateOf(TextFieldValue(current.descripcion)) }
        var precioText by remember { mutableStateOf(TextFieldValue(current.precio.toString())) }
        var stockText by remember { mutableStateOf(TextFieldValue(current.stock.toString())) }
        var categoria by remember { mutableStateOf(TextFieldValue(current.categoria)) }

        AlertDialog(
            onDismissRequest = { editingProducto = null },
            title = { Text("Editar producto") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                    OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
                    OutlinedTextField(value = precioText, onValueChange = { precioText = it }, label = { Text("Precio (número)") })
                    OutlinedTextField(value = stockText, onValueChange = { stockText = it }, label = { Text("Stock (número)") })
                    OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") })
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val precio = precioText.text.toIntOrNull() ?: 0
                    val stock = stockText.text.toIntOrNull() ?: 0
                    val productoToSend = ProductoModel(
                        id = current.id,
                        nombre = nombre.text,
                        descripcion = descripcion.text,
                        precio = precio,
                        stock = stock,
                        imagenRes = current.imagenRes,
                        categoria = categoria.text
                    )
                    editingProducto = null
                    scope.launch {
                        productoViewModel.updateProducto(current.id, productoToSend)
                        snackbarHostState.showSnackbar("Producto actualizado")
                        productoViewModel.getProductosFromPosts()
                    }
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingProducto = null }) { Text("Cancelar") }
            }
        )
    }
}