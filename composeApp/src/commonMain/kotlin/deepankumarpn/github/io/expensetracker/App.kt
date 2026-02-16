package deepankumarpn.github.io.expensetracker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import deepankumarpn.github.io.expensetracker.di.appModule
import deepankumarpn.github.io.expensetracker.navigation.NavGraph
import deepankumarpn.github.io.expensetracker.presentation.ui.theme.ExpenseTrackerTheme
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        ExpenseTrackerTheme {
            NavGraph()
        }
    }
}