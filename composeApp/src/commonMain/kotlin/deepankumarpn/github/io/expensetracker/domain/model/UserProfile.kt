package deepankumarpn.github.io.expensetracker.domain.model

/**
 * Domain model representing user profile information.
 */
data class UserProfile(
    val name: String,
    val email: String,
    val spreadsheetId: String
)
