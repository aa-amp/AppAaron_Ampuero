package repository
//esta clase estaba para manejar los datos locales, ya no la ocupo porque saco desde la api.
//No la borrare por si la llegase a necesitar.
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import model.Usuarios

object DatosLocales {

    private val mutex = Mutex()

    private val _usuarios = MutableStateFlow<List<Usuarios>>(seed())
    val usuarios: StateFlow<List<Usuarios>> = _usuarios

    suspend fun getAll(): List<Usuarios> = _usuarios.value

    suspend fun findById(id: String): Usuarios? = _usuarios.value.firstOrNull { it.id == id }

    suspend fun insert(usuario: Usuarios): Usuarios = mutex.withLock {
        val maxIdNum = _usuarios.value.mapNotNull { it.id?.toIntOrNull() }.maxOrNull() ?: 0
        val nextId = (maxIdNum + 1).toString()
        val nuevo = usuario.copy(id = nextId)
        _usuarios.value = _usuarios.value + nuevo
        nuevo
    }

    suspend fun replace(id: String, usuario: Usuarios): Usuarios = mutex.withLock {
        val actualizado = usuario.copy(id = id)
        _usuarios.value = _usuarios.value.map { if (it.id == id) actualizado else it }
        actualizado
    }

    suspend fun delete(id: String): Boolean = mutex.withLock {
        val antes = _usuarios.value.size
        _usuarios.value = _usuarios.value.filterNot { it.id == id }
        _usuarios.value.size < antes
    }

    suspend fun clear() = mutex.withLock {
        _usuarios.value = emptyList()
    }

    private fun seed(): List<Usuarios> = listOf(
        Usuarios(id = "1", name = "Aarón Ampuero", username = "aaron", email = "aaron@duocuc.cl"),
        Usuarios(id = "2", name = "Usuario Demo", username = "usuario", email = "usuario@duocuc.cl"),
        Usuarios(id = "3", name = "Diego Cares", username = "dcares", email = "d.cares@duocuc.cl")
    )
}