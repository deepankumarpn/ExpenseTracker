package deepankumarpn.github.io.expensetracker.domain.repository

import deepankumarpn.github.io.expensetracker.base.StateFullResult
import deepankumarpn.github.io.expensetracker.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for authentication operations.
 */
interface AuthRepository {
    /**
     * Sign in with Google.
     * @return Result containing user profile if successful.
     */
    suspend fun signInWithGoogle(): StateFullResult<UserProfile>

    /**
     * Sign out the current user.
     */
    suspend fun signOut(): StateFullResult<Unit>

    /**
     * Get current user profile if signed in.
     * @return Flow emitting current user profile or null if not signed in.
     */
    fun getCurrentUser(): Flow<UserProfile?>

    /**
     * Check if user is authenticated.
     */
    suspend fun isAuthenticated(): Boolean
}
