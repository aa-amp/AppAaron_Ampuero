package viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import data.AppPreferencias
import navigation.Screen
import ui.utils.UsuarioErrores
import ui.utils.UsuarioUiState
import kotlinx.coroutines.launch
import repository.UsuariosRepository
import model.Usuarios

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = AppPreferencias(application)

    private val _estado = MutableStateFlow(UsuarioUiState())
    private val correoDueno = "dueno@duocuc.cl"
    private val claveDueno = "123456"

    private val usuariosRepo = repository.UsuariosRepository()

    val estado: StateFlow<UsuarioUiState> = _estado

    val sesionActiva: Flow<String?> = prefs.obtenerSesion()

    fun onNombreChange(valor: String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    fun onDireccionChange(valor: String) {
        _estado.update { it.copy(direccion = valor, errores = it.errores.copy(direccion = null)) }
    }

    fun onAceptarTerminosChange(valor: Boolean) {
        _estado.update { it.copy(aceptaTerminos = valor) }
    }

    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) "El nombre es requerido" else null,
            correo = if (!estadoActual.correo.contains("@duocuc.cl")) "Correo inválido, debe llevar @duocuc.cl" else null,
            clave = if (estadoActual.clave.length < 6) "La clave debe tener un largo de 6 carácteres" else null,
            direccion = if (estadoActual.direccion.isBlank()) "La dirección es requerida" else null
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.clave,
            errores.direccion
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }

    fun validarLogin(): Boolean {
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            correo = if (!estadoActual.correo.contains("@duocuc.cl")) "Correo inválido, debe llevar @duocuc.cl" else null,
            clave = if (estadoActual.clave.length < 6) "La clave debe tener un largo de 6 carácteres" else null
        )

        val hayErrores = listOfNotNull(
            errores.correo,
            errores.clave
        ).isNotEmpty()

        _estado.update { it.copy(errores = it.errores.copy(correo = errores.correo, clave = errores.clave)) }

        return !hayErrores
    }

    fun loginTipoUsuario(): Screen? {
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            correo = if (!estadoActual.correo.contains("@duocuc.cl")) "Correo inválido, debe llevar @duocuc.cl" else null,
            clave = if (estadoActual.clave.length < 6) "Clave inválida, debe tener un largo de 6 carácteres" else null
        )

        val hayErrores = listOfNotNull(
            errores.correo,
            errores.clave
        ).isNotEmpty()

        _estado.update { it.copy(errores = it.errores.copy(correo = errores.correo, clave = errores.clave)) }

        if (hayErrores) return null

        val destino = if (
            estadoActual.correo == correoDueno &&
            estadoActual.clave == claveDueno
        ) {
            Screen.Dueno
        } else {
            Screen.Product
        }

        viewModelScope.launch {
            prefs.guardarSesion(estadoActual.correo)
        }
        return destino
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            prefs.guardarSesion("")
        }
    }

    fun registrarUsuario(
        onResult: (creado: Usuarios?, mensaje: String?) -> Unit = { _, _ -> },
        notifyUsuariosList: (() -> Unit)? = null
    ) {
        val s = _estado.value
        val nuevo = Usuarios(
            id = 0,
            name = s.nombre,
            username = s.nombre.replace("\\s+".toRegex(), "").lowercase(),
            email = s.correo
        )

        viewModelScope.launch {
            try {
                val creado = usuariosRepo.crearUsuario(nuevo)
                onResult(creado, null)
                notifyUsuariosList?.invoke()
            } catch (e: Exception) {
                onResult(null, e.localizedMessage ?: "Error al crear usuario")
            }
        }
    }

}