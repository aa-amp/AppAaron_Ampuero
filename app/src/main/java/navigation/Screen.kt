package navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Login : Screen("login")

    object Registro : Screen("registro")
    object Product : Screen("product")
    object Cart : Screen("cart")
}
