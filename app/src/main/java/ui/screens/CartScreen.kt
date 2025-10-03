package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import navigation.Screen

@Composable
fun CartScreen(onNavigate: (Screen) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Carrito de Compras", style = MaterialTheme.typography.headlineMedium)

        Text("Aquí se mostrarán los productos agregados")

        Button(
            onClick = { /* acción futura de pagar */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finalizar Compra")
        }
    }
}
