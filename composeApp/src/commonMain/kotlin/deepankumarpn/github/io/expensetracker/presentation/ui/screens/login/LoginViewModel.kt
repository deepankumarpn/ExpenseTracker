package deepankumarpn.github.io.expensetracker.presentation.ui.screens.login

import androidx.lifecycle.viewModelScope
import deepankumarpn.github.io.expensetracker.base.BaseViewModel
import deepankumarpn.github.io.expensetracker.base.StateFullResult
import deepankumarpn.github.io.expensetracker.domain.repository.AuthRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for Login screen.
 */
class LoginViewModel(
    private val authRepository: AuthRepository
) : BaseViewModel<LoginContract.State, LoginContract.Event, LoginContract.Effect>(
    initialState = LoginContract.State()
) {

    init {
        checkAuthenticationStatus()
    }

    override fun onEvent(event: LoginContract.Event) {
        when (event) {
            LoginContract.Event.SignInWithGoogleClicked -> signInWithGoogle()
            LoginContract.Event.RetryClicked -> signInWithGoogle()
        }
    }

    private fun checkAuthenticationStatus() {
        viewModelScope.launch {
            val isAuthenticated = authRepository.isAuthenticated()
            if (isAuthenticated) {
                setEffect(LoginContract.Effect.NavigateToHome)
            }
        }
    }

    private fun signInWithGoogle() {
        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }

            when (val result = authRepository.signInWithGoogle()) {
                is StateFullResult.Success -> {
                    setState { copy(isLoading = false, isAuthenticated = true) }
                    setEffect(LoginContract.Effect.NavigateToHome)
                }
                is StateFullResult.Error -> {
                    setState { copy(isLoading = false, error = result.message) }
                    setEffect(LoginContract.Effect.ShowError(result.message))
                }
                is StateFullResult.Loading -> {
                    setState { copy(isLoading = true) }
                }
            }
        }
    }
}
