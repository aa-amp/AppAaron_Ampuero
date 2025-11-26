package repository

import android.util.Log
import model.Usuarios
import remote.ApiService
import remote.RetrofitInstance
import java.io.IOException
import retrofit2.HttpException
import retrofit2.Response

class UsuariosRepository(private val api: ApiService = RetrofitInstance.api) {

    private suspend fun <T> callApi(block: suspend () -> Response<T>): T {
        try {
            val resp = block()
            if (resp.isSuccessful) {
                return resp.body() ?: throw IOException("Respuesta vacía del servidor")
            } else {
                val err = try { resp.errorBody()?.string() } catch (_: Exception) { null }
                Log.e("USU", "API call failed code=${resp.code()} body=$err")
                throw IOException("HTTP ${resp.code()} - ${err ?: "Error del servidor"}")
            }
        } catch (e: HttpException) {
            val code = e.code()
            val body = try { e.response()?.errorBody()?.string() } catch (_: Exception) { null }
            Log.e("USU", "HttpException code=$code body=$body", e)
            throw IOException("HTTP $code - ${body ?: e.message()}", e)
        } catch (e: Exception) {
            Log.e("USU", "Network error", e)
            throw IOException(e.localizedMessage ?: "Error de red", e)
        }
    }

    suspend fun fetchAll(): List<Usuarios> {
        Log.d("USU", "fetchAll -> solicitando lista al servidor")
        return callApi { api.getUsers() }
    }

    suspend fun createUser(user: Usuarios): Usuarios {
        Log.d("USU", "createUser -> payload=${user}")
        return callApi { api.postUser(user) }
    }

    suspend fun putUserInternal(id: String, usuario: Usuarios): Usuarios {
        Log.d("USU", "putUserInternal: id=$id payload=${usuario}")
        try {
            return callApi { api.putUser(id, usuario) }
        } catch (e: IOException) {
            if (e.message?.contains("HTTP 404") == true) {
                Log.e("USU", "putUserInternal 404 Not Found", e)
                throw IOException("HTTP 404 - Not Found")
            }
            throw e
        }
    }

    suspend fun deleteUser(id: String): Boolean {
        Log.d("USU", "deleteUser: id=$id")
        try {
            val resp = api.deleteUser(id)
            if (resp.isSuccessful) {
                Log.d("USU", "deleteUser OK id=$id code=${resp.code()}")
                return true
            } else {
                val err = try { resp.errorBody()?.string() } catch (_: Exception) { null }
                Log.e("USU", "deleteUser failed id=$id code=${resp.code()} body=$err")
                throw IOException("HTTP ${resp.code()} - ${err ?: "Error al eliminar"}")
            }
        } catch (e: HttpException) {
            val code = e.code()
            val body = try { e.response()?.errorBody()?.string() } catch (_: Exception) { null }
            Log.e("USU", "deleteUser HttpException code=$code body=$body", e)
            throw IOException("HTTP $code - ${body ?: e.message()}", e)
        } catch (e: Exception) {
            Log.e("USU", "deleteUser error", e)
            throw IOException(e.localizedMessage ?: "Error al eliminar usuario", e)
        }
    }
}