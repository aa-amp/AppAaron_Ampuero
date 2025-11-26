package remote

import model.Usuarios
import remote.dto.PostDto
import model.DuenoModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("users")
    suspend fun getUsers(): Response<List<Usuarios>>

    @POST("users")
    suspend fun postUser(@Body user: Usuarios): Response<Usuarios>

    @PUT("users/{id}")
    suspend fun putUser(@Path("id") id: String, @Body user: Usuarios): Response<Usuarios>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String): Response<Unit>

    @GET("posts")
    suspend fun getPosts(): Response<List<PostDto>>

    @GET("posts/{id}")
    suspend fun getPost(@Path("id") id: Int): Response<PostDto>

    @POST("posts")
    suspend fun postPost(@Body post: PostDto): Response<PostDto>

    @PUT("posts/{id}")
    suspend fun putPost(@Path("id") id: Int, @Body post: PostDto): Response<PostDto>

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Int): Response<Unit>

    @GET("posts")
    suspend fun getCarrito(): Response<List<PostDto>>

    @DELETE("posts/{id}")
    suspend fun deleteCarritoItem(@Path("id") id: Int): Response<Unit>
}