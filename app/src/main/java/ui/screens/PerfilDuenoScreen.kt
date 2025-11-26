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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import navigation.Screen
import ui.componentes.CustomBottomBar
import ui.componentes.CustomTopBar
import ui.componentes.ImagenCostumizable
import ui.utils.copiarImagenADispositivo
import viewmodel.DuenoViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilDuenoScreen(
    onNavigate: (Screen) -> Unit,
    viewModel: DuenoViewModel = viewModel()
) {
    val imagenUri by viewModel.imagenUri.collectAsState()
    val correoLocal by viewModel.correo.collectAsState()
    val nombreLocal by viewModel.nombre.collectAsState()
    val rol by viewModel.rol.collectAsState()
    val dueno by viewModel.dueno.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val context = LocalContext.current
    var tempUri by remember { mutableStateOf<Uri?>(null) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.setImagen(it) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempUri != null) {
            viewModel.setImagen(tempUri)
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            tempUri?.let { uri -> cameraLauncher.launch(uri) }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fueImagenCopiada().collectLatest { yaCopiada ->
            if (!yaCopiada) {
                copiarImagenADispositivo(context)
                viewModel.marcarImagenCopiada()
            }
        }
    }


    Scaffold(
        topBar = { CustomTopBar(titulo = "Perfil", colorFondo = MaterialTheme.colorScheme.primary) },
        bottomBar = { CustomBottomBar(onNavigate = onNavigate, showHome = true, showAdmin = true) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val duenoSnapshot = dueno

            duenoSnapshot?.let { d ->
                Text(
                    text = d.nombre.ifBlank { "Nombre no disponible" },
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = d.correo.ifBlank { "Correo no disponible" },
                    style = MaterialTheme.typography.bodyLarge
                )

                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)            } ?: run {
                Text(
                    text = nombreLocal.ifBlank { "Nombre no disponible" },
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = correoLocal.ifBlank { "Correo no disponible" },
                    style = MaterialTheme.typography.bodyLarge
                )
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)
            }

            ImagenCostumizable(uri = imagenUri)
            Text(rol.ifBlank { "Rol no disponible" }, style = MaterialTheme.typography.bodyMedium)

            Button(onClick = { galleryLauncher.launch("image/*") }) {
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

    mensaje?.let { msg ->
        LaunchedEffect(msg) {
            snackbarHostState.showSnackbar(msg)
            viewModel.limpiarMensaje()
        }
    }
}