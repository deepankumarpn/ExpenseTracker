package deepankumarpn.github.io.expensetracker.presentation.ui.screens.login

import deepankumarpn.github.io.expensetracker.base.UiEffect
import deepankumarpn.github.io.expensetracker.base.UiEvent
import deepankumarpn.github.io.expensetracker.base.UiState

/**
 * Contract for Login screen following MVI pattern.
 */
class LoginContract {
    /**
     * UI Events that can be triggered from the Login screen.
     */
    sealed class Event : UiEvent {
        data object SignInWithGoogleClicked : Event()
        data object RetryClicked : Event()
    }

    /**
     * UI State for the Login screen.
     */
    data class State(
        val isLoading: Boolean = false,
        val isAuthenticated: Boolean = false,
        val error: String? = null
    ) : UiState

    /**
     * One-time UI effects for the Login screen.
     */
    sealed class Effect : UiEffect {
        data object NavigateToHome : Effect()
        data class ShowError(val message: String) : Effect()
    }
}
