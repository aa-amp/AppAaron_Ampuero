package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import model.Usuarios
import ui.componentes.CustomTopBar
import viewmodel.UsuariosDatosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosDatosScreen(viewModel: UsuariosDatosViewModel = viewModel()) {
    val usuarios by viewModel.usuarios.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.error.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var usuarioAEliminar by remember { mutableStateOf<String?>(null) }
    var usuarioEditando by remember { mutableStateOf<Usuarios?>(null) }
    var creando by remember { mutableStateOf(false) }

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
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = { creando = true }) {
                            Text("Agregar usuario")
                        }
                        Button(onClick = { viewModel.loadUsuarios() }) {
                            Text("Actualizar")
                        }
                    }

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
                                    Text("ID : ${user.id}", style = MaterialTheme.typography.labelSmall)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Nombre: ${user.name}", style = MaterialTheme.typography.titleMedium)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Nombre de usuario: ${user.username}", style = MaterialTheme.typography.bodySmall)

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row {
                                        Button(onClick = { usuarioEditando = user }) { Text("Editar") }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Button(onClick = { usuarioAEliminar = user.id }) { Text("Eliminar") }
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
                            val idString = usuarioAEliminar
                            usuarioAEliminar = null
                            if (!idString.isNullOrBlank()) {
                                viewModel.eliminarUsuario(idString) { success ->
                                    scope.launch {
                                        if (success) snackbarHostState.showSnackbar("Usuario eliminado")
                                        else snackbarHostState.showSnackbar("Error al eliminar usuario")
                                    }
                                }
                            } else {
                                scope.launch { snackbarHostState.showSnackbar("ID inválido") }
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
                            val idString: String? = usuario.id
                            if (!idString.isNullOrBlank()) {
                                viewModel.actualizarUsuario(idString, actualizado) { updated ->
                                    scope.launch {
                                        if (updated != null) snackbarHostState.showSnackbar("Usuario actualizado")
                                        else snackbarHostState.showSnackbar("Error al actualizar usuario")
                                    }
                                }
                            } else {
                                scope.launch { snackbarHostState.showSnackbar("ID inválido") }
                            }
                        }) { Text("Guardar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { usuarioEditando = null }) { Text("Cancelar") }
                    }
                )
            }

            if (creando) {
                var idText by remember { mutableStateOf("") }
                var nombre by remember { mutableStateOf("") }
                var username by remember { mutableStateOf("") }
                var email by remember { mutableStateOf("") }

                AlertDialog(
                    onDismissRequest = { creando = false },
                    title = { Text("Nuevo usuario") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = idText,
                                onValueChange = { idText = it.filter { ch -> ch.isDigit() } },
                                label = { Text("ID (opcional, solo números)") }
                            )
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            creando = false
                            val idInt: Int? = idText.trim().takeIf { it.isNotBlank() }?.toIntOrNull()
                            val nuevo = Usuarios(id = idInt?.toString(), name = nombre, username = username, email = email)
                            viewModel.crearUsuario(nuevo) { creado ->
                                scope.launch {
                                    if (creado != null) snackbarHostState.showSnackbar("Usuario creado")
                                    else snackbarHostState.showSnackbar("Error al crear usuario")
                                }
                            }
                        }) { Text("Crear") }
                    },
                    dismissButton = {
                        TextButton(onClick = { creando = false }) { Text("Cancelar") }
                    }
                )
            }
        }
    }
}