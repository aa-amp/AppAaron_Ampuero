package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import navigation.Screen
import ui.componentes.CustomBottomBar
import ui.componentes.CustomTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuenoScreen(
    onNavigate: (Screen) -> Unit
) {
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
        }
    )  { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text("Bienvenido",style = MaterialTheme.typography.headlineMedium)


            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            Text("Resumen de inventario: 12 productos")
            Text("Stock bajo: 3 productos")

            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            Text("Ventas esta semana: $120.000")

            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            Button(onClick = { onNavigate(Screen.AgregarProducto) }) {
                Text("Agregar producto")
            }

            Button(onClick = { onNavigate(Screen.GestionarProductos) }) {
                Text("Editar / Eliminar producto")
            }
            Button(
                onClick = { onNavigate(Screen.PerfilDueno) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver perfil del dueño")
            }
            Button(onClick = { onNavigate(Screen.UsuarioDatos)}) {
                Text("Ver Lista de Usuarios")
            }
        }
    }
}