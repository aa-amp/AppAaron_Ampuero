package viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.appaaron_ampuero.R
import data.AppPreferencias
import model.Producto

class ProductoViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = AppPreferencias(application)

    private val _productos = MutableStateFlow(
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
    val productos: StateFlow<List<Producto>> = _productos

    private val _carrito = MutableStateFlow<List<Producto>>(emptyList())
    val carrito: StateFlow<List<Producto>> = _carrito

    init {
        viewModelScope.launch {
            prefs.obtenerCarrito().collect { ids ->
                val productosGuardados = _productos.value.filter { it.nombre in ids }
                _carrito.value = productosGuardados
            }
        }
    }

    fun agregarAlCarrito(producto: Producto) {
        val nuevoCarrito = _carrito.value + producto
        _carrito.value = nuevoCarrito
        guardarCarritoEnPrefs(nuevoCarrito)
    }

    fun eliminarDelCarrito(producto: Producto) {
        val nuevoCarrito = _carrito.value - producto
        _carrito.value = nuevoCarrito
        guardarCarritoEnPrefs(nuevoCarrito)
    }

    fun limpiarCarrito() {
        _carrito.value = emptyList()
        guardarCarritoEnPrefs(emptyList())
    }

    private fun guardarCarritoEnPrefs(lista: List<Producto>) {
        viewModelScope.launch {
            prefs.guardarCarrito(lista.map { it.nombre }.toSet())
        }
    }
}
