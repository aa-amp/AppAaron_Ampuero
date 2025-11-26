package model

data class CarritoModel(
    val id: Int = 0,
    val productoId: Int = 0,
    val nombre: String = "",
    val cantidad: Int = 1,
    val precio: Int = 0
)