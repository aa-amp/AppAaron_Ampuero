// repository/ProductoRepository.kt
package repository

import android.content.Context
import com.example.appaaron_ampuero.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import remote.ApiService
import remote.dto.PostDto
import model.ProductoModel
import retrofit2.Response
import java.io.IOException

class ProductoRepository(
    private val api: ApiService,
    private val context: Context
) {

    @Throws(IOException::class)
    suspend fun getProductos(): List<ProductoModel> {
        val resp: Response<List<PostDto>> = api.getPosts()
        if (!resp.isSuccessful) {
            val err = resp.errorBody()?.string() ?: "HTTP ${resp.code()}"
            throw IOException("HTTP ${resp.code()} - $err")
        }
        val posts = resp.body() ?: emptyList()
        return posts.map { post ->
            val idInt = post.id?.toIntOrNull() ?: 0
            val imagenRes = resolveDrawableId(post.imageName)
            ProductoModel(
                id = idInt,
                nombre = post.title,
                descripcion = post.body,
                precio = post.body.toIntOrNull() ?: 1000,
                stock = 10,
                imagenRes = imagenRes,
                imageName = post.imageName,
                imageUrl = post.imageUrl,
                categoria = "API"
            )
        }
    }

    /* No puse el boton de obtener producto por id asi que la dejare comentada.
    @Throws(IOException::class)
    suspend fun getProducto(id: Int): ProductoModel {
        val resp: Response<PostDto> = api.getPost(id)
        if (!resp.isSuccessful) {
            val err = resp.errorBody()?.string() ?: "HTTP ${resp.code()}"
            throw IOException("HTTP ${resp.code()} - $err")
        }
        val post = resp.body() ?: throw IOException("Respuesta vacía del servidor")
        val idInt = post.id?.toIntOrNull() ?: 0
        val imagenRes = resolveDrawableId(post.imageName)
        return ProductoModel(
            id = idInt,
            nombre = post.title,
            descripcion = post.body,
            precio = post.body.toIntOrNull() ?: 1000,
            stock = 10,
            imagenRes = imagenRes,
            imageName = post.imageName,
            imageUrl = post.imageUrl,
            categoria = "API"
        )
    }*/

    @Throws(IOException::class)
    suspend fun createProducto(postDto: PostDto): ProductoModel {
        val resp: Response<PostDto> = api.postPost(postDto)
        if (!resp.isSuccessful) {
            val err = resp.errorBody()?.string() ?: "HTTP ${resp.code()}"
            throw IOException("HTTP ${resp.code()} - $err")
        }
        val created = resp.body() ?: throw IOException("Respuesta vacía al crear producto")
        val idInt = created.id?.toIntOrNull() ?: 0
        val imagenRes = resolveDrawableId(created.imageName)
        return ProductoModel(
            id = idInt,
            nombre = created.title,
            descripcion = created.body,
            precio = created.body.toIntOrNull() ?: 1000,
            stock = 10,
            imagenRes = imagenRes,
            imageName = created.imageName,
            imageUrl = created.imageUrl,
            categoria = "API"
        )
    }

    @Throws(IOException::class)
    suspend fun updateProducto(id: Int, postDto: PostDto): Response<ProductoModel> {
        val resp: Response<PostDto> = api.putPost(id, postDto)
        return if (resp.isSuccessful) {
            val body = resp.body() ?: throw IOException("Respuesta vacía al actualizar")
            val idInt = body.id?.toIntOrNull() ?: 0
            val mapped = ProductoModel(
                id = idInt,
                nombre = body.title,
                descripcion = body.body,
                precio = body.body.toIntOrNull() ?: 1000,
                stock = 10,
                imagenRes = resolveDrawableId(body.imageName),
                imageName = body.imageName,
                imageUrl = body.imageUrl,
                categoria = "API"
            )
            Response.success(mapped)
        } else {
            val err = resp.errorBody()?.string() ?: "Error ${resp.code()}"
            Response.error(resp.code(), err.toByteArray().toResponseBody("application/json".toMediaTypeOrNull()))
        }
    }

    suspend fun deleteProducto(id: Int): Response<Unit> {
        return api.deletePost(id)
    }

    private fun resolveDrawableId(imageName: String?): Int {
        if (imageName.isNullOrBlank()) return R.drawable.product_placeholder
        val resId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
        return if (resId != 0) resId else R.drawable.product_placeholder
    }
}