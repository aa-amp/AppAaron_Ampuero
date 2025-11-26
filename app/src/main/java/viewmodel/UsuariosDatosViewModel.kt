package viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import model.Usuarios
import repository.UsuariosRepository

class UsuariosDatosViewModel(application: Application) : AndroidViewModel(application) {

    private val usuariosRepo: UsuariosRepository = UsuariosRepository()

    private val _usuarios = MutableStateFlow<List<Usuarios>>(emptyList())
    val usuarios: StateFlow<List<Usuarios>> = _usuarios

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadUsuarios() = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null
        try {
            val list = usuariosRepo.fetchAll()
            _usuarios.value = list
            Log.d("USU", "loadUsuarios OK count=${list.size}")
        } catch (e: Exception) {
            _error.value = e.localizedMessage ?: "Error al cargar usuarios"
            Log.e("USU", "loadUsuarios error", e)
        } finally {
            _isLoading.value = false
        }
    }

    fun crearUsuario(usuario: Usuarios, onResult: (Usuarios?) -> Unit = {}) = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null
        try {
            val creado = usuariosRepo.createUser(usuario)
            loadUsuarios()
            onResult(creado)
        } catch (e: Exception) {
            _error.value = e.localizedMessage ?: "Error al crear usuario"
            Log.e("USU", "crearUsuario error", e)
            onResult(null)
        } finally {
            _isLoading.value = false
        }
    }

    fun eliminarUsuario(id: String, onResult: (Boolean) -> Unit = {}) = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null
        try {
            val ok = usuariosRepo.deleteUser(id)
            if (ok) loadUsuarios()
            onResult(ok)
        } catch (e: Exception) {
            _error.value = e.localizedMessage ?: "Error al eliminar usuario"
            Log.e("USU", "eliminarUsuario error", e)
            onResult(false)
        } finally {
            _isLoading.value = false
        }
    }

    fun actualizarUsuario(id: String, usuario: Usuarios, onResult: (Usuarios?) -> Unit = {}) = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null
        try {
            val actualizado = usuariosRepo.putUserInternal(id, usuario)
            loadUsuarios()
            onResult(actualizado)
        } catch (e: Exception) {
            _error.value = e.localizedMessage ?: "Error al actualizar usuario"
            Log.e("USU", "actualizarUsuario error", e)
            onResult(null)
        } finally {
            _isLoading.value = false
        }
    }
}