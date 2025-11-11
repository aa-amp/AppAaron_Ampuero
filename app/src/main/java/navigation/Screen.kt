package navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Login : Screen("login")

    object Registro : Screen("registro")
    object Product : Screen("product")
    object Cart : Screen("cart")

    object Resumen : Screen("resumen")

    object Dueno : Screen("dueno")
    object AgregarProducto : Screen("agregarProducto")
    object GestionarProductos : Screen("gestionarProductos")

    object estado : Screen("estado")
    object PerfilDueno : Screen("perfildueno")

    object UsuarioDatos: Screen("usuarios_datos")

}