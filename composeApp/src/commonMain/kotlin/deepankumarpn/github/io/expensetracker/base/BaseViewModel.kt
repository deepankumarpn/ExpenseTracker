package deepankumarpn.github.io.expensetracker.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel that implements MVI pattern.
 *
 * @param STATE The UI state type
 * @param EVENT The UI event type
 * @param EFFECT The UI effect type
 */
abstract class BaseViewModel<STATE : UiState, EVENT : UiEvent, EFFECT : UiEffect>(
    initialState: STATE
) : ViewModel() {

    // State
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state.asStateFlow()

    // Effects (one-time events)
    private val _effect = Channel<EFFECT>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    /**
     * Current state value
     */
    protected val currentState: STATE
        get() = _state.value

    /**
     * Update state
     */
    protected fun setState(reducer: STATE.() -> STATE) {
        _state.value = currentState.reducer()
    }

    /**
     * Send a one-time effect
     */
    protected fun setEffect(effect: EFFECT) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    /**
     * Handle incoming UI events
     */
    abstract fun onEvent(event: EVENT)
}
