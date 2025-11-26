package ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.appaaron_ampuero.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import navigation.Screen
import ui.componentes.CustomTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigate: (Screen) -> Unit) {
    Scaffold(
        topBar = {
            CustomTopBar(
                titulo = "App.AaronAmpuero",
                colorFondo = Color.Gray
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido a la App de Bazar del Barrio!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Button(
                onClick = {onNavigate(Screen.Product) },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    "Ir a Productos",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface, thickness = 1.dp)
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo App",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface, thickness = 1.dp)

            Button(
                onClick = {onNavigate(Screen.Login)},
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Login", color = MaterialTheme.colorScheme.onPrimary)
            }

            Button(
                onClick = {onNavigate(Screen.Registro)},
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ){
                Text("Registro", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}