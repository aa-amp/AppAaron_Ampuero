package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ui.components.CustomTopBar
import viewmodel.UsuariosDatosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosDatosScreen(viewModel: UsuariosDatosViewModel = viewModel()) {
    val usuarios by viewModel.usuariosList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var usuarioAEliminar by remember { mutableStateOf<Int?>(null) }
    var usuarioEditando by remember { mutableStateOf<model.Usuarios?>(null) }

    Scaffold(
        topBar = {
            CustomTopBar(
                titulo = "Listado de Usuarios",
                colorFondo = Color(0xFFB3E5FC)
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(usuarios) { user ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "ID : ${user.id}", style = MaterialTheme.typography.labelSmall)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Nombre: ${user.name}", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Nombre de usuario: ${user.username}", style = MaterialTheme.typography.bodySmall)

                                Spacer(modifier = Modifier.height(12.dp))

                                Row {
                                    Button(onClick = {
                                        usuarioEditando = user
                                    }) {
                                        Text("Editar")
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(onClick = {
                                        usuarioAEliminar = user.id
                                    }) {
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            errorMessage?.let { err ->
                LaunchedEffect(err) {
                    snackbarHostState.showSnackbar(err)
                }
            }

            if (usuarioAEliminar != null) {
                AlertDialog(
                    onDismissRequest = { usuarioAEliminar = null },
                    title = { Text("Confirmar eliminación") },
                    text = { Text("¿Eliminar usuario con id ${usuarioAEliminar}? Esta acción no se puede deshacer.") },
                    confirmButton = {
                        TextButton(onClick = {
                            val id = usuarioAEliminar!!
                            usuarioAEliminar = null
                            viewModel.eliminarUsuario(id) { success, msg ->
                                scope.launch {
                                    if (success) snackbarHostState.showSnackbar("Usuario eliminado")
                                    else snackbarHostState.showSnackbar("Error: ${msg ?: "desconocido"}")
                                }
                            }
                        }) { Text("Eliminar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { usuarioAEliminar = null }) { Text("Cancelar") }
                    }
                )
            }

            if (usuarioEditando != null) {
                val usuario = usuarioEditando!!
                var nombre by remember { mutableStateOf(usuario.name) }
                var username by remember { mutableStateOf(usuario.username) }
                var email by remember { mutableStateOf(usuario.email) }

                AlertDialog(
                    onDismissRequest = { usuarioEditando = null },
                    title = { Text("Editar usuario") },
                    text = {
                        Column {
                            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            val actualizado = usuario.copy(name = nombre, username = username, email = email)
                            usuarioEditando = null
                            viewModel.reemplazarUsuario(usuario.id, actualizado) { success, msg ->
                                scope.launch {
                                    if (success) snackbarHostState.showSnackbar("Usuario actualizado")
                                    else snackbarHostState.showSnackbar("Error: ${msg ?: "desconocido"}")
                                }
                            }
                        }) { Text("Guardar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { usuarioEditando = null }) { Text("Cancelar") }
                    }
                )
            }
        }
    }
}