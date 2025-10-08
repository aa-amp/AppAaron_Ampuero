package data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "app_preferences")

class AppPreferences(private val context: Context) {

    private val MODO_ESPECIAL = booleanPreferencesKey("modo_especial")
    private val USUARIO_ID = stringPreferencesKey("usuario_id")
    private val CARRITO = stringSetPreferencesKey("carrito")

    suspend fun guardarModoEspecial(valor: Boolean) {
        context.dataStore.edit { it[MODO_ESPECIAL] = valor }
    }
    fun obtenerModoEspecial(): Flow<Boolean?> =
        context.dataStore.data.map { it[MODO_ESPECIAL] }

    suspend fun guardarSesion(usuarioId: String) {
        context.dataStore.edit { it[USUARIO_ID] = usuarioId }
    }
    fun obtenerSesion(): Flow<String?> =
        context.dataStore.data.map { it[USUARIO_ID] }

    suspend fun guardarCarrito(productos: Set<String>) {
        context.dataStore.edit { it[CARRITO] = productos }
    }
    fun obtenerCarrito(): Flow<Set<String>> =
        context.dataStore.data.map { it[CARRITO] ?: emptySet() }
}