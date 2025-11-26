package viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import remote.RetrofitInstance
import repository.CarritoRepository
import remote.dto.PostDto
import model.CarritoModel
import model.Producto

class CarritoViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = CarritoRepository(RetrofitInstance.api)

    private val _carrito = MutableStateFlow<List<CarritoModel>>(emptyList())
    val carrito: StateFlow<List<CarritoModel>> = _carrito

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private fun postToCarrito(post: PostDto) = CarritoModel(
        id = post.id?.toIntOrNull() ?: 0,
        productoId = post.userId,
        nombre = post.title,
        cantidad = 1,
        precio = post.body.toIntOrNull() ?: 0
    )

    fun getCarritoFromPosts() = viewModelScope.launch {
        _isLoading.value = true
        _errorMessage.value = null
        try {
            val resp = repo.getCarrito()
            if (resp.isSuccessful) _carrito.value = resp.body().orEmpty().map { postToCarrito(it) }
            else _errorMessage.value = "Error ${resp.code()}"
        } catch (e: Exception) {
            _errorMessage.value = e.localizedMessage ?: "Error de conexión"
            Log.e("CARRITO", "getCarritoFromPosts", e)
        } finally {
            _isLoading.value = false
        }
    }

    fun deleteCarritoItemViaPosts(id: Int) = viewModelScope.launch {
        _isLoading.value = true
        _errorMessage.value = null
        try {
            val resp = repo.deletePost(id)
            if (resp.isSuccessful) _carrito.value = _carrito.value.filter { it.id != id }
            else _errorMessage.value = "Error ${resp.code()}"
        } catch (e: Exception) {
            _errorMessage.value = e.localizedMessage ?: "Error de conexión"
            Log.e("CARRITO", "deleteCarritoItemViaPosts", e)
        } finally {
            _isLoading.value = false
        }
    }

    fun productoToCarritoModel(producto: Producto) = CarritoModel(
        id = 0,
        productoId = 0,
        nombre = producto.nombre,
        cantidad = 1,
        precio = producto.precio.replace("[^0-9]".toRegex(), "").toIntOrNull() ?: 0
    )

    suspend fun fetchThreePostsAsProductos(): List<Producto> = try {
        val resp = repo.getCarrito()
        if (resp.isSuccessful) resp.body().orEmpty().take(3).map {
            Producto(nombre = it.title, imagenRes = 0, precio = "$${it.body.toIntOrNull() ?: 0}")
        } else emptyList()
    } catch (e: Exception) {
        emptyList()
    }

    fun clearCarrito() = viewModelScope.launch {
        _isLoading.value = true
        _errorMessage.value = null
        try {
            _carrito.value = emptyList()
            Log.d("CARRITO", "clearCarrito: carrito local vaciado")
        } catch (e: Exception) {
            _errorMessage.value = e.localizedMessage ?: "Error al vaciar carrito local"
            Log.e("CARRITO", "clearCarrito", e)
        } finally {
            _isLoading.value = false
        }
    }

    fun getCarrito() = getCarritoFromPosts()
    fun deleteCarritoItem(id: Int) = deleteCarritoItemViaPosts(id)
}