package viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appaaron_ampuero.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import model.Producto
import model.ProductoModel
import remote.RetrofitInstance
import remote.dto.PostDto
import repository.ProductoRepository
import data.AppPreferencias
import retrofit2.Response

class ProductoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductoRepository =
        ProductoRepository(RetrofitInstance.api, application.applicationContext)
    private val prefs = AppPreferencias(application)
    private var tempIdCounter = -1

    private val _productosLocal = MutableStateFlow(
        listOf(
            Producto("Fideos Rigati 400g", R.drawable.product1, "$1.000"),
            Producto("Alfi 45g", R.drawable.product2, "$700"),
            Producto("Cheetos 40g", R.drawable.product4, "$1.000"),
            Producto("Chocman 33g", R.drawable.product5, "$300"),
            Producto("Lays 110g", R.drawable.product6, "$2.000"),
            Producto("Golpe 27g", R.drawable.product7, "$500"),
            Producto("Coca Cola Zero 1L", R.drawable.product8, "$1.100"),
            Producto("Tableta BonoBon 270g", R.drawable.product3, "$3.500")
        )
    )
    val productosLocal: StateFlow<List<Producto>> = _productosLocal

    private val _productosApi = MutableStateFlow<List<ProductoModel>>(emptyList())
    val productosApi: StateFlow<List<ProductoModel>> = _productosApi

    private val _carrito = MutableStateFlow<List<Producto>>(emptyList())
    val carrito: StateFlow<List<Producto>> = _carrito

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        viewModelScope.launch {
            try {
                val ids = prefs.obtenerCarrito().first().map { it.trim().lowercase() }.toSet()
                val productosGuardados = _productosLocal.value.filter { normalizeName(it.nombre) in ids }
                _carrito.value = productosGuardados
            } catch (e: Exception) {
                Log.e("PROD", "error leyendo prefs iniciales", e)
            }
            getProductosFromPosts()
        }
    }

    fun getProductos() { getProductosFromPosts() }

    fun getProductosFromPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val lista = repository.getProductos()
                _productosApi.value = lista.distinctBy { it.id }
                Log.d("PROD", "productos cargados desde posts: ${_productosApi.value.size}")
            } catch (e: Exception) {
                Log.e("PROD", "getProductos error", e)
                _errorMessage.value = e.localizedMessage ?: "Error de conexión"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createProducto(producto: ProductoModel) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val postDto = producto.toPostDto()
                try {
                    val creado: ProductoModel = repository.createProducto(postDto)
                    if (creado.id > 0) {
                        val current = _productosApi.value.toMutableList()
                        val tempIndex = current.indexOfFirst {
                            it.id <= 0 && it.nombre == producto.nombre && it.descripcion == producto.descripcion
                        }
                        if (tempIndex >= 0) {
                            current[tempIndex] = creado
                        } else {
                            if (current.none { it.id == creado.id }) current.add(creado)
                        }
                        _productosApi.value = current
                        Log.d("PROD", "producto creado en API id=${creado.id}")
                    } else {
                        Log.w("PROD", "API devolvió id inválido; forzando recarga")
                        getProductosFromPosts()
                    }
                } catch (e: Exception) {
                    val tempId = tempIdCounter--
                    val local = producto.copy(id = tempId, categoria = "LOCAL")
                    val current = _productosApi.value.toMutableList()
                    current.add(local)
                    _productosApi.value = current
                    Log.e("PROD", "createProducto POST falló, agregado local id=${local.id}", e)
                    _errorMessage.value = e.localizedMessage ?: "Error al crear producto"
                }
            } catch (e: Exception) {
                Log.e("PROD", "createProducto error", e)
                _errorMessage.value = e.localizedMessage ?: "Error al crear producto"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun crearProductoDesdeParametros(
        nombre: String,
        descripcion: String,
        precio: Int,
        imageUrl: String?,
        imageName: String? = null
    ) {
        val producto = ProductoModel(
            id = 0,
            nombre = nombre,
            descripcion = descripcion,
            precio = precio,
            stock = 1,
            imagenRes = 0,
            imageName = imageName,
            imageUrl = imageUrl,
            categoria = if (imageUrl?.startsWith("content://") == true) "LOCAL" else "API"
        )
        createProducto(producto)
    }

    fun updateProducto(id: Int, producto: ProductoModel) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                if (id > 0) {
                    val postDto = producto.toPostDto()
                    val response: Response<ProductoModel> = repository.updateProducto(id, postDto)
                    if (response.isSuccessful) {
                        val actualizado = response.body()
                        if (actualizado != null) {
                            _productosApi.value = _productosApi.value.map { if (it.id == id) actualizado else it }
                        }
                    } else {
                        _errorMessage.value = "Error al actualizar producto (${response.code()})"
                        Log.e("PROD", "updateProducto error code=${response.code()}")
                    }
                } else {
                    _productosApi.value = _productosApi.value.map { if (it.id == id) producto else it }
                    Log.d("PROD", "producto local actualizado id=$id")
                }
            } catch (e: Exception) {
                Log.e("PROD", "updateProducto exception", e)
                _errorMessage.value = e.localizedMessage ?: "Error de conexión"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteProducto(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                if (id > 0) {
                    val response = repository.deleteProducto(id)
                    if (response.isSuccessful) {
                        _productosApi.value = _productosApi.value.filter { it.id != id }
                        Log.d("PROD", "producto eliminado en API id=$id")
                    } else {
                        _errorMessage.value = "Error al eliminar producto (${response.code()})"
                        Log.e("PROD", "deleteProducto error code=${response.code()}")
                    }
                } else {
                    _productosApi.value = _productosApi.value.filter { it.id != id }
                    Log.d("PROD", "producto local eliminado id=$id")
                }
            } catch (e: Exception) {
                Log.e("PROD", "deleteProducto exception", e)
                _errorMessage.value = e.localizedMessage ?: "Error de conexión"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun normalizeName(name: String) = name.trim().lowercase()

    fun agregarAlCarrito(producto: Producto) {
        val exists = _carrito.value.any { normalizeName(it.nombre) == normalizeName(producto.nombre) }
        val nuevoCarrito = if (exists) _carrito.value else _carrito.value + producto
        _carrito.value = nuevoCarrito
        guardarCarritoEnPrefs(nuevoCarrito)
    }

    fun eliminarDelCarrito(producto: Producto) {
        val target = normalizeName(producto.nombre)
        val nuevoCarrito = _carrito.value.filterNot { normalizeName(it.nombre) == target }
        _carrito.value = nuevoCarrito
        guardarCarritoEnPrefs(nuevoCarrito)
    }

    fun limpiarCarrito() {
        _carrito.value = emptyList()
        viewModelScope.launch {
            try {
                prefs.guardarCarrito(emptySet())
            } catch (e: Exception) {
                Log.e("PROD", "error guardando prefs", e)
            }
        }
    }

    private fun guardarCarritoEnPrefs(lista: List<Producto>) {
        viewModelScope.launch {
            try {
                val nombresLocal = lista.map { normalizeName(it.nombre) }
                    .filter { nombre ->
                        _productosLocal.value.any { local -> normalizeName(local.nombre) == nombre }
                    }.toSet()
                prefs.guardarCarrito(nombresLocal)
            } catch (e: Exception) {
                Log.e("PROD", "error guardando prefs", e)
            }
        }
    }

    private fun ProductoModel.toPostDto(): PostDto {
        return PostDto(
            userId = 1,
            id = if (this.id > 0) this.id.toString() else null,
            title = this.nombre,
            body = this.precio.toString(),
            imageName = this.imageName,
            imageUrl = this.imageUrl
        )
    }
}