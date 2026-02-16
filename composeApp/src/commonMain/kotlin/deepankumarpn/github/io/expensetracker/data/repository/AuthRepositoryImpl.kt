package deepankumarpn.github.io.expensetracker.data.repository

import deepankumarpn.github.io.expensetracker.base.StateFullResult
import deepankumarpn.github.io.expensetracker.domain.model.UserProfile
import deepankumarpn.github.io.expensetracker.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * In-memory implementation of AuthRepository.
 */
class AuthRepositoryImpl : AuthRepository {
    private val currentUser = MutableStateFlow<UserProfile?>(null)

    override suspend fun signInWithGoogle(): StateFullResult<UserProfile> {
        return try {
            // Mock user profile
            val profile = UserProfile(
                name = "Demo User",
                email = "user@example.com",
                spreadsheetId = "mock-spreadsheet-id"
            )
            currentUser.value = profile
            StateFullResult.Success(profile)
        } catch (e: Exception) {
            StateFullResult.Error(e.message ?: "Failed to sign in")
        }
    }

    override suspend fun signOut(): StateFullResult<Unit> {
        return try {
            currentUser.value = null
            StateFullResult.Success(Unit)
        } catch (e: Exception) {
            StateFullResult.Error(e.message ?: "Failed to sign out")
        }
    }

    override fun getCurrentUser(): Flow<UserProfile?> = currentUser

    override suspend fun isAuthenticated(): Boolean = currentUser.value != null

    suspend fun getUserProfile(): UserProfile? = currentUser.value
}
