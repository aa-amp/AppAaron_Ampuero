package ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import navigation.Screen
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.example.appaaron_ampuero.R
import kotlinx.coroutines.launch


@Composable
fun MyBottomBar() {
    BottomAppBar {
        IconButton(onClick = { /* Acción */ }) {
            Icon(Icons.Filled.Home, contentDescription = "Inicio")
        }
    }
}
data class Product(
    val nombre: String,
    val imageRes: Int,
    val precio: String
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(onNavigate: (Screen) -> Unit) {
    var cartCount by remember { mutableStateOf(0) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val productos = listOf(
        Product("Fideos Rigati 400g", R.drawable.product1, "$1.000"),
        Product("Alfi 45g", R.drawable.product2, "$700" ),
        Product("Tableta Bon o Bon 270g", R.drawable.product3, "$3.500"),
        Product("Cheetos 40g", R.drawable.product4, "$1.000"),
        Product("Chocman 33g", R.drawable.product5, "$300"),
        Product("Lays 110g", R.drawable.product6, "$2.000"),
        Product("Golpe 27g", R.drawable.product7, "$500"),
        Product("Coca Cola Zero 1l", R.drawable.product8, "$1.100")
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigate(Screen.Home) },
                    label = { Text("Inicio") },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigate(Screen.Cart) },
                    label = { Text("Carrito ($cartCount)") },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) }
                )
            }
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(productos) { producto ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = producto.imageRes),
                            contentDescription = producto.nombre,
                            modifier = Modifier
                                .height(120.dp)
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(producto.nombre, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(producto.precio, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { cartCount++
                                      scope.launch {
                                          snackbarHostState.showSnackbar(
                                              message = "${producto.nombre} agregado al carrito!"
                                )
                            }
                      },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Comprar")
                        }
                    }
                }
            }
        }
    }
}
