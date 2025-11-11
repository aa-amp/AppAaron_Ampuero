package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import viewmodel.UsuariosDatosViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import ui.components.CustomTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosDatosScreen(viewModel: UsuariosDatosViewModel = viewModel()) {
    val usuarios = viewModel.usuariosList.collectAsState().value

    Scaffold(
        topBar = {
            CustomTopBar(
                titulo = "Listado de Usuarios",
                colorFondo = Color(0xFFB3E5FC)
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                items(usuarios) { user ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text= "ID : ${user.id}", style = MaterialTheme.typography.labelSmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Nombre: ${user.name}", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Nombre de usuario: ${user.username}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}