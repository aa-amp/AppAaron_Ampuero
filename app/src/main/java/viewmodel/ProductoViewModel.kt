package viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.appaaron_ampuero.R
import data.AppPreferencias
import kotlinx.coroutines.launch
import ui.screens.Product

class ProductoViewModel (application: Application) : AndroidViewModel(application) {
    private val prefs = AppPreferencias(application)

    private val _productos = MutableStateFlow(
        listOf(
            Product("Fideos Rigati 400g", R.drawable.product1, "$1.000"),
            Product("Alfi 45g", R.drawable.product2, "$700"),
            Product("Cheetos 40g", R.drawable.product4, "$1.000"),
            Product("Chocman 33g", R.drawable.product5, "$300"),
            Product("Lays 110g", R.drawable.product6, "$2.000"),
            Product("Golpe 27g", R.drawable.product7, "$500"),
            Product("Coca Cola Zero 1L", R.drawable.product8, "$1.100"),
            Product("Tableta BonoBon 270g", R.drawable.product3, "$3.500")
        )
    )
    val productos: StateFlow<List<Product>> = _productos

    private val _carrito = MutableStateFlow<List<Product>>(emptyList())
    val carrito: StateFlow<List<Product>> = _carrito

    init {
        viewModelScope.launch {
            prefs.obtenerCarrito().collect { ids ->
                val productosGuardados = _productos.value.filter { it.nombre in ids }
                _carrito.value = productosGuardados
            }
        }
    }
    fun agregarAlCarrito(producto: Product) {
        val nuevoCarrito = _carrito.value + producto
        _carrito.value = nuevoCarrito
        guardarCarritoEnPrefs(nuevoCarrito)
    }

    fun eliminarDelCarrito(producto: Product) {
        val nuevoCarrito = _carrito.value - producto
        _carrito.value = nuevoCarrito
        guardarCarritoEnPrefs(nuevoCarrito)
    }

    fun limpiarCarrito() {
        _carrito.value = emptyList()
        guardarCarritoEnPrefs(emptyList())
    }

    private fun guardarCarritoEnPrefs(lista: List<Product>) {
        viewModelScope.launch {
            prefs.guardarCarrito(lista.map { it.nombre }.toSet())
        }
    }
}
