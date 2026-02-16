package deepankumarpn.github.io.expensetracker.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Base class for all use cases.
 * Provides dispatcher for background operations.
 */
abstract class BaseUseCase(
    protected val dispatcher: CoroutineDispatcher = Dispatchers.Default
)
