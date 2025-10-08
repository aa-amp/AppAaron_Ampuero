package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import navigation.Screen

@Composable
fun CustomBottomBar(
    cartCount: Int = 0,
    onNavigate: (Screen) -> Unit,
    showHome: Boolean = false,
    showProducts: Boolean = false,
    showCart: Boolean = false,
    showAdmin: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showHome) {
            IconButton(onClick = { onNavigate(Screen.Home) }) {
                Icon(Icons.Default.Home, contentDescription = "Ir a inicio")
            }
        }
        if (showProducts) {
            IconButton(onClick = { onNavigate(Screen.Product) }) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Ir a productos")
            }
        }
        if (showCart) {
            BadgedBox(
                badge = {
                    if (cartCount > 0) {
                        Badge { Text("$cartCount") }
                    }
                }
            ) {
                IconButton(onClick = { onNavigate(Screen.Cart) }) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Ir al carrito")
                }
            }
        }
        if (showAdmin) {
            IconButton(onClick = { onNavigate(Screen.Dueno) }) {
                Icon(Icons.Default.Person, contentDescription = "Panel administrador")
            }

        }
}}