package deepankumarpn.github.io.expensetracker.domain.model

import kotlinx.datetime.Instant

/**
 * Domain model representing a transaction category.
 */
data class Category(
    val id: String,
    val name: String,
    val isDefault: Boolean,
    val createdAt: Instant
)
