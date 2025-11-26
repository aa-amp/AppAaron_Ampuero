package data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "app_preferencias")

class AppPreferencias(private val context: Context) {

    private val USUARIO_ID = stringPreferencesKey("usuario_id")
    private val CARRITO = stringSetPreferencesKey("carrito")
    private val FOTO_URI = stringPreferencesKey("foto_uri")
    private val IMAGEN_COPIADA = booleanPreferencesKey("imagen_copiada")

    suspend fun guardarSesion(usuarioId: String) {
        context.dataStore.edit { it[USUARIO_ID] = usuarioId }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { it.remove(USUARIO_ID) }
    }

    suspend fun guardarCarrito(productos: Set<String>) {
        context.dataStore.edit { it[CARRITO] = productos }
    }

    fun obtenerCarrito(): Flow<Set<String>> =
        context.dataStore.data.map { it[CARRITO] ?: emptySet() }

    suspend fun guardarFotoPerfil(uri: String) {
        context.dataStore.edit { it[FOTO_URI] = uri }
    }

    fun obtenerFotoPerfil(): Flow<String?> =
        context.dataStore.data.map { it[FOTO_URI] }

    suspend fun marcarImagenCopiada() {
        context.dataStore.edit { it[IMAGEN_COPIADA] = true }
    }

    fun fueImagenCopiada(): Flow<Boolean> =
        context.dataStore.data.map { it[IMAGEN_COPIADA] ?: false }
}