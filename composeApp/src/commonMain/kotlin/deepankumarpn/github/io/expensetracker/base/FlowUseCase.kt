package deepankumarpn.github.io.expensetracker.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Base class for flow-based use cases that return continuous streams of data.
 *
 * @param PARAMS The input parameters type
 * @param RESULT The result type emitted by the Flow
 */
abstract class FlowUseCase<in PARAMS, out RESULT>(
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : BaseUseCase(dispatcher) {

    /**
     * Execute the use case with parameters.
     * This method returns a Flow that emits results on the provided dispatcher.
     */
    protected abstract fun execute(params: PARAMS): Flow<RESULT>

    /**
     * Invoke the use case with parameters.
     */
    operator fun invoke(params: PARAMS): Flow<RESULT> = execute(params).flowOn(dispatcher)
}

/**
 * Extension for flow use cases that don't require parameters.
 */
abstract class FlowUseCaseNoParams<out RESULT>(
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : BaseUseCase(dispatcher) {

    /**
     * Execute the use case without parameters.
     */
    protected abstract fun execute(): Flow<RESULT>

    /**
     * Invoke the use case.
     */
    operator fun invoke(): Flow<RESULT> = execute().flowOn(dispatcher)
}
