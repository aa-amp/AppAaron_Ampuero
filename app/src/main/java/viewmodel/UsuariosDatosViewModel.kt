package viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import model.Usuarios
import repository.UsuariosRepository

class UsuariosDatosViewModel(
    private val repository: UsuariosRepository = UsuariosRepository()
) : ViewModel() {

    private val _usuariosList = MutableStateFlow<List<Usuarios>>(emptyList())
    val usuariosList: StateFlow<List<Usuarios>> = _usuariosList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchUsuarios()
    }

    fun fetchUsuarios() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _usuariosList.value = repository.getUsuarios()
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun reemplazarUsuario(id: Int, usuario: Usuarios, onResult: (Boolean, String?) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val actualizado = repository.reemplazarUsuario(id, usuario)
                _usuariosList.value = _usuariosList.value.map { if (it.id == id) actualizado else it }
                onResult(true, null)
            } catch (e: Exception) {
                val msg = e.localizedMessage ?: "Error al reemplazar usuario"
                _errorMessage.value = msg
                onResult(false, msg)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun eliminarUsuario(id: Int, onResult: (Boolean, String?) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val ok = repository.eliminarUsuario(id)
                if (ok) {
                    _usuariosList.value = _usuariosList.value.filter { it.id != id }
                    onResult(true, null)
                } else {
                    val msg = "El servidor respondió con error al eliminar"
                    _errorMessage.value = msg
                    onResult(false, msg)
                }
            } catch (e: Exception) {
                val msg = e.localizedMessage ?: "Error al eliminar usuario"
                _errorMessage.value = msg
                onResult(false, msg)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun crearUsuario(usuario: Usuarios, onResult: (Boolean, String?) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val creado = repository.crearUsuario(usuario) // usar repository (no repo)
                _usuariosList.value = _usuariosList.value + creado
                onResult(true, null)
            } catch (e: Exception) {
                val msg = e.localizedMessage ?: "Error al crear usuario"
                _errorMessage.value = msg
                onResult(false, msg)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun insertLocal(usuario: Usuarios) {
        _usuariosList.value = _usuariosList.value + usuario
    }
}
