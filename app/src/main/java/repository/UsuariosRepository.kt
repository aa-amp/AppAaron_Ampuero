package repository

import model.Usuarios
import remote.RetrofitInstance
import retrofit2.Response

class UsuariosRepository{
    suspend fun getUsuarios(): List<Usuarios>{
        return RetrofitInstance.api.getUsuarios()
    }

    suspend fun crearUsuario(usuario: Usuarios): Usuarios {
        return RetrofitInstance.api.crearUsuario(usuario)
    }
    suspend fun reemplazarUsuario(id: Int, usuario: Usuarios): Usuarios {
        return RetrofitInstance.api.reemplazarUsuario(id, usuario)
    }

    suspend fun eliminarUsuario(id: Int): Boolean {
        val resp: Response<Unit> = RetrofitInstance.api.eliminarUsuario(id)
        return resp.isSuccessful
    }

}
