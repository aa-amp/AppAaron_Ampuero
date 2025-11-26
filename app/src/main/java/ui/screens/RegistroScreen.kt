package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import model.Usuarios
import viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    onNavigate: (navigation.Screen) -> Unit,
    viewModel: UsuarioViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var aceptaTerminos by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Registro de usuario") }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = clave,
                onValueChange = { clave = it },
                label = { Text("Clave") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(checked = aceptaTerminos, onCheckedChange = { aceptaTerminos = it })
                Spacer(modifier = Modifier.width(8.dp))
                Text("Acepto términos y condiciones")
            }

            Button(
                onClick = {
                    if (!aceptaTerminos) {
                        scope.launch { snackbarHostState.showSnackbar("Debes aceptar los términos") }
                        return@Button
                    }
                    if (!email.contains("@duocuc.cl")) {
                        scope.launch { snackbarHostState.showSnackbar("El correo debe contener @duocuc.cl") }
                        return@Button
                    }
                    if (clave.length < 6) {
                        scope.launch { snackbarHostState.showSnackbar("La contraseña debe tener al menos 6 caracteres") }
                        return@Button
                    }

                    viewModel.onNombreChange(nombre)
                    viewModel.onCorreoChange(email)
                    viewModel.onDireccionChange(direccion)
                    viewModel.onClaveChange(clave)
                    viewModel.onAceptarTerminosChange(aceptaTerminos)

                    val nuevo = Usuarios(id = null, name = nombre, username = username, email = email)
                    viewModel.registrarUsuarioLocal(nuevo) {
                        onNavigate(navigation.Screen.Resumen)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }
        }
    }
}