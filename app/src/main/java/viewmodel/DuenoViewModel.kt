package viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import data.AppPreferencias
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DuenoViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = AppPreferencias(application)
    private val _imagenUri = MutableStateFlow<Uri?>(null)
    val imagenUri: StateFlow<Uri?> = _imagenUri

    private val _correo = MutableStateFlow("dueno@duocuc.cl")
    val correo: StateFlow<String> = _correo

    private val _rol = MutableStateFlow("Administrador")
    val rol: StateFlow<String> = _rol

    private val _nombre = MutableStateFlow("Aarón Ampuero")
    val nombre: StateFlow<String> = _nombre

    fun setImagen(uri: Uri?) {
        _imagenUri.value = uri
        viewModelScope.launch {
            prefs.guardarFotoPerfil(uri?.toString() ?: "")
        }
    }

    init {
        viewModelScope.launch {
            prefs.obtenerFotoPerfil().collect { savedUri ->
                _imagenUri.value = if (!savedUri.isNullOrEmpty()) {
                    Uri.parse(savedUri)
                } else {
                    null
                }
            }
        }
    }

    fun fueImagenCopiada(): Flow<Boolean> = prefs.fueImagenCopiada()

    fun marcarImagenCopiada() {
        viewModelScope.launch {
            prefs.marcarImagenCopiada()
        }
    }
}
