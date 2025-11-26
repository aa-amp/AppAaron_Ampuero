package viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import data.AppPreferencias
import model.DuenoModel

class DuenoViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val prefs = AppPreferencias(application)

    private val _imagenUri = MutableStateFlow<Uri?>(null)
    val imagenUri: StateFlow<Uri?> = _imagenUri

    private val _correo = MutableStateFlow("dueno@duocuc.cl")
    val correo: StateFlow<String> = _correo

    private val _rol = MutableStateFlow("Administrador")
    val rol: StateFlow<String> = _rol

    private val _nombre = MutableStateFlow("Aarón Ampuero")
    val nombre: StateFlow<String> = _nombre

    private val _dueno = MutableStateFlow<DuenoModel?>(null)
    val dueno: StateFlow<DuenoModel?> = _dueno

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            prefs.obtenerFotoPerfil().collect { savedUri ->
                _imagenUri.value = if (!savedUri.isNullOrEmpty()) Uri.parse(savedUri) else null
            }
        }
    }

    fun setImagen(uri: Uri?) {
        _imagenUri.value = uri
        viewModelScope.launch { prefs.guardarFotoPerfil(uri?.toString() ?: "") }
    }

    fun fueImagenCopiada(): Flow<Boolean> = prefs.fueImagenCopiada()

    fun marcarImagenCopiada() {
        viewModelScope.launch { prefs.marcarImagenCopiada() }
    }

    fun limpiarMensaje() { _mensaje.value = null }
}