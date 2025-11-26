package ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import viewmodel.ProductoViewModel
import com.example.appaaron_ampuero.R
import model.ProductoModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearProductoScreen(viewModel: ProductoViewModel, onDone: () -> Unit) {
    var idText by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precioText by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var pickedUri by remember { mutableStateOf<String?>(null) }

    val pickLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { pickedUri = it.toString() }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = idText,
            onValueChange = { idText = it.filter { ch -> ch.isDigit() } },
            label = { Text("ID (opcional)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = precioText,
            onValueChange = { precioText = it.filter { ch -> ch.isDigit() } },
            label = { Text("Precio") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text("Imagen (elige una o pega URL). Si ambas, se usa la imagen elegida.")
        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(onClick = { pickLauncher.launch("image/*") }) { Text("Elegir imagen") }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("URL imagen") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val displayImage = when {
            pickedUri != null -> pickedUri!!
            imageUrl.isNotBlank() -> imageUrl
            else -> null
        }

        displayImage?.let {
            AsyncImage(
                model = it,
                contentDescription = "Preview",
                placeholder = painterResource(id = R.drawable.product_placeholder),
                error = painterResource(id = R.drawable.product_placeholder),
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val id = idText.toIntOrNull() ?: 0
            val precio = precioText.toIntOrNull() ?: 0
            val imageToSend = pickedUri ?: imageUrl.ifBlank { null }
            val producto = ProductoModel(
                id = id,
                nombre = nombre,
                descripcion = descripcion,
                precio = precio,
                stock = 1,
                imagenRes = 0,
                imageName = null,
                imageUrl = imageToSend,
                categoria = if (imageToSend?.startsWith("content://") == true) "LOCAL" else "API"
            )
            viewModel.createProducto(producto)
            idText = ""
            nombre = ""
            descripcion = ""
            precioText = ""
            imageUrl = ""
            pickedUri = null
            onDone()
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Crear producto")
        }
    }
}