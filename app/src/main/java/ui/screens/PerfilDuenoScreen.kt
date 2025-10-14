package ui.screens

import android.Manifest
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import navigation.Screen
import ui.componentes.ImagenCostumizable
import ui.components.CustomBottomBar
import ui.components.CustomTopBar
import ui.utils.copiarImagenADispositivo
import viewmodel.DuenoViewModel
import java.io.File

@Composable
fun PerfilDuenoScreen(
    viewModel: DuenoViewModel = viewModel(),
    onNavigate: (Screen) -> Unit
) {
    val imagenUri by viewModel.imagenUri.collectAsState()
    val correo by viewModel.correo.collectAsState()
    val nombre by viewModel.nombre.collectAsState()
    val rol by viewModel.rol.collectAsState()

    val context = LocalContext.current
    var tempUri by remember { mutableStateOf<Uri?>(null) }

    // Lanzador para galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.setImagen(it)
        }
    }

    // Lanzador para cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempUri != null) {
            viewModel.setImagen(tempUri)
        }
    }

    // Lanzador para pedir permiso de cámara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            tempUri?.let { uri -> cameraLauncher.launch(uri) }
        }
    }

    // Copiar imagen automáticamente una sola vez
    LaunchedEffect(Unit) {
        viewModel.fueImagenCopiada().collectLatest { yaCopiada ->
            if (!yaCopiada) {
                copiarImagenADispositivo(context)
                viewModel.marcarImagenCopiada()
            }
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                titulo = "Perfil",
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(correo, style = MaterialTheme.typography.bodyLarge)

            ImagenCostumizable(uri = imagenUri)

            Text(rol, style = MaterialTheme.typography.bodyMedium)
            Text(nombre, style = MaterialTheme.typography.titleMedium)

            Button(onClick = {
                galleryLauncher.launch("image/*")
            }) {
                Text("Subir desde galería")
            }

            Button(onClick = {
                val file = File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "dueno_${System.currentTimeMillis()}.jpg"
                )
                tempUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }) {
                Text("Tomar foto con cámara")
            }
        }
    }
}

