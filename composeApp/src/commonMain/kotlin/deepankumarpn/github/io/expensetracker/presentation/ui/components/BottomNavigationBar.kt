package deepankumarpn.github.io.expensetracker.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import deepankumarpn.github.io.expensetracker.navigation.Screen
import deepankumarpn.github.io.expensetracker.presentation.ui.theme.ExpenseTrackerTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Bottom navigation bar composable with three tabs.
 */
@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        BottomNavItem.entries.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                alwaysShowLabel = true
            )
        }
    }
}

/**
 * Enum representing bottom navigation items.
 */
enum class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    HOME(
        route = Screen.Home.route,
        title = "Home",
        icon = Icons.Default.Home
    ),
    HISTORY(
        route = Screen.History.route,
        title = "History",
        icon = Icons.Default.Receipt
    ),
    SETTINGS(
        route = Screen.Settings.route,
        title = "Settings",
        icon = Icons.Default.Settings
    )
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
    ExpenseTrackerTheme {
        BottomNavigationBar(
            currentRoute = Screen.Home.route,
            onNavigate = {}
        )
    }
}

@Preview
@Composable
fun BottomNavigationBarHistoryPreview() {
    ExpenseTrackerTheme {
        BottomNavigationBar(
            currentRoute = Screen.History.route,
            onNavigate = {}
        )
    }
}

@Preview
@Composable
fun BottomNavigationBarDarkPreview() {
    ExpenseTrackerTheme(darkTheme = true) {
        BottomNavigationBar(
            currentRoute = Screen.Settings.route,
            onNavigate = {}
        )
    }
}
