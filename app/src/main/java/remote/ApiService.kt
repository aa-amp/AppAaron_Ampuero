package remote
import model.Usuarios
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Body
interface ApiService {

    @GET("users")
    suspend fun getUsuarios(): List<Usuarios>

    @POST("users")
    suspend fun crearUsuario(@Body usuario: Usuarios): Usuarios

    @PUT("users/{id}")
    suspend fun reemplazarUsuario(@Path("id") id: Int, @Body usuario: Usuarios): Usuarios

    @DELETE("users/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Int): Response<Unit>

}

