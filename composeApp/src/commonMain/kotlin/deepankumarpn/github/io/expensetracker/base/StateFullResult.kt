package deepankumarpn.github.io.expensetracker.base

/**
 * Sealed class representing the result of an operation with different states.
 * Used to wrap data from use cases and repositories.
 */
sealed class StateFullResult<out T> {
    data object Loading : StateFullResult<Nothing>()
    data class Success<T>(val data: T) : StateFullResult<T>()
    data class Error(val message: String, val exception: Throwable? = null) : StateFullResult<Nothing>()

    fun isLoading(): Boolean = this is Loading
    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error

    fun getDataOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun getErrorOrNull(): String? = when (this) {
        is Error -> message
        else -> null
    }
}
