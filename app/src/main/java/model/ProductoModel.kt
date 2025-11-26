package model

data class ProductoModel(
    val id: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Int = 0,
    val stock: Int = 0,
    val imagenRes: Int = 0,      // drawable
    val imageName: String? = null, // db.json para resolver drawable
    val imageUrl: String? = null,  // url
    val categoria: String = ""
)