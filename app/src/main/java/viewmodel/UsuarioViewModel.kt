package viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.Usuarios
import navigation.Screen
import repository.UsuariosRepository
import ui.utils.UsuarioErrores
import ui.utils.UsuarioUiState
import data.AppPreferencias

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val usuariosRepo: UsuariosRepository = UsuariosRepository()

    private var prefs: AppPreferencias? = null

    private val _estado = MutableStateFlow(UsuarioUiState())
    val estado: StateFlow<UsuarioUiState> = _estado

    private val correoDueno = "dueno@duocuc.cl"
    private val claveDueno = "123456"

    private val usuariosLocal = mutableListOf<Usuarios>()

    fun setPreferencias(preferencias: AppPreferencias) {
        this.prefs = preferencias
    }

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
        val s = _estado.value
        val errores = UsuarioErrores(
            nombre = if (s.nombre.isBlank()) "El nombre es requerido" else null,
            correo = if (!s.correo.contains("@duocuc.cl")) "Correo inválido, debe llevar @duocuc.cl" else null,
            clave = if (s.clave.length < 6) "La clave debe tener un largo de 6 carácteres" else null,
            direccion = if (s.direccion.isBlank()) "La dirección es requerida" else null
        )
        val hayErrores = listOfNotNull(errores.nombre, errores.correo, errores.clave, errores.direccion).isNotEmpty()
        _estado.update { it.copy(errores = errores) }
        return !hayErrores
    }

    fun validarLogin(): Boolean {
        val s = _estado.value
        val errores = UsuarioErrores(
            correo = if (!s.correo.contains("@duocuc.cl")) "Correo inválido, debe llevar @duocuc.cl" else null,
            clave = if (s.clave.length < 6) "La clave debe tener un largo de 6 carácteres" else null
        )
        val hayErrores = listOfNotNull(errores.correo, errores.clave).isNotEmpty()
        _estado.update { it.copy(errores = it.errores.copy(correo = errores.correo, clave = errores.clave)) }
        return !hayErrores
    }

    fun loginTipoUsuario(): Screen? {
        val s = _estado.value
        val errores = UsuarioErrores(
            correo = if (!s.correo.contains("@duocuc.cl")) "Correo inválido, debe llevar @duocuc.cl" else null,
            clave = if (s.clave.length < 6) "Clave inválida, debe tener un largo de 6 carácteres" else null
        )
        val hayErrores = listOfNotNull(errores.correo, errores.clave).isNotEmpty()
        _estado.update { it.copy(errores = it.errores.copy(correo = errores.correo, clave = errores.clave)) }
        if (hayErrores) return null
        val destino = if (s.correo == correoDueno && s.clave == claveDueno) Screen.Dueno else Screen.Product
        prefs?.let { preferencias ->
            viewModelScope.launch { preferencias.guardarSesion(s.correo) }
        }
        return destino
    }

    fun registrarUsuarioLocal(
        nuevo: Usuarios,
        onResult: (creado: Usuarios) -> Unit = {}
    ) {
        val nextInt = (usuariosLocal.maxOfOrNull { it.id?.toIntOrNull() ?: 0 } ?: 0) + 1
        val nextId = nextInt.toString()
        val creado = nuevo.copy(id = nextId)
        usuariosLocal.add(creado)

        _estado.update {
            it.copy(
                nombre = creado.name,
                correo = creado.email,
                direccion = it.direccion,
                clave = it.clave,
                aceptaTerminos = it.aceptaTerminos,
                errores = it.errores
            )
        }

        onResult(creado)
    }
}