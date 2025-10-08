package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import navigation.Screen
import ui.components.CustomBottomBar
import ui.components.CustomTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionarProductosScreen(onNavigate: (Screen) -> Unit) {
    Scaffold(
        topBar = {
            CustomTopBar(
                titulo = "Editar o Eliminar Productos",
                colorFondo = Color.Gray
            )
        },
        bottomBar = {
            CustomBottomBar(
                onNavigate = onNavigate,
                showHome = true,
                showAdmin = true
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Estamos trabajando para usted")
        }}}