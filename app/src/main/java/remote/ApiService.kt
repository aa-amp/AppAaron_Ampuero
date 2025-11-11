package remote
import model.Usuarios
import retrofit2.http.GET

interface ApiService {

    @GET("users")
    suspend fun getUsuarios(): List<Usuarios>
}

