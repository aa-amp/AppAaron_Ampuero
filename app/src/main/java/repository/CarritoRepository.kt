package repository

import remote.ApiService
import remote.dto.PostDto
import retrofit2.Response

class CarritoRepository(private val api: ApiService) {

    suspend fun getCarrito(): Response<List<PostDto>> = api.getCarrito()

    suspend fun deletePost(id: Int): Response<Unit> = api.deleteCarritoItem(id)
}