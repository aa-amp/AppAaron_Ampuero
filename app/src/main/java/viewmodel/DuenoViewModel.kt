package viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DuenoViewModel : ViewModel() {
    private val _imagenUri = MutableStateFlow<Uri?>(null)
    val imagenUri: StateFlow<Uri?> = _imagenUri

    private val _correo = MutableStateFlow("dueno@duocuc.cl")
    val correo: StateFlow<String> = _correo

    private val _nombre = MutableStateFlow("Administrador")
    val nombre: StateFlow<String> = _nombre

    fun setImagen(uri: Uri?) {
        _imagenUri.value = uri
    }
}