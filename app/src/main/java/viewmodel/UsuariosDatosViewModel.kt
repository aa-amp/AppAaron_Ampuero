package viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import model.Usuarios
import repository.UsuariosRepository

class UsuariosDatosViewModel : ViewModel(){
    private val repository = UsuariosRepository()

    private val _usuariosList = MutableStateFlow<List<Usuarios>>(emptyList())

    val usuariosList: StateFlow<List<Usuarios>> = _usuariosList

    init {
        fetchUsuarios()
    }

    private fun fetchUsuarios() {
    viewModelScope.launch {
        try {
            _usuariosList.value = repository.getUsuarios()
        } catch (e: Exception) {
            println("Error al obtener los usuarios: ${e.localizedMessage}")
        }
    }
}}

