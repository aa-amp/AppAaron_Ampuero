package repository

import model.Usuarios
import remote.RetrofitInstance

class UsuariosRepository{
    suspend fun getUsuarios(): List<Usuarios>{
        return RetrofitInstance.api.getUsuarios()
    }
}
