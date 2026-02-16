package deepankumarpn.github.io.expensetracker.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import deepankumarpn.github.io.expensetracker.presentation.ui.components.BottomNavigationBar
import deepankumarpn.github.io.expensetracker.presentation.ui.screens.login.LoginScreen
import deepankumarpn.github.io.expensetracker.presentation.ui.screens.login.LoginViewModel
import deepankumarpn.github.io.expensetracker.presentation.ui.screens.home.HomeScreen
import deepankumarpn.github.io.expensetracker.presentation.ui.screens.home.HomeViewModel
import deepankumarpn.github.io.expensetracker.presentation.ui.screens.history.HistoryScreen
import deepankumarpn.github.io.expensetracker.presentation.ui.screens.history.HistoryViewModel
import deepankumarpn.github.io.expensetracker.presentation.ui.screens.settings.SettingsScreen
import deepankumarpn.github.io.expensetracker.presentation.ui.screens.settings.SettingsContract
import deepankumarpn.github.io.expensetracker.presentation.ui.screens.settings.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Main navigation graph for the application.
 */
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Login Screen
        composable(Screen.Login.route) {
            val viewModel = koinViewModel<LoginViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is deepankumarpn.github.io.expensetracker.presentation.ui.screens.login.LoginContract.Effect.NavigateToHome -> {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                        else -> {}
                    }
                }
            }

            LoginScreen(
                state = state,
                onEvent = viewModel::onEvent
            )
        }

        // Main App with Bottom Navigation
        composable(Screen.Home.route) {
            MainScreen(navController)
        }
        composable(Screen.History.route) {
            MainScreen(navController)
        }
        composable(Screen.Settings.route) {
            MainScreen(navController)
        }
    }
}

/**
 * Main screen with bottom navigation.
 */
@Composable
private fun MainScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentRoute) {
                Screen.Home.route -> {
                    val viewModel = koinViewModel<HomeViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()

                    HomeScreen(
                        state = state,
                        onEvent = viewModel::onEvent
                    )
                }
                Screen.History.route -> {
                    val viewModel = koinViewModel<HistoryViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()

                    HistoryScreen(
                        state = state,
                        onEvent = viewModel::onEvent
                    )
                }
                Screen.Settings.route -> {
                    val viewModel = koinViewModel<SettingsViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()

                    LaunchedEffect(Unit) {
                        viewModel.effect.collect { effect ->
                            when (effect) {
                                is SettingsContract.Effect.NavigateToLogin -> {
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                                else -> {}
                            }
                        }
                    }

                    SettingsScreen(
                        state = state,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }
    }
}
