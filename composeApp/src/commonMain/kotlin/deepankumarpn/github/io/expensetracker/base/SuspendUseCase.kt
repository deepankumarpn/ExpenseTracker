package deepankumarpn.github.io.expensetracker.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Base class for suspend use cases that return a single result.
 *
 * @param PARAMS The input parameters type
 * @param RESULT The result type
 */
abstract class SuspendUseCase<in PARAMS, out RESULT>(
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : BaseUseCase(dispatcher) {

    /**
     * Execute the use case with parameters.
     * This method is executed on the provided dispatcher.
     */
    protected abstract suspend fun execute(params: PARAMS): RESULT

    /**
     * Invoke the use case with parameters.
     */
    suspend operator fun invoke(params: PARAMS): RESULT = withContext(dispatcher) {
        execute(params)
    }
}

/**
 * Extension for use cases that don't require parameters.
 */
abstract class SuspendUseCaseNoParams<out RESULT>(
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : BaseUseCase(dispatcher) {

    /**
     * Execute the use case without parameters.
     */
    protected abstract suspend fun execute(): RESULT

    /**
     * Invoke the use case.
     */
    suspend operator fun invoke(): RESULT = withContext(dispatcher) {
        execute()
    }
}
