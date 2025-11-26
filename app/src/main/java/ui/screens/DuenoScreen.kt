package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import navigation.Screen
import ui.componentes.CustomBottomBar
import ui.componentes.CustomTopBar
import viewmodel.DuenoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuenoScreen(
    onNavigate: (Screen) -> Unit,
    viewModel: DuenoViewModel = viewModel()
) {
    val dueno by viewModel.dueno.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState(initial = false)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            CustomTopBar(
                titulo = "Panel del Administrador",
                colorFondo = Color.Gray
            )
        },
        bottomBar = {
            CustomBottomBar(
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("Bienvenido", style = MaterialTheme.typography.headlineMedium)

            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

            Text("Resumen de inventario: 12 productos", style = MaterialTheme.typography.bodyLarge)
            Text("Stock bajo: 3 productos", style = MaterialTheme.typography.bodyMedium)

            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
            Text("Ventas esta semana: $120.000", style = MaterialTheme.typography.bodyLarge)

            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
            Button(onClick = { onNavigate(Screen.AgregarProducto) }, modifier = Modifier.fillMaxWidth()) {
                Text("Agregar producto")
            }
            Button(onClick = { onNavigate(Screen.GestionarProductos) }, modifier = Modifier.fillMaxWidth()) {
                Text("Editar / Eliminar producto")
            }
            Button(
                onClick = { onNavigate(Screen.PerfilDueno) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver perfil del dueño")
            }
            Button(onClick = { onNavigate(Screen.UsuarioDatos) }, modifier = Modifier.fillMaxWidth()) {
                Text("Ver Lista de Usuarios")
            }
        }
    }
}