package deepankumarpn.github.io.expensetracker.navigation

/**
 * Sealed class representing all navigation routes in the app.
 */
sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object History : Screen("history")
    data object Settings : Screen("settings")
}
